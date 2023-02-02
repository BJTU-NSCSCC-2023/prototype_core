package stages
import chisel3._
import chisel3.util._
import config.Config
import stages._
import memory._
import stages._

class Cu2IfBundle(implicit config: Config) extends Bundle {
  val valid = Output(Bool())
  val ready = Input(Bool())
  val pc    = Output(UInt(config.virAddrWid.W))
}

class IfStage(implicit config: Config) extends Module {
  val io = IO(new Bundle {
    val fromCu    = Flipped(new Cu2IfBundle)
    val toId      = new If2IdBundle
    val toInstMem = Flipped(new InstMemAccessProxyBundle)
  })

  //  digital circuit components
  val ready = Wire(Bool())
  val valid = Wire(Bool())
  val nowSrc = RegInit({
    val b = new Bundle {
      val valid = Bool()
      val pc    = UInt(config.virAddrWid.W)
    }
    b.valid := false.B
    b.pc    := config.magicVal.U
    b
  })
  val state = RegInit({
    val b = new Bundle {
      val c = UInt(config.memoryWaitingStepWid.W)
    }
    b.c := 0.U
    b
  })
  val ctx = RegInit({
    val b = new Bundle {
      val instBits = UInt(config.wordWid.W)
    }
    b.instBits := config.magicVal.U
    b
  })

  //  helper signals
  val updThis = ready && io.fromCu.valid
  val updNext = valid && io.toId.ready
  val n       = io.toInstMem.rdWaitingStepsNum

  //  io connections
  io.fromCu.ready := ready
  io.toId.valid   := valid
  io.toId.instBits := Mux(
    state.c === n && io.toInstMem.cacheHit,
    io.toInstMem.readRes,
    ctx.instBits
  )
  io.toId.thisPc           := nowSrc.pc
  io.toInstMem.virAddr     := nowSrc.pc
  io.toInstMem.writeVal    := config.magicVal.U
  io.toInstMem.writeEnable := false.B
  io.toInstMem.ce          := nowSrc.valid

  //  component connections
  valid := nowSrc.valid && ((state.c === n && io.toInstMem.cacheHit) || state.c === 1.U || state.c === 0.U)
  ready := Mux(
    nowSrc.valid,
    Mux(
      valid,
      io.toId.valid,
      false.B
    ),
    true.B
  )
  state.c := Mux(
    updThis,
    n,
    Mux(state.c === 0.U, 0.U, state.c - 1.U)
  )
  ctx.instBits := Mux(
    valid,
    Mux(state.c =/= 0.U, io.toInstMem.readRes, ctx.instBits),
    config.magicVal.U
  )
  nowSrc.valid := Mux(
    updThis,
    true.B,
    Mux(
      updNext,
      false.B,
      nowSrc.valid
    )
  )
  nowSrc.pc := Mux(
    updThis,
    io.fromCu.pc,
    nowSrc.pc
  )
}
