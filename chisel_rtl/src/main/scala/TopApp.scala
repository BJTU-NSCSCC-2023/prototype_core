import config.BaseConfig
import pipeline.SysTop
object TopApp extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new SysTop(), args)
}

object TesterApp extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new TesterChip(), args)
}