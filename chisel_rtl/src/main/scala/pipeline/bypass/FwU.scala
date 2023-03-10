package pipeline.bypass
import chisel3._
import chisel3.util._
import config.BaseConfig

/**
  * Forward Unit
  */
class FwU(implicit cfg: BaseConfig) extends Module {
  val infoToExu   = IO(new InfoFromFwuToExuBundle)
  val infoFromWbu = IO(Flipped(new InfoFromWbuToFwuBundle))
  infoToExu.regFwEn := infoFromWbu.en
  infoToExu.regFwId := infoFromWbu.drid
  infoToExu.regVal  := infoFromWbu.regVal
}

class InfoFromFwuToExuBundle(implicit cfg: BaseConfig) extends Bundle {
  val regFwEn = Output(Bool())
  val regFwId = Output(UInt(cfg.xidLen.W))
  val regVal  = Output(UInt(cfg.xLen.W))
}

class InfoFromWbuToFwuBundle(implicit cfg: BaseConfig) extends Bundle {
  val en     = Output(Bool())
  val drid   = Output(UInt(cfg.xidLen.W))
  val regVal = Output(UInt(cfg.xLen.W))
}
