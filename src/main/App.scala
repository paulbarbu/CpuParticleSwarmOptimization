package main 

import simulator.PSATSim
import simulator.PSATSimCfg
import simulator.RsbArchitecture

import jmetal.core.Algorithm
import jmetal.core.Problem
import jmetal.core.SolutionSet
import jmetal.metaheuristics.smpso.SMPSO
import jmetal.operators.mutation.Mutation
import jmetal.operators.mutation.MutationFactory
import jmetal.problems.ProblemFactory
import jmetal.problems.ZDT.ZDT4
import jmetal.qualityIndicator.QualityIndicator
import jmetal.util.Configuration
import jmetal.util.JMException

import java.io.IOException
import java.util.HashMap
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.Calendar
import java.text.SimpleDateFormat
import java.nio.file.Paths
import java.io.File

object App {
  def main(args: Array[String]): Unit = {
    val now = Calendar.getInstance.getTime
    val dateFmt = new SimpleDateFormat("YYYY-MM-dd HH.mm.ss")
    val dirPath = dateFmt.format(now).toString
    
    println(dirPath)
    var funPath = Paths.get(dirPath, "FUN").toString
    var varPath = Paths.get(dirPath, "VAR").toString
    var plotPath = Paths.get(dirPath, "plot.png").toString
    
    //save every run in a different directory
    val dir = new File(dirPath)
    if(!dir.mkdir())
    {
      funPath = "FUN"
      varPath = "VAR"
      plotPath = "plot.png"
    }
    
//    val psatsimPath = "E:/Facultate/SOAC/psatsim/PSATSim"
//    val psatsimName = "psatsim_con.exe"
//    val sim = new PSATSim(psatsimPath)
//    
//    val cpu = new PSATSimCfg(
//        "config1", 1, 2, 3,
//        RsbArchitecture(0), false,
//        1.1, 600,
//        7, 8, 9, 10)
//    
//    //scpu.save(psatsimPath)
//    //sim.run(cpu, psatsimPath, psatsimName)
//		val (cpi, energy) = sim.readResults(cpu.output)
//    println(cpi, energy)
//    return 1
    
    var indicators : QualityIndicator = null
    // Logger object and file to store log messages
    var logger_      = Configuration.logger_
    var fileHandler_ = new FileHandler("SMPSO_main.log") 
    logger_.addHandler(fileHandler_)
    
    var problem = new CpuProblem 
    var algorithm = new SMPSO(problem)
    
    // Algorithm parameters
    algorithm.setInputParameter("swarmSize", 1)
    algorithm.setInputParameter("archiveSize", 100)
    algorithm.setInputParameter("maxIterations", 0)

    val parameters = new HashMap[String, Double]
    parameters.put("probability", 1.0/problem.getNumberOfVariables)
    parameters.put("distributionIndex", 20.0)
    var mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters)                    

    algorithm.addOperator("mutation", mutation)
    
    // Execute the Algorithm 
    var initTime = System.currentTimeMillis
    var population = algorithm.execute
    var estimatedTime = System.currentTimeMillis - initTime   
    
    if (indicators != null) {
      logger_.info("Quality indicators")
      logger_.info("Hypervolume: " + indicators.getHypervolume(population))
      logger_.info("GD         : " + indicators.getGD(population))
      logger_.info("IGD        : " + indicators.getIGD(population))
      logger_.info("Spread     : " + indicators.getSpread(population))
      logger_.info("Epsilon    : " + indicators.getEpsilon(population))
    }
    
    val plotter = new Plotter
    
    plotter.plot(population, plotPath)
    population.printObjectivesToFile(funPath)
    population.printVariablesToFile(varPath)
         
    for(i <- 0 until population.size)
    {
      var solution = population.get(i)
      println("Solution %d, CPI = %1.3f, Energy = %1.3f".format(i+1, solution.getObjective(0), solution.getObjective(1)))
    }
    
    logger_.info("Total execution time: " + estimatedTime/1000 + " s")
    logger_.info("Objectives values have been writen to file %s".format(funPath))
    logger_.info("Variables values have been writen to file %s".format(varPath))
    logger_.info("The plot has been writen to file %s".format(plotPath))

  }
}