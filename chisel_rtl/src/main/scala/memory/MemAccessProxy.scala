package memory
import chisel3._
import chisel3.util._
import config.Config

/**
  * See doc: docs/intro_memory_access_proxy.md for design description.
  */

class InstMemAccessProxyBundle(implicit config: Config) extends Bundle {
  val virAddr           = Input(UInt(config.virAddrWid.W))
  val writeVal          = Input(UInt(config.wordWid.W))
  val writeEnable       = Input(Bool())
  val ce                = Input(Bool())
  val readRes           = Output(UInt(config.wordWid.W))
  val wrWaitingStepsNum = Output(UInt(config.memoryWaitingStepWid.W))
  val rdWaitingStepsNum = Output(UInt(config.memoryWaitingStepWid.W))
  val cacheHit          = Output(Bool())
}

class DataMemAccessProxyBundle(implicit config: Config) extends Bundle {
  val virAddr           = Input(UInt(config.virAddrWid.W))
  val writeVal          = Input(UInt(config.wordWid.W))
  val writeEnable       = Input(Bool())
  val ce                = Input(Bool())
  val readRes           = Output(UInt(config.wordWid.W))
  val wrWaitingStepsNum = Output(UInt(config.memoryWaitingStepWid.W))
  val rdWaitingStepsNum = Output(UInt(config.memoryWaitingStepWid.W))
  val cacheHit          = Output(Bool())
}

class BareMemory(implicit config: Config) extends Module {
  val instMemAccessProxy = IO(new InstMemAccessProxyBundle)
  val dataMemAccessProxy = IO(new DataMemAccessProxyBundle)
  val mem                = SyncReadMem(config.bareMemSize, UInt(config.wordWid.W))

  instMemAccessProxy.wrWaitingStepsNum := config.bareMemWrWaitingStepsNum.U
  instMemAccessProxy.rdWaitingStepsNum := config.bareMemRdWaitingStepsNum.U
  instMemAccessProxy.cacheHit          := true.B
  dataMemAccessProxy.wrWaitingStepsNum := config.bareMemWrWaitingStepsNum.U
  dataMemAccessProxy.rdWaitingStepsNum := config.bareMemRdWaitingStepsNum.U
  dataMemAccessProxy.cacheHit          := true.B

  instMemAccessProxy.readRes := DontCare
  when(instMemAccessProxy.ce) {
    when(instMemAccessProxy.writeEnable) {
      //  WARNING: virAddr is wider than address bit
      mem.write(instMemAccessProxy.virAddr, instMemAccessProxy.writeVal)
    }.otherwise {
      instMemAccessProxy.readRes := mem.read(instMemAccessProxy.virAddr)
    }
  }

  dataMemAccessProxy.readRes := DontCare
  when(dataMemAccessProxy.ce) {
    when(dataMemAccessProxy.writeEnable) {
      //  WARNING: virAddr is wider than address bit
      mem.write(dataMemAccessProxy.virAddr, dataMemAccessProxy.writeVal)
    }.otherwise {
      dataMemAccessProxy.readRes := mem.read(dataMemAccessProxy.virAddr)
    }
  }
}

class MemAccessProxy(implicit config: Config) extends Module {
  val instMemAccessProxy = IO(new InstMemAccessProxyBundle)
  val dataMemAccessProxy = IO(new DataMemAccessProxyBundle)

  config.memAccessProxyType match {
    case config.MemAccessProxyType.bare_memory =>
      val bareMemory = Module(new BareMemory)
      bareMemory.instMemAccessProxy <> instMemAccessProxy
      bareMemory.dataMemAccessProxy <> dataMemAccessProxy
  }
}
