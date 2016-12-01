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
    var opts = Map(
        'swarmSize -> 1, // number of particles
        'archiveSize -> 100, // max. number of non-dominated particles
        'maxIterations -> 0,
        'psatsimPath -> "E:/Facultate/SOAC/psatsim/PSATSim",
        'psatsimName -> "psatsim_con.exe")
    opts = ArgParser.getOptions(opts, args.toList)
            
    val now = Calendar.getInstance.getTime
    val dateFmt = new SimpleDateFormat("YYYY-MM-dd HH.mm.ss")
    val dirPath = dateFmt.format(now).toString
    
    println(dirPath)
    var funPath = Paths.get(dirPath, "FUN").toString
    var varPath = Paths.get(dirPath, "VAR").toString
    var plotPath = Paths.get(dirPath, "plot.png").toString
    var logPath = Paths.get(dirPath, "SOMPSO.log").toString
    
    //save every run in a different directory
    val dir = new File(dirPath)
    if(!dir.mkdir())
    {
      funPath = "FUN"
      varPath = "VAR"
      plotPath = "plot.png"
      logPath = "SOMPSO.log"
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
    
    val indicators : QualityIndicator = null
    // Logger object and file to store log messages
    val logger_      = Configuration.logger_
    val fileHandler_ = new FileHandler(logPath) 
    logger_.addHandler(fileHandler_)
    
    logger_.info(opts.toString)
    
    var problem = new CpuProblem(opts('psatsimPath).toString, opts('psatsimName).toString) 
    var algorithm = new SMPSO(problem)
    
    // Algorithm parameters
    algorithm.setInputParameter("swarmSize", opts('swarmSize))
    algorithm.setInputParameter("archiveSize", opts('archiveSize))
    algorithm.setInputParameter("maxIterations", opts('maxIterations))

    val parameters = new HashMap[String, Double]
    parameters.put("probability", 1.0/problem.getNumberOfVariables)
    parameters.put("distributionIndex", 20.0)
    val mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters)                    

    algorithm.addOperator("mutation", mutation)
    
    // Execute the Algorithm 
    val initTime = System.currentTimeMillis
    val population = algorithm.execute
    val estimatedTime = System.currentTimeMillis - initTime   
    
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
         
    logger_.info("Evaluated %d configurations for %d iterations in %d seconds (10 benchmarks)".format(
        opts('swarmSize), 
        opts('maxIterations),
        estimatedTime/1000))
    logger_.info("Objectives values have been writen to file %s".format(funPath))
    logger_.info("Variables values have been writen to file %s".format(varPath))
    logger_.info("The plot has been writen to file %s".format(plotPath))
    
    for(i <- 0 until population.size)
    {
      val solution = population.get(i)
      println("Solution %d, CPI = %1.3f, Energy = %1.3f".format(i+1, solution.getObjective(0), solution.getObjective(1)))
    }
  }
}