package stages
import chisel3._
import chisel3.util._
import config.Config

class CuStage(implicit config: Config) extends Module {
  val io = IO(new Bundle {
    val toIf = new Cu2IfBundle
  })
  //  digital circuit components
  val rPc   = RegInit(config.pcResetVal.U)
  val valid = true.B

  //  helper signals
  val updIf = io.toIf.ready && valid

  //  io connections
  io.toIf.valid := valid
  io.toIf.pc    := rPc

  //  component connections
  rPc := Mux(
    updIf,
    rPc + 4.U,
    rPc
  )
}
