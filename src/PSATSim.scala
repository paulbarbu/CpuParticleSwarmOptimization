import sys.process._
import java.nio.file.Paths
import scala.xml.XML

class PSATSim(val simPath: String){
  val simName = "psatsim_con.exe"
  
  def run(cpu: CpuCfg) = {
    val cmd = Seq(
        Paths.get(simPath, simName).toString,
        cpu.input,
        cpu.output,
        "-cg")
    
    println("Running PSATSim from command line: " + cmd)
    val x = Process(cmd, new java.io.File(simPath)).!!
    println(x)
    
    readResults(cpu.output)
  }
  
  private def readResults(outFile: String) = {
    val resultsXml = XML.loadFile(Paths.get(simPath, outFile).toString)
    val energy = resultsXml \\ "psatsim_results" \\ "variation" \ "general" \@ "energy"
    val ipc = resultsXml \\ "psatsim_results" \\ "variation" \ "general" \@ "ipc"
       
    // I compute CPI and Energy 
    (1/ipc.toDouble, energy.toDouble)
  }
}