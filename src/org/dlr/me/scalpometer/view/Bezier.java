/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 *
 * @author Matthias Bremser, Dr. Uwe Mittag
 */
public class Bezier extends ControlCurve {
    final int STEPS;
    Polygon pts_old = new Polygon();

    public Bezier(int STEPS) {
        this.STEPS = STEPS;
    }

    public int Sx0,Sx1,Sx2,Sx3,Sy0,Sy1,Sy2,Sy3;

    private void calcSxy() {
        Sx3 = (-pts.xpoints[0]+ 3 * pts.xpoints[1] - 3*pts.xpoints[2] + pts.xpoints[3]);
        Sx2 = (3*pts.xpoints[0]-6 * pts.xpoints[1] + 3*pts.xpoints[2]);
        Sx1 = (-3*pts.xpoints[0]+ 3 * pts.xpoints[1]);
        Sx0 = pts.xpoints[0];
        Sy3 = (-pts.ypoints[0]+ 3 * pts.ypoints[1] - 3*pts.ypoints[2] + pts.ypoints[3]);
        Sy2 = (3*pts.ypoints[0]-6 * pts.ypoints[1] + 3*pts.ypoints[2]);
        Sy1 = (-3*pts.ypoints[0]+ 3 * pts.ypoints[1]);
        Sy0 = pts.ypoints[0];

    }

    /**
     * Implements C(t) from Wikipedia.
     * @return
     */
    public float CxofT(float t) {
         return ( Sx3*t*t*t + Sx2*t*t + Sx1*t +Sx0 );
    }
    public float CyofT(float t) {
        return ( Sy3*t*t*t + Sy2*t*t + Sy1*t +Sy0 );
    }

    /**
     * d / dt C(t) - The derivate for t of C(t)
     * @param t
     * @param P0
     * @param P1
     * @param P2
     * @param P3
     * @return
     */
    private float DerivateOfCxOfT(float t, int Sx1, int Sx2, int Sx3) {
        return ( 3*Sx3*t*t + 2*Sx2*t + Sx1 );
    }
    private float DerivateOfCyOfT(float t, int Sy1, int Sy2, int Sy3) {
        return ( 3*Sy3*t*t + 2*Sy2*t + Sy1 );
    }

    
    /* pts are the P0, ..., P3 */

    /**
     * Implements C(t) from Wikipedia.
     * @param t
     * @param P0
     * @param P1
     * @param P2
     * @param P3
     * @return
     */
    public double c(double t, int P0, int P1, int P2, int P3) {
        if (t >= 0 && t <= 1) {

            /*return ( ( 1 - t ) * ( 1 - t ) * ( 1 - t ) * P0
                    + 3 * t * ( 1 - t ) * ( 1 - t ) * P1
                    + 3 * t * t * ( 1 - t ) * P2
                    + t * t * t * P3 );*/
            return (-P0 + 3*P1 - 3*P2 + P3)*t*t*t +(3*P0 - 6*P1 + 3*P2)*t*t + (-3*P0 + 3*P1)*t + P0;
        } else {
            
            return -1;
        }
    }

    /**
     * d / dt C(t) - The derivate for t of C(t)
     * @param t
     * @param P0
     * @param P1
     * @param P2
     * @param P3
     * @return
     */
    public double DerivateOfC(double t, int P0, int P1, int P2, int P3) {
        if (t >= 0 && t <= 1) {
                    /*return ( ( - 3 * ( 1 - t ) * ( 1 - t ) ) * P0
                    + ( 3 * ( 1 - t ) * ( 1 - t ) - 6 * t * ( 1 - t ) ) * P1
                    + ( -3 * t * t + 6 * t * ( 1 - t ) ) * P2
                    + (3 * t * t) * P3 );*/
                    return 3*(-P0 + 3*P1 - 3*P2 + P3)*t*t +2*(3*P0 - 6*P1 + 3*P2)*t + (-3*P0 + 3*P1);
        } else {

            return -1;
        }
    }

    public int getX(double t) {
        int x = -1;

        if (t >= 0&& t <= 1) {
            x = Math.round(Math.round(c(t, pts.xpoints[0], pts.xpoints[1], pts.xpoints[2], pts.xpoints[3])));
        }

        return x;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        calcSxy();
        if (pts.npoints >= 4) {
        recalcPolygon();
        drawDashedPolygonLine(g, polygon.xpoints, polygon.ypoints, polygon.npoints);
        }
    }

    public double getM(double t) {
        double dx = (DerivateOfC(t, pts.xpoints[0], pts.xpoints[1], pts.xpoints[2], pts.xpoints[3]));
        double dy = (DerivateOfC(t, pts.ypoints[0], pts.ypoints[1], pts.ypoints[2], pts.ypoints[3]));
        double m = (dy / dx);

        return m;
    }

    public double getN(double t) {
        double n = - 1 / getM(t);

        return n;
    }

    private void drawPolygonLine(Graphics g, int[] xpoints, int[] ypoints, int npoints) {
        Color color = g.getColor();
        g.setColor(Color.YELLOW);
        for (int i = 0; i < npoints - 1; i++) {
            g.drawLine(xpoints[i], ypoints[i], xpoints[i], ypoints[i]);
        }
        g.setColor(color);
    }

    private void drawDashedPolygonLine(Graphics g, int[] xpoints, int[] ypoints, int npoints) {
        Color color = g.getColor();
        g.setColor(Color.YELLOW);
        for (int i = 0; i < npoints - 1; i += 8) {
            g.drawLine(xpoints[i], ypoints[i], xpoints[i+2], ypoints[i+2]);
        }
        g.setColor(color);
    }

    @Override
    public Polygon getPolygon() {
        recalcPolygon();

        return polygon;
    }

    private void recalcPolygon() {
        double t;
        int x;
        int y;

        polygon.reset();
        for (int i = 1; i <= STEPS; i++) {
            t = (double) i / STEPS;
            x = Math.round(Math.round(c(t, pts.xpoints[0], pts.xpoints[1], pts.xpoints[2], pts.xpoints[3])));
            y = Math.round(Math.round(c(t, pts.ypoints[0], pts.ypoints[1], pts.ypoints[2], pts.ypoints[3])));
            polygon.addPoint(x, y);
        }
    }

    public int getY(double t) {
        int y = -1;

        if (t >= 0&& t <= 1) {
            y = Math.round(Math.round(c(t, pts.ypoints[0], pts.ypoints[1], pts.ypoints[2], pts.ypoints[3])));
        }
        return y;
    }
}
