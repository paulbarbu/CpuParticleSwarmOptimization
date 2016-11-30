package main

import jmetal.core.Problem
import jmetal.core.Solution
import jmetal.encodings.variable._
import simulator._
import jmetal.util.comparators.NumberOfViolatedConstraintComparator

class CpuProblem extends Problem {
  numberOfVariables_ = 11
  numberOfObjectives_ = 2
  numberOfConstraints_ = 0
  problemName_ = "CpuProblem"
  
  solutionType_ = new CpuSolutionType(this)
  
  var variables = solutionType_.createVariables
    
  lowerLimit_ = new Array[Double](11);
  upperLimit_ = new Array[Double](11);
    
  for (i <- 0 until numberOfVariables_){
    lowerLimit_(i) = variables(i).getLowerBound
    upperLimit_(i) = variables(i).getUpperBound
  }
  
  def evaluate(solution: Solution) = {
    val psatsimPath = "E:/Facultate/SOAC/psatsim/PSATSim"
    val psatsimName = "psatsim_con.exe"
    val sim = new PSATSim(psatsimPath)
    
    val variables = solution.getDecisionVariables
        
    val cpu = new PSATSimCfg(
        "config1", variables(0).getValue.toInt, variables(1).getValue.toInt, variables(2).getValue.toInt,
        RsbArchitecture(variables(3).getValue.toInt), if (variables(4).getValue.toInt == 1) true else false,
        variables(5).getValue, variables(6).getValue.toInt,
        variables(7).getValue.toInt, variables(8).getValue.toInt, variables(9).getValue.toInt, variables(10).getValue.toInt)
    
    cpu.save(psatsimPath)
    sim.run(cpu, psatsimPath, psatsimName)
		val (cpi, energy) = sim.readResults(cpu.output)
		
		solution.setObjective(0, cpi)
		solution.setObjective(1, energy)
  }
}