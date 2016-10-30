package pkg

import sys.process._
import java.nio.file.Paths
import scala.xml.XML

class PSATSim(val simPath: String){  
  val NumThreads = 5
  val SimName = "psatsim_con.exe"
  
  def run(cpu: CpuCfg) = {
    val cmd = Seq(
        Paths.get(simPath, SimName).toString,
        cpu.input,
        cpu.output,
        "-t", NumThreads.toString,
        "-cg")
    
    println("Running PSATSim from command line: " + cmd)
    val x = Process(cmd, new java.io.File(simPath)).!!
    println(x)
    
    readResults(cpu.output)
  }
  
  private def readResults(outFile: String) = {
    val resultsXml = XML.loadFile(Paths.get(simPath, outFile).toString)
    val variations = resultsXml \\ "psatsim_results" \\ "variation"
    
    // take every variation's energy and convert it to double
    val energies =  variations map (variation => variation \ "general" \@ "energy") map(_.toDouble)
    
    // CPI = 1/IPC
    val ipcs = variations map (variation => variation \ "general" \@ "ipc") map(1/_.toDouble)
       
    // I compute the average CPI and Energy 
    (ipcs.sum / ipcs.length, energies.sum / energies.length)
  }
}