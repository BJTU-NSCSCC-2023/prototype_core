package pipeline.regfile
import chisel3._
import chisel3.util._
import config.{BaseConfig, preDefine => pdf}

class Cp0File extends Module {
  val wen  = IO(Input(Bool()))
  val id   = IO(Input(UInt(pdf.cp0.regIdLen.W)))
  val rVal = IO(Output(UInt(pdf.cp0.mxLen.W)))
  val wVal = IO(Input(UInt(pdf.cp0.mxLen.W)))

  // TODO: CP0 will be completed after os test. 


}
