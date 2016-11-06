package simulator

import java.nio.file.Paths

import scala.util.Random
import scala.xml.XML

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
    private var _superscalar: Int,
    private var _rename: Int,
    private var _reorder: Int, 
    private var _rsb_architecture: RsbArchitecture,
    private var _separate_dispatch: Boolean,
    private var _vdd: Double,
    private var _freq: Double,
    private var _integerFu: Int,
    private var _floatingFu: Int,
    private var _branchFu: Int,
    private var _memoryFu: Int) {
  
  val input = name + ".xml"
  val output = name + "_out.xml"
  
  private val traces = List("applu.tra", "compress.tra", "epic.tra", "fpppp.tra", "ijpeg.tra", "mpeg2d.tra", "mpeg2e.tra", "pegwitd.tra", "perl.tra", "toast.tra")
  private val minVdd = 1.8 // in Volts
  private val maxVdd = 3.3
  private val minFreq = 10 // in Mhz
  private val maxFreq = 5000
  
  def save(path: String) = {
    XML.save(Paths.get(path, name + ".xml").toString, getXml)
  }
  
  def validate: Boolean = {
    return true
    //TODO: write this
  }
  
  def randomize() = {
    var r = new Random()
    
    superscalar = r.nextInt(16) + 1
    rename = r.nextInt(512) + 1
    reorder = r.nextInt(512) + 1
    rsb_architecture = RsbArchitecture(r.nextInt(4))
    separate_dispatch = r.nextBoolean()
    vdd = minVdd + (maxVdd - minVdd) * r.nextDouble()
    freq = minFreq + (maxFreq - minFreq) * r.nextDouble()
    integerFu = r.nextInt(8) + 1
    floatingFu = r.nextInt(8) + 1
    branchFu = r.nextInt(8) + 1
    memoryFu = r.nextInt(8) + 1
  }
  
  // getter and setter for the superscalar parameter
  def superscalar = _superscalar
  def superscalar_= (value: Int) = value match {
    case value if 1 to 16 contains value => _superscalar = value
    case _ => throw new IllegalArgumentException(value + " is not a valid superscalar value!")
  }
  
  // getter and setter for the rename parameter
  def rename = _rename
  def rename_= (value: Int) = value match {
    case retval if 1 to 512 contains value => _rename = retval
    case _ => throw new IllegalArgumentException(value + " is not a valid rename value!")
  }

  // getter and setter for the reorder parameter
  def reorder = _reorder
  def reorder_= (value: Int) = value match {
    case retval if 1 to 512 contains value => _reorder = retval
    case _ => throw new IllegalArgumentException(value + " is not a valid reorder value!")
  }
  
  // getter and setter for the rsb_architecture parameter
  def rsb_architecture = _rsb_architecture
  def rsb_architecture_= (value: RsbArchitecture) = _rsb_architecture = value
  
  // getter and setter for the separate_dispatch parameter
  def separate_dispatch = _separate_dispatch
  def separate_dispatch_= (value: Boolean) = _separate_dispatch = value
  
  // getter and setter for the vdd parameter
  def vdd = _separate_dispatch
  def vdd_= (value: Double) = value match {
    case retval if (value >= minVdd && value <= maxVdd) => _vdd = retval
    case _ => throw new IllegalArgumentException(value + " is not a valid vdd value!")
  }
  
  // getter and setter for the freq parameter
  def freq = _freq
  def freq_= (value: Double) = value match {
    case retval if (value >= minFreq && value <= maxFreq) => _freq = retval
    case _ => throw new IllegalArgumentException(value + " is not a valid freq value!")
  }
  
  // getter and setter for the integerFu parameter
  def integerFu = _integerFu
  def integerFu_= (value: Int) = value match {
    case retval if 1 to 8 contains value => _integerFu = retval
    case _ => throw new IllegalArgumentException(value + " is not a valid integerFu value!")
  }
  
  // getter and setter for the floatingFu parameter
  def floatingFu = _floatingFu
  def floatingFu_= (value: Int) = value match {
    case retval if 1 to 8 contains value => _floatingFu = retval
    case _ => throw new IllegalArgumentException(value + " is not a valid floatingFu value!")
  }
  
  // getter and setter for the branchFu parameter
  def branchFu = _branchFu
  def branchFu_= (value: Int) = value match {
    case retval if 1 to 8 contains value => _branchFu = retval
    case _ => throw new IllegalArgumentException(value + " is not a valid branchFu value!")
  }
  
  // getter and setter for the memoryFu parameter
  def memoryFu = _memoryFu
  def memoryFu_= (value: Int) = value match {
    case retval if 1 to 8 contains value => _memoryFu = retval
    case _ => throw new IllegalArgumentException(value + " is not a valid memoryFu value!")
  }
  
  private def getXml = {
    def generalNode(trace: String) =  
      <general							
					superscalar={_superscalar.toString}
					rename={_rename.toString}
					reorder={_reorder.toString}
					rsb_architecture={_rsb_architecture.toString}
					separate_dispatch={_separate_dispatch.toString}
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