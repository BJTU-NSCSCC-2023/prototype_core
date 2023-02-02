package stages
import chisel3._
import chisel3.util._
import config.Config

class If2IdBundle(implicit config: Config) extends Bundle {
  val valid     = Output(Bool())
  val instBits = Output(UInt(config.wordWid.W))
  val thisPc    = Output(UInt(config.virAddrWid.W))
  val ready     = Input(Bool())
}

class IdStage(implicit config: Config) extends Module {
  val io = IO(new Bundle {
    val fromIf = Flipped(new If2IdBundle)
  })
}
