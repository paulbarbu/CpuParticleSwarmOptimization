import scala.xml.XML
import java.nio.file.Paths

/** Create and configure a CPU configuration to be simulated */
object RsbArchitecture extends Enumeration
{
  type RsbArchitecture = Value
  val centralized, hybrid, distributed = Value
}

import RsbArchitecture._

class CpuCfg(
    val name: String,
    var superscalar: Int,
    var rename: Int,
    var reorder: Int, 
    var rsb_architecture: RsbArchitecture,
    var separate_dispatch: Boolean,
    var trace: String,
    var vdd: Double,
    var freq: Int,
    var integerFu: Int,
    var floatingFu: Int,
    var branchFu: Int,
    var memoryFu: Int){
  
  val input = name + ".xml"
  val output = name + "_out.xml"
  
  def saveXml(root: String) {
    XML.save(Paths.get(root, name + ".xml").toString, getXml)  
  }
  
  private def getXml = 
    <psatsim>
			<config name={name}>
				<general							
					superscalar={superscalar.toString}
					rename={rename.toString}
					reorder={reorder.toString}
					rsb_architecture={rsb_architecture.toString}
					separate_dispatch={separate_dispatch.toString}
					seed="0"
					trace={trace}
					output={output}
					vdd={vdd.toString}
					freq={freq.toString}
				/>
				<execution
					architecture="standard"
					integer={integerFu.toString}
					floating={floatingFu.toString}
					branch={branchFu.toString}
					memory={memoryFu.toString}
				/>
				<memory architecture="l2">
					<l1_code hitrate="0.990" latency="1" />
					<l1_data hitrate="0.970" latency="1" />
					<l2 hitrate="0.990" latency="3" />
					<system latency="20" />
				</memory>
		  </config>
    </psatsim>
  
}