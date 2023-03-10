package pipeline.bypass
import chisel3._
import chisel3.util._
import config.BaseConfig

/**
  * Invalid Control Unit
  */
class IvCU(implicit cfg: BaseConfig) extends Module {
  val fromWbu = IO(Flipped(new InfoFromStageUnitToIvcuBundle))
  val fromExu = IO(Flipped(new InfoFromStageUnitToIvcuBundle))
  val fromIdu = IO(Flipped(new InfoFromStageUnitToIvcuBundle))
  val fromIfu = IO(Flipped(new InfoFromStageUnitToIvcuBundle))

  fromWbu.invalid := false.B
  fromExu.invalid := fromWbu.invalid || (fromWbu.en && (fromWbu.isException || fromWbu.isInterrupt || fromWbu.isOipp))
  fromIdu.invalid := fromExu.invalid || (fromExu.en && (fromExu.isException || fromExu.isInterrupt || fromExu.isOipp))
  fromIfu.invalid := fromIdu.invalid || (fromIdu.en && (fromIdu.isException || fromIdu.isInterrupt || fromIdu.isOipp))
}

class InfoFromStageUnitToIvcuBundle(implicit cfg: BaseConfig) extends Bundle {
  val en          = Output(Bool()) //  info is enabled (work && "info is generated")
  val isException = Output(Bool()) //  this stage is in exception
  val isInterrupt = Output(Bool()) //  this stage is in interrupt
  val isOipp      = Output(Bool()) //  Oipp = One inst per pipeline, is this stage handling oipp inst (cp0, ll, sc, etc.)
  val invalid     = Input(Bool())
}
