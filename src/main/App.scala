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
import jmetal.qualityIndicator.Hypervolume
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

import scala.collection.JavaConversions._

object App {
  def main(args: Array[String]): Unit = {        
    var opts = Map(
        'swarmSize -> 2, // number of particles
        'archiveSize -> 100, // max. number of non-dominated particles
        'maxIterations -> 2,
        'epsilon -> 0.0001,
        'psatsimPath -> "E:/localhost/CpuParticleSwarmOptimization/psatsim/PSATSim",
        'psatsimName -> "psatsim_con.exe")
    opts = ArgParser.getOptions(opts, args.toList)
            
    val now = Calendar.getInstance.getTime
    val dateFmt = new SimpleDateFormat("YYYY-MM-dd HH.mm.ss")
    val dirPath = dateFmt.format(now).toString
    
    println(dirPath)
    var funPath = Paths.get(dirPath, "FUN").toString
    var varPath = Paths.get(dirPath, "VAR").toString
    var plotSolutionsPath = Paths.get(dirPath, "plot-solutions.png").toString
    var plotHypervolumePath = Paths.get(dirPath, "plot-hv.png").toString
    var logPath = Paths.get(dirPath, "SOMPSO.log").toString
    
    //save every run in a different directory
    val dir = new File(dirPath)
    if(!dir.mkdir())
    {
      funPath = "FUN"
      varPath = "VAR"
      plotSolutionsPath = "plot-solutions.png"
      plotHypervolumePath = "plot-hv.png"
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
    
    // Logger object and file to store log messages
    val logger_      = Configuration.logger_
    val fileHandler_ = new FileHandler(logPath) 
    logger_.addHandler(fileHandler_)
    
    logger_.info(opts.toString)
    
    var problem = new CpuProblem(opts('psatsimPath).toString, opts('psatsimName).toString) 
    var algorithm = new SMPSOpaul(problem)
    
    // Algorithm parameters
    algorithm.setInputParameter("swarmSize", opts('swarmSize))
    algorithm.setInputParameter("archiveSize", opts('archiveSize))
    algorithm.setInputParameter("maxIterations", opts('maxIterations))
    algorithm.setInputParameter("epsilon", opts('epsilon))

    val parameters = new HashMap[String, Double]
    parameters.put("probability", 1.0/problem.getNumberOfVariables)
    parameters.put("distributionIndex", 20.0)
    val mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters)                    

    algorithm.addOperator("mutation", mutation)
    
    // Execute the Algorithm 
    val initTime = System.currentTimeMillis
    val population = algorithm.execute
    val estimatedTime = System.currentTimeMillis - initTime   
    
    val plotter = new Plotter
    
    plotter.plotSolutions(population, plotSolutionsPath)
    plotter.plotHypervolume(algorithm.getHv, plotHypervolumePath)
    population.printObjectivesToFile(funPath)
    population.printVariablesToFile(varPath)
         
    logger_.info("Evaluated %d configurations for %d iterations in %d seconds (10 benchmarks)".format(
        opts('swarmSize), 
        opts('maxIterations),
        estimatedTime/1000))
    logger_.info("Objectives values have been writen to file %s".format(funPath))
    logger_.info("Variables values have been writen to file %s".format(varPath))
    logger_.info("The solutions plot has been writen to file %s".format(plotSolutionsPath))
    logger_.info("The hypervolume plot has been writen to file %s".format(plotHypervolumePath))
    logger_.info("The hypervolume values are: %s".format(algorithm.getHv))
    if(algorithm.isConvergent)
    {
      logger_.info("Stopped because of converging solutions")
    }
    else
    {
      logger_.info("Stopped because of max. iterations")
    }
    
    for(i <- 0 until population.size)
    {
      val solution = population.get(i)
      println("Solution %d, CPI = %1.3f, Energy = %1.3f".format(i+1, solution.getObjective(0), solution.getObjective(1)))
    }
  }
}

