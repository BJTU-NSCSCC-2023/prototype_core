package config
import chisel3._
import chisel3.util._

class BaseConfig {
  val xLen     = 32
  val laLen    = 32
  val magicLa  = 0x3f3f3f3f
  val gprCnt   = 32
//  val gprCnt = 4
  val xidLen   = 5
  val magicVal = 0x01010101
}
