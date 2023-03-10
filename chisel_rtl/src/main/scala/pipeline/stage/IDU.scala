package pipeline.stage

import chisel3._
import chisel3.util._
import pipeline.bypass.{InfoFromIduToPcguBundle,InfoFromIduToScuBundle,InfoFromStageUnitToIvcuBundle}
import config.{BaseConfig, preDefine => pdf}

/**
  * Inst Decode Unit
  */
class IDU(implicit cfg: BaseConfig) extends Module {
  val cur = IO(new Bundle {
    val valid   = Output(Bool())
    val invalid = Output(Bool())
    val ready   = Output(Bool())
    val data = new Bundle {
      val idInfo  = Output(UInt(pdf.id._len.W))
      val rsId    = Output(UInt(cfg.xidLen.W))
      val rsValue = Output(UInt(cfg.xLen.W))
      val rtId    = Output(UInt(cfg.xidLen.W))
      val rtValue = Output(UInt(cfg.xLen.W))
      val rdId    = Output(UInt(cfg.xidLen.W))
      val sh5     = Output(UInt(5.W))
      val imm16   = Output(UInt(16.W))
      val imm26   = Output(UInt(26.W))
      val eInfo   = Output(UInt(pdf.eh._len.W))
      val iInfo   = Output(UInt(pdf.ih._len.W))
      val pc      = Output(UInt(cfg.laLen.W))
    }
    val sel = Output(UInt(pdf.exSel._len.W))
  })
  val pre = IO(new Bundle {
    val valid   = Input(Bool())
    val invalid = Input(Bool())
    val data = new Bundle {
      val instBits = Input(UInt(pdf.instLen.W))
      val eInfo    = Input(UInt(pdf.eh._len.W))
      val iInfo    = Input(UInt(pdf.ih._len.W))
      val pc       = Input(UInt(cfg.laLen.W))
    }
  })
  val nxt = IO(new Bundle {
    val ready = Input(Bool())
  })
  val work    = IO(Output(Bool()))
  val stall   = IO(Input(Bool()))
  val infoToPcgu = IO(new InfoFromIduToPcguBundle)
  val infoToScu = IO(new InfoFromIduToScuBundle)
  val infoToIvcu = IO(new InfoFromStageUnitToIvcuBundle)
  // TODO: impl of idu

}
