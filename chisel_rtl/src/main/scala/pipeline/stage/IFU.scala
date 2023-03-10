package pipeline.stage

import chisel3._
import chisel3.util._
import pipeline.bypass.{InfoFromIfuToPcguBundle,InfoFromStageUnitToIvcuBundle}
import config.{BaseConfig, preDefine => pdf}

/**
  * Inst Fetch Unit
  */
class IFU(implicit cfg: BaseConfig) extends Module {
  val cur = IO(new Bundle {
    val valid   = Output(Bool())
    val invalid = Output(Bool())
    val ready   = Output(Bool())
    val data = new Bundle {
      val instBits = Output(UInt(pdf.instLen.W))
      val eInfo    = Output(UInt(pdf.eh._len.W))
      val iInfo    = Output(UInt(pdf.ih._len.W))
      val pc       = Output(UInt(cfg.laLen.W))
    }
  })
  val pre = IO(new Bundle {
    val valid   = Input(Bool())
    val invalid = Input(Bool())
    val data = new Bundle {
      val npc = Input(UInt(cfg.laLen.W))
    }
  })
  val nxt = IO(new Bundle {
    val ready = Input(Bool())
  })
  val work    = IO(Output(Bool()))
  val infoToPcgu = IO(new InfoFromIfuToPcguBundle)
  val infoToIvcu = IO(new InfoFromStageUnitToIvcuBundle)
  // TODO: impl of ifu
}
