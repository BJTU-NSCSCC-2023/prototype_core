package pipeline.stage

import chisel3._
import chisel3.util._
import pipeline.bypass.{InfoFromWbuToPcguBundle, InfoFromStageUnitToIvcuBundle, InfoFromWbuToFwuBundle}
import config.{BaseConfig, preDefine => pdf}

/**
  * Write Back Unit
  */
class WBU(implicit cfg: BaseConfig) extends Module {
  val cur = IO(new Bundle {
    val valid   = Output(Bool())
    val invalid = Output(Bool())
    val ready   = Output(Bool())
    val data = new Bundle {
      // may be no need?
      val eInfo = Output(UInt(pdf.eh._len.W))
      val iInfo = Output(UInt(pdf.ih._len.W))
      val pc    = Output(UInt(cfg.laLen.W))
    }
  })
  val pre = IO(new Bundle {
    val valid   = Input(Bool())
    val invalid = Input(Bool())
    val data = new Bundle {
      val wbEn  = Input(Bool())
      val wrId  = Input(UInt(cfg.xidLen.W))
      val wrVal = Input(UInt(cfg.xLen.W))
      val eInfo = Input(UInt(pdf.eh._len.W))
      val iInfo = Input(UInt(pdf.ih._len.W))
      val pc    = Input(UInt(cfg.laLen.W))
    }
    val sel = Output(UInt(pdf.exSel._len.W))
  })
  val nxt = IO(new Bundle {
    val ready = Input(Bool())
  })
  val work    = IO(Output(Bool()))
  val infoToFwu = IO(new InfoFromWbuToFwuBundle)
  val infoToPcgu = IO(new InfoFromWbuToPcguBundle)
  val infoToIvcu = IO(new InfoFromStageUnitToIvcuBundle)
  // TODO: impl of exu

}
