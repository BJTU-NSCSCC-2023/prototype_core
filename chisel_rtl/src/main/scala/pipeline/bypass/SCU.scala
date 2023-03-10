package pipeline.bypass
import chisel3._
import chisel3.util._
import config.BaseConfig

/**
  * Stall Control Unit
  */
class SCU(implicit cfg: BaseConfig) extends Module {
  val infoFromIdu = IO(Flipped(new InfoFromIduToScuBundle))
  val infoFromExu = IO(Flipped(new InfoFromExuToScuBundle))
  infoFromIdu.stall :=
    infoFromIdu.en && infoFromExu.en &&
      (infoFromIdu.lrid === infoFromExu.drid || infoFromIdu.rrid === infoFromExu.drid) &&
      infoFromExu.drid.orR
}

class InfoFromIduToScuBundle(implicit cfg: BaseConfig) extends Bundle {
  val en    = Output(Bool()) //  info is effective
  val lrid  = Output(UInt(cfg.xidLen.W)) //  left register id
  val rrid  = Output(UInt(cfg.xidLen.W)) //  right register id
  val stall = Input(Bool())
}

class InfoFromExuToScuBundle(implicit cfg: BaseConfig) extends Bundle {
  val en   = Output(Bool()) // info is effective
  val drid = Output(UInt(cfg.xidLen.W)) //  dest register id
}
