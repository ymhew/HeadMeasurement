/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.controller;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Matthias Bremser
 */
public class StatisticUnit {
    private ArrayList<Point> measurePoints;
    private ArrayList<Double> lineLengths;
    private int quantity;
    private double mean;
    private double standardDeviation;
    private double scale;
    private String unit;

    StatisticUnit(ArrayList<Point> measurePoints, double scale, String unit) {
        this.unit = unit;
        this.scale = scale;
        this.measurePoints = measurePoints;
        calcLineLengths();
        this.quantity = lineLengths.size();
        calcMean();
        calcStandardDeviation();
    }

    private String getUnit() {

        return unit;
    }

    public ArrayList<Double> getLineLengths() {
        
        return lineLengths;
    }

    public Double[] getLineLengthsArray() {
        
        return lineLengths.toArray(new Double[0]);
    }

    public ArrayList<Point> getMeasurePoints() {
        
        return measurePoints;
    }

    private void calcLineLengths() {
        double lineHeight;
        double lineWidth;
        this.lineLengths = new ArrayList<Double>();

        for (int i = 0; i < measurePoints.size(); i += 2) {
            lineWidth = (Math.abs(measurePoints.get(i).x - measurePoints.get(i + 1).x) * scale);
            lineHeight = (Math.abs(measurePoints.get(i).y - measurePoints.get(i + 1).y) * scale);
            lineLengths.add(Math.sqrt(((lineHeight * lineHeight) + (lineWidth * lineWidth))));
        }
    }

    private void calcMean() {
        double sumOfAll = 0;
        int i;
        
        for (i = 0; i < lineLengths.size(); i++) {
            sumOfAll += lineLengths.get(i);
        }
        this.mean = sumOfAll / i;
    }

    private void calcStandardDeviation() {
        double sum = 0;

        for (int i = 0; i < lineLengths.size(); i++) {
            sum += (lineLengths.get(i) - mean) * (lineLengths.get(i) - mean);
        }
        this.standardDeviation = Math.sqrt(sum / ((lineLengths.size() - 1 <= 0) ? 1 : lineLengths.size() - 1));
    }

    public double getMean() {

        return this.mean;
    }

    public double getStandardDeviation() {

        return this.standardDeviation;
    }

    public int getQuantity() {

        return this.quantity;
    }

    public double getMeasuredMax() {
        double max = 0;

        for (Double d : lineLengths) {
            if (d > max) {
                max = d;
            }
        }

        return max;
    }

    public double getMeasuredMin() {
        double min = Double.MAX_VALUE;

        for (Double d: lineLengths) {
            if (d < min) {
                min = d;
            }
        }

        return min;
    }

}
