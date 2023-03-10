package pipeline.bypass
import chisel3._
import chisel3.util._
import config.BaseConfig

class SHCU(implicit cfg:BaseConfig) extends Module {
  val ifu       = IO(Flipped(new HSInterfaceBundle))
  val idu       = IO(Flipped(new HSInterfaceBundle))
  val exu       = IO(Flipped(new HSInterfaceBundle))
  val wbu       = IO(Flipped(new HSInterfaceBundle))
  val trueReady = IO(Output(Bool()))
  val trueValid = IO(Output(Bool()))

  trueReady :=
    ((wbu.work && wbu.valid && true.B) || !wbu.work) &&
      ((exu.work && exu.valid && wbu.ready) || !exu.work) &&
      ((idu.work && idu.valid && exu.ready) || !idu.work) &&
      ((ifu.work && ifu.valid && idu.ready) || !ifu.work) &&
      ((true.B && true.B && ifu.ready) || !true.B)
  trueValid :=
    ((wbu.work && wbu.valid && true.B) || !wbu.work) &&
      ((exu.work && exu.valid && wbu.ready) || !exu.work) &&
      ((idu.work && idu.valid && exu.ready) || !idu.work) &&
      ((ifu.work && ifu.valid && idu.ready) || !ifu.work) &&
      ((true.B && true.B && ifu.ready) || !true.B)
}

class HSInterfaceBundle(implicit cfg:BaseConfig) extends Bundle {
  val invalid = Output(Bool())
  val valid   = Output(Bool())
  val ready   = Output(Bool())
  val work    = Output(Bool())
}
