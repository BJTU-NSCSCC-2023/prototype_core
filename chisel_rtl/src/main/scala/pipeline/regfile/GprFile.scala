package pipeline.regfile
import chisel3._
import chisel3.util._
import config.BaseConfig

class GprFile(implicit cfg:BaseConfig) extends Module {
  val wr = IO(new Bundle {
    val en    = Input(Bool())
    val id    = Input(UInt(cfg.xidLen.W))
    val value = Input(UInt(cfg.xLen.W))
  })
  val rdL = IO(new Bundle {
    val en    = Input(Bool())
    val id    = Input(UInt(cfg.xidLen.W))
    val value = Output(UInt(cfg.xLen.W))
  })
  val rdR = IO(new Bundle {
    val en    = Input(Bool())
    val id    = Input(UInt(cfg.xidLen.W))
    val value = Output(UInt(cfg.xLen.W))
  })
  val wrId    = Mux(wr.en, wr.id, 0.U)
  val wrValue = Mux(wrId.orR, wr.value, 0.U)
  val file    = RegInit(VecInit(Seq.fill(cfg.gprCnt)(0.U(cfg.xLen.W))))
  //  file(rdL.id)
  val rdLValueFromFile =
    VecInit(
      (0 until cfg.gprCnt).map(i => Mux(i.U === rdL.id, file(i), 0.U))
    ).reduceTree((l, r) => l | r)
  val rdRValueFromFile =
    VecInit(
      (0 until cfg.gprCnt).map(i => Mux(i.U === rdR.id, file(i), 0.U))
    ).reduceTree((l, r) => l | r)
  rdL.value := Mux(
    rdL.en,
    Mux(rdL.id === wr.id, wr.value, rdLValueFromFile),
    cfg.magicVal.U
  )
  rdR.value := Mux(
    rdR.en,
    Mux(rdR.id === wr.id, wr.value, rdRValueFromFile),
    cfg.magicVal.U
  )
  for (i <- 0 until cfg.gprCnt) {
    file(i) := Mux(i.U === wrId, wrValue, file(i))
  }
}
