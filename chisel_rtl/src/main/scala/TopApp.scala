import config.Config
object TopApp extends App {
  implicit val config = new Config()
  (new chisel3.stage.ChiselStage).emitVerilog(new SysTop(), args)
}
