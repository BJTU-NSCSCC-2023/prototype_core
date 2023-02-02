package config
import chisel3._
import chisel3.util._

class Config {
  val gprNum     = 32
  val pcResetVal = 0
  val pcResetValid = true
  val virAddrWid = 32
  val wordWid    = 32

  val magicVal = 0x0c0c0c0c

  object MemAccessProxyType extends Enumeration {
    val bare_memory = Value("bare_memory")
  }
  val memAccessProxyType       = MemAccessProxyType.bare_memory
  val bareMemFilePath          = ""
  val bareMemSize              = 4096 //  in word
  val bareMemWrWaitingStepsNum = 1
  val bareMemRdWaitingStepsNum = 1
  val memoryWaitingStepWid     = 8
}
