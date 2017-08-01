/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.controller;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import org.dlr.me.scalpometer.view.Bezier;

/**
 * This class shall hold polygons of each curve on ImagePanel and be able
 * to call methods of curves to measure their distance, angle etc.
 * @author Matthias Bremser, Dr. Uwe Mittag
 */
public class CurveObserver {
    Bezier curve1;
    Bezier curve2;
    Bezier upperBezier;
    Bezier lowerBezier;
    Polygon polygon1;
    Polygon polygon2;
        
    public CurveObserver(Bezier curve1, Bezier curve2) {
        this.curve1 = curve1;
        this.curve2 = curve2;
    }

    public StatisticUnit measureDistance(int amount, double scale, String unit) {
        ArrayList<Point> measurePoints;
        if (areAllCurvesReady()) {
            sortCurves();
            measurePoints = new ArrayList<Point>();
            double[] t = new double[amount];
            double step = 1 / (double) amount;
            int x0, x1, y0, y1;
            for (int i = 0; i < t.length; i++) {
                t[i] = intersection(step * i);
                if (t[i] <= 0) {
                    continue;
                }
                x0 = curve1.getX(step * i);
                y0 = curve1.getY(step * i);
                x1 = curve2.getX(t[i]);
                y1 =  curve2.getY(t[i]);

                measurePoints.add(new Point(x0, y0));
                measurePoints.add(new Point(x1, y1));

            }
            StatisticUnit statUnit = new StatisticUnit(measurePoints, scale, unit);

            return statUnit;
        }

        return null;
    }

    /**
     * compute the intersection point of the curve and normal
     * @param upperT is the t value for of the point on upper bezier from which
     * the normal should cross the lower bezier.
     * @return
     */
    private double intersection(double upperT) {
        sortCurves();
        double Q3, Q2, Q1, Q0, a, b, dif, t0, t1, t2, t0res, t1res, t2res;

        /* Get normal of upperBezier */
        double upperDX = (upperBezier.DerivateOfC(upperT, upperBezier.pts.xpoints[0], upperBezier.pts.xpoints[1], upperBezier.pts.xpoints[2], upperBezier.pts.xpoints[3]));
        double upperDY = (upperBezier.DerivateOfC(upperT, upperBezier.pts.ypoints[0], upperBezier.pts.ypoints[1], upperBezier.pts.ypoints[2], upperBezier.pts.ypoints[3]));

        /**
         * Compute components of the linear equation of the normal from tangent on upperT
         * y1 = ax1 + b
         * => b = y1 -ax1
         */
        double threshold = upperDY * upperDY;
        if (threshold <= Math.pow(10, -14)) {
            upperDY = Math.pow(10, -7);
        }

        a = - upperDX / upperDY;
        
        double upperX1 = upperBezier.getX(upperT);
        double upperY1 = upperBezier.getY(upperT);

        b = upperY1 - a * upperX1;
        
        /**
         * Now computing intersection with lowerBezier
         * We will approximating the equation of the form:
         * c(t) = a * x + b
         * to find the intersection point(s).
         */
        Q3 = lowerBezier.Sy3 - a * lowerBezier.Sx3;
        Q2 = lowerBezier.Sy2 - a * lowerBezier.Sx2;
        Q1 = lowerBezier.Sy1 - a * lowerBezier.Sx1;
        Q0 = lowerBezier.Sy0 - a * lowerBezier.Sx0;

        t0 = 0d;
        t1 = 0.5d;
        t2 = 1d;

        // vorzeichewechsel muss in interval sein, sonst gibt es keinen schnittpunkt, oder mehrere.
        t0res = Q3 * t0 * t0 * t0 + Q2 * t0 * t0 + Q1 * t0 + Q0 - b;
        t1res = Q3 * t1 * t1 * t1 + Q2 * t1 * t1 + Q1 * t1 + Q0 - b;
        t2res = Q3 * t2 * t2 * t2 + Q2 * t2 * t2 + Q1 * t2 + Q0 - b;

        if (t0res * t2res > 0) {
            System.err.println("There is not exacly 1 intersection: This means, there are multiple or none.");
            return -1;
        }

        while (true) {

            dif = (t2 - t1) / 2;
            if (isChangeOfSignInInterval(t0res, t1res)) {
                t2 = t1;
                t1 = t0 + dif;
            } else if (isChangeOfSignInInterval(t1res, t2res)) {
                t0 = t1;
                t1 = t1 + dif;
            } else {
                System.err.println("There is no change of sign within any interval. There are multiple or none intersections.");
                return -1;
            }

            if (Math.abs(t0 - t2) <= Math.pow(10, -5) ) {
                return t1;
            }

            t0res = Q3 * t0 * t0 * t0 + Q2 * t0 * t0 + Q1 * t0 + Q0 - b;
            t1res = Q3 * t1 * t1 * t1 + Q2 * t1 * t1 + Q1 * t1 + Q0 - b;
            t2res = Q3 * t2 * t2 * t2 + Q2 * t2 * t2 + Q1 * t2 + Q0 - b;
        }
    }

    private boolean isChangeOfSignInInterval(double a, double b) {

        return ( ( a > 0 && b < 0 ) || ( a < 0 && b > 0 ) ); // gleich null vorher abfangen.
    }

    private void sortCurves() {
        if (curve1.pts.ypoints[0] > curve2.pts.ypoints[0]) {
            lowerBezier = curve1;
            upperBezier = curve2;
        } else {
            lowerBezier = curve2;
            upperBezier = curve1;
        }
    }

    private boolean areAllCurvesReady() {
        
        return curve1.isFull() && curve2.isFull();
    }
}
