package simulator

import java.nio.file.Paths

import scala.sys.process._
import scala.xml.XML

class PSATSim(val simPath: String) {
  val NumThreads = 4
  
  def run(cpu: PSATSimCfg, simPath: String, simName: String, simArgs: Seq[String] = Seq()) = {
    val cmd = Seq(
        Paths.get(simPath, simName).toString,
        cpu.input,
        cpu.output,
        "-t", NumThreads.toString,
        "-cg") ++ simArgs
    
    println("Running PSATSim from command line: " + cmd)
    val x = Process(cmd, new java.io.File(simPath)).!!
    println(x)
  }
  
  def readResults(outFile: String) = {
    val resultsXml = XML.loadFile(Paths.get(simPath, outFile).toString)
    val variations = resultsXml \\ "psatsim_results" \\ "variation"
    
    // take every variation's energy and convert it to double
    val energies =  variations map (variation => variation \ "general" \@ "energy") map(_.toDouble)
    
    // CPI = 1/IPC
    val cpis = variations map (variation => variation \ "general" \@ "ipc") map(1/_.toDouble)
       
    // I compute the average CPI and Energy 
    (cpis.sum / cpis.length, energies.sum / energies.length)
  }
}