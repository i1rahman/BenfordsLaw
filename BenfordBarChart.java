import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 

import org.jfree.chart.labels.ItemLabelAnchor; 
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator; 
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.TextAnchor;

/**
 * Date: May 12, 2021
 * Teacher: Mr. Ho
 * <p>
 * This program generates a bar chart using the JFreeChart API. 
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class BenfordBarChart extends ApplicationFrame {
   
   /**
    * Constructor for the BenfordBarChart class. This constructor will create an instance of the barchar object,
    * set the size, labels, and dataset.
    *
    * @param chartTitle Chart title and program title for the bar chart. 
    * @param xLabel Bar chart x axis label.
    * @param yLabel Bar chart y axis label.
    * @param dataset Array containing the leading digit distribution as a percentage (float).
    */
   public BenfordBarChart(String chartTitle, String xLabel, String yLabel, float[] dataset) {
      
      // Adds the chart title to the program title. 
      super(chartTitle);     
      
      // Constructs a new bar chart using the values passed in to the method. 
      JFreeChart barChart = ChartFactory.createBarChart(chartTitle, xLabel, yLabel, createDataset(dataset), PlotOrientation.VERTICAL, true, true, false);
      
      // Constructs a panel that displays the specified chart.
      ChartPanel chartPanel = new ChartPanel(barChart);    
      
      // Sets window size for bar chart pop-up    
      chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
      setContentPane(chartPanel); 
      
      // Removes the legend from the bar graph.
      barChart.removeLegend();

      // Displays an overlay of the numeric percentage on each bar in the bar graph. 
      BarRenderer renderer = (BarRenderer) barChart.getCategoryPlot().getRenderer();
      renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
      renderer.setDefaultItemLabelsVisible(true);
      ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER);
      renderer.setDefaultPositiveItemLabelPosition(position); 

   }

   /**
    * This is a helper method. It creates a new dataset for the BenfordBarChart from an array of floats.
    *
    * @param data Array containing the leading digit distribution as a percentage (float).
    * @return 
    */
   private CategoryDataset createDataset(float[] data) {

      // Creates a new empty dataset.
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
      
      // Creates a new string series. Used so that all the leading digits are in the same series. 
      String series1 = "DigitFrequency";

      // For loop goes through every index in the array and rounds the value to 1 decimal place. Then, it adds the number it
      // represents at the beginning and a percentage sign at the end.
      for (int i = 0; i < data.length; i++) {

         // rounds sales data percentage to 1 decimal place.
         double roundedSalesPercent = Math.round(data[i] * 10)/10.0;

         // Adds the roundedSalesPercent, series, and digit to the dataset. 
         dataset.addValue(roundedSalesPercent, series1, String.valueOf(i+1));
      }
      
      // returns the dataset.
      return dataset; 
   }
}