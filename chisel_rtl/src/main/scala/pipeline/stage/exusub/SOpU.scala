package pipeline.stage.exusub
import chisel3._
import chisel3.util._
import pipeline.bypass.{InfoFromExuToPcguBundle, InfoFromExuToScuBundle, InfoFromFwuToExuBundle, InfoFromStageUnitToIvcuBundle}
import config.{BaseConfig, preDefine => pdf}

class SOpU(implicit cfg: BaseConfig) extends Module {
  val cur = IO(new Bundle {
    val valid   = Output(Bool())
    val invalid = Output(Bool())
    val ready   = Output(Bool())
    val data = new Bundle {
      val wbEn  = Output(Bool())
      val wrId  = Output(UInt(cfg.xidLen.W))
      val wrVal = Output(UInt(cfg.xLen.W))
      val eInfo = Output(UInt(pdf.eh._len.W))
      val iInfo = Output(UInt(pdf.ih._len.W))
      val pc    = Output(UInt(cfg.laLen.W))
    }
  })
  val pre = IO(new Bundle {
    val valid   = Input(Bool())
    val invalid = Input(Bool())
    val data = new Bundle {
      val idInfo  = Input(UInt(pdf.id._len.W))
      val rsId    = Input(UInt(cfg.xidLen.W))
      val rsValue = Input(UInt(cfg.xLen.W))
      val rtId    = Input(UInt(cfg.xidLen.W))
      val rtValue = Input(UInt(cfg.xLen.W))
      val rdId    = Input(UInt(cfg.xidLen.W))
      val sh5     = Input(UInt(5.W))
      val imm16   = Input(UInt(16.W))
      val imm26   = Input(UInt(26.W))
      val eInfo   = Input(UInt(pdf.eh._len.W))
      val iInfo   = Input(UInt(pdf.ih._len.W))
      val pc      = Input(UInt(cfg.laLen.W))
    }
  })
  val nxt = IO(new Bundle {
    val ready = Input(Bool())
  })
  val work        = IO(Output(Bool()))
  val infoFromFwu = IO(Flipped(new InfoFromFwuToExuBundle))
  val infoToPcgu  = IO(new InfoFromExuToPcguBundle)
  val infoToScu   = IO(new InfoFromExuToScuBundle)
  val infoToIvcu  = IO(new InfoFromStageUnitToIvcuBundle)
  // TODO:

}
