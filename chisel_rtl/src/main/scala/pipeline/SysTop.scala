package pipeline
import chisel3._
import chisel3.util._
import config.BaseConfig
import pipeline.bypass._
import pipeline.regfile._
import pipeline.stage._

class SysTop extends Module {
  implicit val cfg = (new BaseConfig)

  //  pipeline
  val ifu = Module(new IFU)
  val idu = Module(new IDU)
  val exu = Module(new EXU)
  val wbu = Module(new WBU)

  //  bypass
  val pcgu = Module(new PcGU)
  val fwu  = Module(new FwU)
  val ivcu = Module(new IvCU)
  val scu  = Module(new SCU)
  val shcu = Module(new SHCU)

  //  regfile
  val pgrFile = Module(new GprFile)
  val lhrFile = Module(new LhrFile)
  val cp0File = Module(new Cp0File)

  //  SHCU
  def doShcu() = {
    { //  shcu ifu
      shcu.ifu.invalid := ifu.cur.invalid
      shcu.ifu.valid   := ifu.cur.valid
      shcu.ifu.ready   := ifu.cur.ready
      shcu.ifu.work    := ifu.work
      ifu.pre.valid    := shcu.trueValid
      ifu.nxt.ready    := shcu.trueReady
    }
    { //  shcu idu
      shcu.idu.invalid := idu.cur.invalid
      shcu.idu.valid   := idu.cur.valid
      shcu.idu.ready   := idu.cur.ready
      shcu.idu.work    := idu.work
      idu.pre.valid    := shcu.trueValid
      idu.nxt.ready    := shcu.trueReady
    }
    { //  shcu exu
      shcu.exu.invalid := exu.cur.invalid
      shcu.exu.valid   := exu.cur.valid
      shcu.exu.ready   := exu.cur.ready
      shcu.exu.work    := exu.work
      exu.pre.valid    := shcu.trueValid
      exu.nxt.ready    := shcu.trueReady
    }
    { //  shcu wbu
      shcu.wbu.invalid := wbu.cur.invalid
      shcu.wbu.valid   := wbu.cur.valid
      shcu.wbu.ready   := wbu.cur.ready
      shcu.wbu.work    := wbu.work
      wbu.pre.valid    := shcu.trueValid
      wbu.nxt.ready    := shcu.trueReady
    }
  }
  doShcu()

  //  FwU
  def doFwu() = {
    wbu.infoToFwu <> fwu.infoFromWbu
    fwu.infoToExu <> exu.infoFromFwu
  }
  doFwu()

  //  PcGU
  def doPcgu() = {
    ifu.infoToPcgu <> pcgu.infoFromIf
    idu.infoToPcgu <> pcgu.infoFromId
    exu.infoToPcgu <> pcgu.infoFromEx
    wbu.infoToPcgu <> pcgu.infoFromWb
    //  pcgu.npc is processed later.
  }
  doPcgu()

  //  SCU
  def doScu() = {
    exu.infoToScu <> scu.infoFromExu
    idu.infoToScu <> scu.infoFromIdu
  }
  doScu()

  //  IvCU
  def doIvcu() = {
    ifu.infoToIvcu <> ivcu.fromIfu
    idu.infoToIvcu <> ivcu.fromIdu
    exu.infoToIvcu <> ivcu.fromExu
    wbu.infoToIvcu <> ivcu.fromWbu
  }
  doIvcu()

  //  IFU
  def doIfu() = {
    ifu.pre.valid    := shcu.trueValid
    ifu.pre.invalid  := pcgu.toIf.invalid
    ifu.pre.data.npc := pcgu.toIf.npc

    ifu.nxt.ready := shcu.trueReady
  }
  doIfu()

  //  IDU
  def doIdu() = {
    idu.pre.valid   := shcu.trueValid
    idu.pre.invalid := ifu.cur.invalid
    idu.pre.data    := ifu.cur.data

    idu.nxt.ready := shcu.trueReady
  }
  doIdu()

  //  EXU
  def doExu() = {
    exu.pre.valid   := shcu.trueValid
    exu.pre.invalid := idu.cur.invalid
    exu.pre.data    := idu.cur.data

    exu.nxt.ready := shcu.trueReady

    exu.pre.sel := idu.cur.sel
  }
  doExu()

  //  WBU
  def doWbu() = {
    wbu.pre.valid   := shcu.trueValid
    wbu.pre.invalid := exu.cur.invalid
    wbu.pre.data    := exu.cur.data

    wbu.nxt.ready := shcu.trueReady
  }
  doWbu()

}
