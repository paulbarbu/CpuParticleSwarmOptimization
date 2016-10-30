package main

import simulator.PSATSim
import simulator.RsbArchitecture
import simulator.PSATSimCfg
import java.nio.file.Paths

object App {
  def main(args: Array[String]): Unit = {
    //TODO: Runner (runs a simulator cfg), PSatSIM (validate cfg, ie l2 >l1, etc), PsatSimCfg (randomize, setRob(val or -val))
    val psatsimPath = "E:/Facultate/SOAC/psatsim/PSATSim"
    val psatsimName = "psatsim_con.exe"
    val sim = new PSATSim(psatsimPath)
    val cpu1 = new PSATSimCfg(
        "config1", 1, 2, 3,
        RsbArchitecture.centralized, true,
        2.2, 600,
        2, 1, 1, 1)  
    
//		val printer = new scala.xml.PrettyPrinter(80, 2)
//		println(printer.format(cpu1 getXml))
    
    cpu1.save(psatsimPath)
    sim.run(cpu1, psatsimPath, psatsimName)
		println(sim.readResults(cpu1.output))    
  }
  
}