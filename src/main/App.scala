package main 

import simulator.PSATSim
import simulator.PSATSimCfg
import simulator.RsbArchitecture

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.smpso.SMPSO;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.problems.ProblemFactory;
import jmetal.problems.ZDT.ZDT4;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

object App {
  def main(args: Array[String]): Unit = {   
    var indicators : QualityIndicator = null
    // Logger object and file to store log messages
    var logger_      = Configuration.logger_ ;
    var fileHandler_ = new FileHandler("SMPSO_main.log"); 
    logger_.addHandler(fileHandler_) ;
    
    var problem = new CpuProblem()     
    var algorithm = new SMPSO(problem)
    
    // Algorithm parameters
    algorithm.setInputParameter("swarmSize", 1)
    algorithm.setInputParameter("archiveSize", 100)
    algorithm.setInputParameter("maxIterations", 0)

    val parameters = new HashMap[String, Double]()
    parameters.put("probability", 1.0/problem.getNumberOfVariables())
    parameters.put("distributionIndex", 20.0)
    var mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters)                    

    algorithm.addOperator("mutation", mutation)
    
    // Execute the Algorithm 
    var initTime = System.currentTimeMillis()
    var population = algorithm.execute()
    var estimatedTime = System.currentTimeMillis() - initTime
    
    // Result messages 
    logger_.info("Total execution time: " + estimatedTime/1000 + " s")
    logger_.info("Objectives values have been writen to file FUN")
    population.printObjectivesToFile("FUN")
    logger_.info("Variables values have been writen to file VAR")
    population.printVariablesToFile("VAR")
    
    if (indicators != null) {
      logger_.info("Quality indicators")
      logger_.info("Hypervolume: " + indicators.getHypervolume(population))
      logger_.info("GD         : " + indicators.getGD(population))
      logger_.info("IGD        : " + indicators.getIGD(population))
      logger_.info("Spread     : " + indicators.getSpread(population))
      logger_.info("Epsilon    : " + indicators.getEpsilon(population))
    }
    
    
    for(i <- 0 until population.size())
    {
      var solution = population.get(i)
      println("Solution %d, CPI = %1.3f, Energy = %1.3f".format(i, solution.getObjective(0), solution.getObjective(1)))
    }
  }
  
}