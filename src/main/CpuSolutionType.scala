package main

import jmetal.core.SolutionType 
import jmetal.core.Problem
import jmetal.core.Variable
import jmetal.encodings.variable._
import jmetal.encodings.solutionType._

class CpuSolutionType(problem: Problem) extends RealSolutionType(problem) {
  
  override def createVariables(): Array[Variable] = {
    var variables = new Array[Variable](11)
    variables(0) = new Int(1, 16) // superscalar
    variables(1) = new Int(1, 512) // rename
    variables(2) = new Int(1, 512) // reorder
    variables(3) = new Int(1, 2) // RSB
    variables(4) = new Int(0, 1) // separate dispatch
    variables(5) = new Real(1.8, 3.3) // vdd
    variables(6) = new Int(10, 5000) // freq
    variables(7) = new Int(1, 8) // interger FU
    variables(8) = new Int(1, 8) // floating FU
    variables(9) = new Int(1, 8) // branch FU
    variables(10) = new Int(1, 8) // memory FU
    
    return variables
  }
}