package main

import jmetal.core.SolutionSet

import org.jfree.data.xy._
import org.jfree.chart.plot._
import org.jfree.chart._
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ChartUtilities

import java.io._

import scala.collection.JavaConversions._

class Plotter {
  def plot(population: SolutionSet) = {
    val series = new XYSeries("Best CPUs")
        
    population.iterator.toList.foreach{
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
    val renderer = chart.getPlot.asInstanceOf[XYPlot].getRenderer.asInstanceOf[XYLineAndShapeRenderer]
    renderer.setSeriesLinesVisible(0, false)   
    renderer.setSeriesShapesVisible(0, true)
    renderer.setSeriesShapesFilled(0, true)
    
    ChartUtilities.saveChartAsPNG(new File("plot.png"), chart, 800, 600)
  }
}