package pkg

//import pkg.PSATSim.RsbArchitecture
//import pkg.CpuCfg

object App {
  def main(args: Array[String]): Unit = {
    val psatsimPath = "E:/Facultate/SOAC/psatsim/PSATSim"
    val sim = new PSATSim(psatsimPath)
    val cpu1 = new CpuCfg(
        "config1", 1, 2, 3,
        RsbArchitecture.centralized, true,
        2.2, 600,
        2, 1, 1, 1)  
    
//		val printer = new scala.xml.PrettyPrinter(80, 2)
//		println(printer.format(cpu1 getXml))
    
    cpu1.saveXml(psatsimPath)
		println(sim.run(cpu1))    
  }
  
}