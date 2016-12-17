package main

import jmetal.core.SolutionSet

import org.jfree.data.xy._
import org.jfree.chart.plot._
import org.jfree.chart._
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ChartUtilities

import java.io._
import java.awt.Color

import scala.collection.JavaConversions._

class Plotter {
  def plotSolutions(population: SolutionSet, plotPath: String) = {
    val series = new XYSeries("Best CPUs")
        
    population.iterator.toList.foreach{
      // OX -> CPI
      // OY -> Energy
      item => series.add(item.getObjective(0), item.getObjective(1))
    }
    
    val dataSet = new XYSeriesCollection
    dataSet.addSeries(series)
        
    val chart: JFreeChart = ChartFactory.createXYLineChart(
        "Pareto front",
        "CPI", // x
        "Energy", // y
        dataSet,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
        )
    val plt = chart.getPlot.asInstanceOf[XYPlot]
    plt.setBackgroundPaint(Color.WHITE)
    plt.setDomainGridlinePaint(Color.lightGray)
    plt.setRangeGridlinePaint(Color.lightGray)

    val renderer = plt.getRenderer.asInstanceOf[XYLineAndShapeRenderer]
    renderer.setSeriesLinesVisible(0, false)   
    renderer.setSeriesShapesVisible(0, true)
    renderer.setSeriesShapesFilled(0, true)
    
    ChartUtilities.saveChartAsPNG(new File(plotPath), chart, 800, 600)
  }
  
  def plotHypervolume(hvValues: java.util.List[java.lang.Double], plotPath: String) = {
    val series = new XYSeries("Hypervolume relative change")
    
    val maxValue = hvValues.max
        
    hvValues.zipWithIndex foreach { 
      case(value, i) => series.add(i, value/maxValue)
    }
    
    val dataSet = new XYSeriesCollection
    dataSet.addSeries(series)
        
    val chart: JFreeChart = ChartFactory.createXYLineChart(
        "Hypervolume evolution",
        "Generation number", // x
        "Hypervolume [%]", // y
        dataSet,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
        )
    val plt = chart.getPlot.asInstanceOf[XYPlot]
    plt.setBackgroundPaint(Color.WHITE)
    plt.setDomainGridlinePaint(Color.lightGray)
    plt.setRangeGridlinePaint(Color.lightGray)

    val renderer = plt.getRenderer.asInstanceOf[XYLineAndShapeRenderer]
    renderer.setSeriesLinesVisible(0, true)   
    renderer.setSeriesShapesVisible(0, true)
    renderer.setSeriesShapesFilled(0, true)
    
    ChartUtilities.saveChartAsPNG(new File(plotPath), chart, 800, 600)
  }
}