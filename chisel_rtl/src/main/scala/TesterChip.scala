import chisel3._
import chisel3.util._
import config.{BaseConfig, preDefine => pdf}

class A extends Module {
  val i = IO(new Bundle {
    val x = Input(UInt(2.W))
    val y = Input(UInt(4.W))
  })
  val o = IO(new Bundle{
    val z = Output(UInt(6.W))
  })
  o.z := Cat(i.x, i.y)
}

class B extends BlackBox {
  val io = IO(new Bundle {
    val y = Output(UInt(2.W))
    val x = Output(UInt(4.W))
  })
}

class TesterChip extends Module {
  val o = IO(new Bundle {
    val z = Output(UInt(6.W))
  })
  val a = Module(new A)
  val b = Module(new B)
  a.i <> b.io
  o.z := a.o.z
}
