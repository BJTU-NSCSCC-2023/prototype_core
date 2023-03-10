package pipeline.bypass
import chisel3._
import chisel3.util._
import config.BaseConfig

/**
  * Pc Generate Unit
  */
class PcGU(implicit cfg: BaseConfig) extends Module {
  val infoFromIf = IO(Flipped(new InfoFromIfuToPcguBundle))
  val infoFromId = IO(Flipped(new InfoFromIduToPcguBundle))
  val infoFromEx = IO(Flipped(new InfoFromExuToPcguBundle))
  val infoFromWb = IO(Flipped(new InfoFromWbuToPcguBundle))
  val toIf       = IO(new PcguNpcBundle)
  toIf.invalid :=
    (infoFromIf.en && (infoFromIf.isException || infoFromIf.isInterrupt || infoFromIf.isOipp)) ||
      (infoFromId.en && (infoFromId.isException || infoFromId.isInterrupt || infoFromId.isOipp)) ||
      (infoFromEx.en && (infoFromEx.isException || infoFromEx.isInterrupt || infoFromEx.isOipp))
  toIf.valid := true.B
  toIf.npc := Mux(
    infoFromWb.en && (infoFromWb.isException || infoFromWb.isInterrupt || infoFromWb.isOipp),
    infoFromWb.npc,
    Mux(
      infoFromId.en && infoFromId.branchEn && !infoFromId.invalid,
      infoFromId.npc,
      infoFromIf.npc
//      cfg.magicLA.U
    )
  )
}

class PcguNpcBundle(implicit cfg: BaseConfig) extends Bundle {
  val npc     = Output(UInt(cfg.laLen.W))
  val valid   = Output(Bool())
  val invalid = Output(Bool())
}

class InfoFromStageUnitToPcguBundle(implicit cfg: BaseConfig) extends Bundle {
  val en          = Output(Bool()) //  info is enabled (work && "info is generated")
  val isException = Output(Bool()) //  this stage is in exception
  val isInterrupt = Output(Bool()) //  this stage is in interrupt
  val isOipp      = Output(Bool()) //  Oipp = One inst per pipeline, is this stage handling oipp inst (cp0, ll, sc, etc.)
}

class InfoFromIfuToPcguBundle(implicit cfg: BaseConfig) extends InfoFromStageUnitToPcguBundle {
  val npc = Output(UInt(cfg.laLen.W)) //  pc+4
}
class InfoFromIduToPcguBundle(implicit cfg: BaseConfig) extends InfoFromStageUnitToPcguBundle {
  val branchEn = Output(Bool()) //  is branch
  val npc      = Output(UInt(cfg.laLen.W)) //  branch npc
  val invalid  = Output(Bool()) // is invalid (for stall)
}
class InfoFromExuToPcguBundle(implicit p: BaseConfig) extends InfoFromStageUnitToPcguBundle {}
class InfoFromWbuToPcguBundle(implicit p: BaseConfig) extends InfoFromStageUnitToPcguBundle {
  val npc = Output(UInt(p.laLen.W)) //  npc from wb stage (like exception handler address)
}
