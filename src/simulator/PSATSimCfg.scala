package simulator

import scala.xml.XML
import java.nio.file.Paths

object RsbArchitecture extends Enumeration
{
  type RsbArchitecture = Value
  val centralized, hybrid, distributed = Value
}

import RsbArchitecture._

/**
 * Create and configure a CPU configuration to be simulated
 * 
 * One configuration will be simulated on all the traces
 */
class PSATSimCfg(
    val name: String,
    var superscalar: Int,
    var rename: Int,
    var reorder: Int, 
    var rsb_architecture: RsbArchitecture,
    var separate_dispatch: Boolean,
    var vdd: Double,
    var freq: Int,
    var integerFu: Int,
    var floatingFu: Int,
    var branchFu: Int,
    var memoryFu: Int) {
  
  val input = name + ".xml"
  val output = name + "_out.xml"
  val traces = List("applu.tra", "compress.tra", "epic.tra", "fpppp.tra", "ijpeg.tra", "mpeg2d.tra", "mpeg2e.tra", "pegwitd.tra", "perl.tra", "toast.tra")
  
  def save(path: String) = {
    XML.save(Paths.get(path, name + ".xml").toString, getXml)
  }
  
  def validate: Boolean = {
    return true
  }
  
  def randomize() = {
    
  }
  
  private def getXml = {
    def generalNode(trace: String) =  
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
				/>;
    
		// this XML will contain 10 general nodes, one for every trace file
    <psatsim>
			<config name={name}>
				{traces map(t => generalNode(t))}
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
}