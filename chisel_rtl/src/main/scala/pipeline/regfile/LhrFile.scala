package pipeline.regfile
import chisel3._
import chisel3.util._
import config.BaseConfig
class LhrFile(implicit cfg: BaseConfig) extends Module {
  val lo = IO(new Bundle {
    val wen  = Input(Bool())
    val wVal = Input(UInt(cfg.xLen.W))
    val rVal = Output(UInt(cfg.xLen.W))
  })
  val hi = IO(new Bundle {
    val wen  = Input(Bool())
    val wVal = Input(UInt(cfg.xLen.W))
    val rVal = Output(UInt(cfg.xLen.W))
  })

  val rLo = RegInit(cfg.magicVal.U(cfg.xLen.W))
  val rHi = RegInit(cfg.magicVal.U(cfg.xLen.W))

  rLo     := Mux(lo.wen, lo.wVal, rLo)
  lo.rVal := rLo

  rHi     := Mux(hi.wen, hi.wVal, rHi)
  hi.rVal := rHi

}
