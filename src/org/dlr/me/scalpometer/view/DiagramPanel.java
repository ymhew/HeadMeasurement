/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.view;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JPanel;
import org.dlr.me.scalpometer.controller.StatisticUnit;

/**
 *
 * @author Matthias Bremser
 */
class DiagramPanel extends JPanel {
    private StatisticUnit statUnit;
    private ArrayList<Double> lineLengths;
    private double sd;
    private double max;
    private double min;
    private double mean;
    private int n;
    final private DecimalFormat df = (DecimalFormat)DecimalFormat.getInstance(Locale.GERMAN);

    public void setStatisticalInfo(StatisticUnit statUnit) {
        this.statUnit = statUnit;
        this.sd = statUnit.getStandardDeviation();
        this.mean = statUnit.getMean();
        this.n = statUnit.getQuantity();
        this.max = statUnit.getMeasuredMax();
        this.min = statUnit.getMeasuredMin();
        this.lineLengths = statUnit.getLineLengths();
        df.applyPattern( "#,###,##0.000" );
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = this.getWidth();
        int height = this.getHeight();
        int radius = ((height / 100) < 1) ? 1 : (height / 100);
        double range = max - min;
        double stepX = (width * 1.0) / (n * 1.0);
        double stepY = height / range;
        
        // Draw background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, width, height);

        // Draw statistical info
        if (statUnit != null) {
            int x0, x1, y0, y1;

            // Draw mean
            x0 = 0;
            x1 = width;
            y0 = round((max - mean) * stepY);;
            y1 = y0;
            g.setColor(Color.WHITE);
            g.drawLine(x0, y0, x1, y1);
            g.drawLine(x0, y0, x1, y1);
            g.drawString(df.format(mean), x0, y0);

            // Draw deviation of 0.1
//            x0 = 0;
//            y0 = round((max - (mean + 0.1)) * stepY);;
//            x1 = width;
//            g.setColor(Color.RED);
//            drawHorizontalDashedLine(x0+g.getFontMetrics().stringWidth("0.1"), x1, y0, g);
//            g.drawString("0.1", x0, y0);
//            y0 = round((max - (mean - 0.1)) * stepY);;
//            drawHorizontalDashedLine(x0+g.getFontMetrics().stringWidth("-0.1"), x1, y0, g);
//            g.drawString("-0.1", x0, y0);

            // Draw real standard deviation above and below mean
            x0 = 0;
            y0 = round((max - (mean + sd)) * stepY);;
            x1 = width;
            y1 = y0;
            g.setColor(Color.CYAN);
            g.drawLine(x0, y0, x1, y1);
            g.drawString(df.format(sd), x0, y0);
            y0 = round((max - (mean - sd)) * stepY);;
            y1 = y0;
            g.drawLine(x0, y0, x1, y1);
            g.drawString("-"+df.format(sd), x0, y0);

            // Draw values
            g.setColor(Color.YELLOW);
            for (int i = 0; i < lineLengths.size(); i++) {
                double length = (lineLengths.get(i).doubleValue() * 1.0);
                x0 = round(i * stepX);
                y0 = round((max - length) * stepY);
                g.fillOval(x0, y0, radius, radius);
            }

        }
    }

    private int round(double d) {

        return Math.round(Math.round(d));
    }
//
//    private void drawHorizontalDashedLine(int x0, int x1, int y0, Graphics g) {
//        int delta = (x1-x0)/2;
//        for (int i = x0; i <= x1; i+=delta) {
//            g.drawLine(i, y0, i+1*delta, y0);
//        }
//    }
}
