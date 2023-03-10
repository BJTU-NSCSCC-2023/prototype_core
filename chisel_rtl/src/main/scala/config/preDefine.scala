package config
import chisel3._
import chisel3.util._

object preDefine {
  val instLen  = 32
  object cp0 {
    val regCnt   = 32 //  TODO: Not decided
    val regIdLen = log2Up(regCnt)
    val mxLen    = 32 // TODO: Not decided
  }

  object eh { //  exception handle, priority: high->low (see doc to get priority levels)
    val integerOverflowException = 0
    val trapException            = integerOverflowException + 1
    val syscallException         = trapException + 1
    val _len                     = syscallException + 1
    //    val breakpointException =
  }
  object ih { //  interrupt handle
    val timer = 0
    val _len  = timer + 1
  }

  object id { //  inst decode
    object sop {
      val _bg                                              = 0
      val add :: addi :: addu :: addiu :: Nil              = (_bg + 0 until _bg + 4).toList
      val sub :: subu :: Nil                               = (_bg + 4 until _bg + 6).toList
      val slt :: slti :: sltu :: sltiu :: Nil              = (_bg + 6 until _bg + 10).toList
      val clo :: clz :: Nil                                = (_bg + 10 until _bg + 12).toList
      val and :: andi :: or :: ori :: Nil                  = (_bg + 12 until _bg + 16).toList
      val nor :: xor :: xori :: Nil                        = (_bg + 16 until _bg + 19).toList
      val sll :: sllv :: sra :: srav :: srl :: srlv :: Nil = (_bg + 19 until _bg + 25).toList
      val lui :: mfhi :: mflo :: mthi :: mtlo :: Nil       = (_bg + 25 until _bg + 30).toList
      val _ed                                              = _bg + 30
    }
    object mul {
      val _bg                 = sop._ed
      val mul :: multu :: Nil = (_bg + 0 until _bg + 2).toList
      val _ed                 = _bg + 2
    }
    object div {
      val _bg                = mul._ed
      val div :: divu :: Nil = (_bg + 0 until _bg + 2).toList
      val _ed                = _bg + 2
    }
    object ma {
      val _bg                                 = div._ed
      val lb :: lbu :: lh :: lhu :: lw :: Nil = (_bg + 0 until _bg + 5).toList
      val sb :: sh :: sw :: ll :: sc :: Nil   = (_bg + 5 until _bg + 10).toList
      val _ed                                 = _bg + 10
    }
    object nop {
      val _bg                                             = ma._ed
      val break :: syscall :: Nil                         = (_bg + 0 until _bg + 2).toList
      val teq :: tge :: tgeu :: tlt :: tltu :: tne :: Nil = (_bg + 2 until _bg + 8).toList
      val eret :: branch :: Nil                           = (_bg + 8 until _bg + 10).toList
      val _ed                                             = _bg + 10
    }
    object cp0op {
      val _bg                 = nop._ed
      val mtc0 :: mfc0 :: Nil = (_bg + 0 until _bg + 2).toList
      val _ed                 = _bg + 2
    }
    object tlbOp {
      val _bg                                   = cp0op._ed
      val tlbr :: tlbwi :: tlbwr :: tlbp :: Nil = (_bg + 0 until _bg + 4).toList
      val _ed                                   = _bg + 4
    }
    val _len = tlbOp._ed
  }
  object exSel {
    val _len                                                   = 6
    val sSop :: sMul :: sMa :: sNop :: sCp0Op :: sTlbOp :: Nil = (0 until _len).toList
  }

}
