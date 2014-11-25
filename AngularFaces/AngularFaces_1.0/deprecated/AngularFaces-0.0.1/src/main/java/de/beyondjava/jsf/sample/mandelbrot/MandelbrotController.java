/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.sample.mandelbrot;

import java.io.Serializable;

import javax.faces.bean.*;
import javax.validation.constraints.*;

/**
 * This is the Java controller of the Mandelbrot demo showing sliders,
 * comboboxes, checkboxes and AngularJS watches.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@ManagedBean
@ViewScoped
public class MandelbrotController implements Serializable {
   private static final long serialVersionUID = 685843404038345451L;

   @Min(10)
   @Max(100)
   private int aperture = 80;

   private int[][] mandelbrotSet;

   private int maxIterations = 1023;

   @Min(1)
   @Max(4)
   private int quality = 4;

   @Min(128)
   @Max(1024)
   private int resolution = 256;

   private boolean showGlobeDemo = true;

   @NotNull
   @Min(-5)
   @Max(5)
   private double xMax = 1;

   @NotNull
   @Min(value = -5)
   @Max(value = 5)
   private double xMin = -2;

   @NotNull
   @Min(value = -1)
   @Max(value = 3)
   private double yMax = 1;
   @NotNull
   @Min(value = -3)
   @Max(value = 1)
   private double yMin = -1;

   public void calculateAction() {
   }

   public int getAperture() {
      return aperture;
   }

   /**
    * @return the mandelbrotSet
    */
   public int[][] getMandelbrotSet() {
      setMandelbrotSet(new MandelbrotCalculator().calculate(xMin, xMax, yMin, yMax, maxIterations, resolution));
      return mandelbrotSet;
   }

   /**
    * This slightly weird code brings the mandelbrot set to the client.
    * 
    * @return a long string consisting of a Javascript function returning a
    *         2-dimensional array of mandelbrot set interation depths.
    */
   public String getMandelbrotSetAsString() {
      StringBuffer s = new StringBuffer();
      s.append("var data=[];\r\n");
      s.append("var temp=[];\r\n");
      int[][] set = getMandelbrotSet();
      for (int[] element : set) {
         StringBuffer tmp = new StringBuffer();
         for (int value : element) {
            tmp.append(",");
            tmp.append(String.valueOf(value));
         }
         s.append("temp = [" + tmp.substring(1) + "];\r\n");
         s.append("data = data.concat(temp);\r\n");
      }
      return s.toString();
   }

   /**
    * @return the maxIterations
    */
   public int getMaxIterations() {
      return maxIterations;
   }

   public int getQuality() {
      return quality;
   }

   /**
    * @return the resolution
    */
   public int getResolution() {
      return resolution;
   }

   public double getxMax() {
      return xMax;
   }

   public double getxMin() {
      return xMin;
   }

   public double getyMax() {
      return yMax;
   }

   public double getyMin() {
      return yMin;
   }

   /**
    * @return the showGlobeDemo
    */
   public boolean isShowGlobeDemo() {
      return showGlobeDemo;
   }

   public void setAperture(int aperture) {
      this.aperture = aperture;
   }

   /**
    * @param mandelbrotSet
    *           the mandelbrotSet to set
    */
   public void setMandelbrotSet(int[][] mandelbrotSet) {
      this.mandelbrotSet = mandelbrotSet;
   }

   /**
    * @param maxIterations
    *           the maxIterations to set
    */
   public void setMaxIterations(int maxIterations) {
      this.maxIterations = maxIterations;
   }

   public void setQuality(int quality) {
      this.quality = quality;
   }

   /**
    * @param resolution
    *           the resolution to set
    */
   public void setResolution(int resolution) {
      this.resolution = resolution;
   }

   /**
    * @param showGlobeDemo
    *           the showGlobeDemo to set
    */
   public void setShowGlobeDemo(boolean showGlobeDemo) {
      this.showGlobeDemo = showGlobeDemo;
   }

   public void setxMax(double xMax) {
      this.xMax = xMax;
   }

   public void setxMin(double xMin) {
      this.xMin = xMin;
   }

   public void setyMax(double yMax) {
      this.yMax = yMax;
   }

   public void setyMin(double yMin) {
      this.yMin = yMin;
   }

}
