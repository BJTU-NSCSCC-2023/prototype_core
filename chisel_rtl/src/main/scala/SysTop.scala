import config.Config
import chisel3._
import chisel3.util._
import memory._
import stages._

class SysTop(implicit config: Config) extends Module {
  val ifStage        = Module(new IfStage)
  val idStage        = Module(new IdStage)
  val cuStage        = Module(new CuStage)
  val memAccessProxy = Module(new MemAccessProxy)
  cuStage.io.toIf      <> ifStage.io.fromCu
  ifStage.io.toId      <> idStage.io.fromIf
  ifStage.io.toInstMem <> memAccessProxy.instMemAccessProxy
}
