circuit Top :
  module Top :
    input clock : Clock
    input reset : UInt<1>
    output io : { flip value1 : UInt<16>, flip value2 : UInt<16>, flip loadingValues : UInt<1>, outputGCD : UInt<16>, outputValid : UInt<1>}

    reg x : UInt, clock with :
      reset => (UInt<1>("h0"), x) @[Top.scala 12:14]
    reg y : UInt, clock with :
      reset => (UInt<1>("h0"), y) @[Top.scala 13:14]
    node _T = gt(x, y) @[Top.scala 15:10]
    when _T : @[Top.scala 15:15]
      node _x_T = sub(x, y) @[Top.scala 16:12]
      node _x_T_1 = tail(_x_T, 1) @[Top.scala 16:12]
      x <= _x_T_1 @[Top.scala 16:7]
    else :
      node _y_T = sub(y, x) @[Top.scala 18:12]
      node _y_T_1 = tail(_y_T, 1) @[Top.scala 18:12]
      y <= _y_T_1 @[Top.scala 18:7]
    when io.loadingValues : @[Top.scala 21:26]
      x <= io.value1 @[Top.scala 22:7]
      y <= io.value2 @[Top.scala 23:7]
    io.outputGCD <= UInt<1>("h0") @[Top.scala 26:18]
    io.outputGCD <= x @[Top.scala 27:18]
    node _io_outputValid_T = eq(y, UInt<1>("h0")) @[Top.scala 28:23]
    io.outputValid <= _io_outputValid_T @[Top.scala 28:18]

