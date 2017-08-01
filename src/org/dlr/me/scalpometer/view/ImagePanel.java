/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.view;

import ij.ImageStack;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.dlr.me.scalpometer.controller.Controller;
import org.dlr.me.scalpometer.controller.CurveObserver;
import org.dlr.me.scalpometer.controller.StatisticUnit;

/**
 *
 * @author Matthias Bremser
 */
public class ImagePanel extends JPanel {
    private Image image;
    private ArrayList<Bezier> curves;
    private final int STEPS = (int) Math.pow(2, 8); 
    private Bezier curve1 = new Bezier(STEPS);
    private Bezier curve2 = new Bezier(STEPS);
    private CurveListener curveListener;
    private CurveObserver curveObserver;
    private ArrayList<Point> measurePoints = new ArrayList<Point>();
    private int imageCount = -1;
    private Controller controller;
    private final View view;
    private Dimension size;

    public void resetImageCount() {
        imageCount = -1;
    }

    public StatisticUnit measureDistance(int amount, double scale, String unit) {
        StatisticUnit statUnit = curveObserver.measureDistance(amount, scale, unit);
        this.measurePoints = statUnit.getMeasurePoints();

        return statUnit;
    }

    public void clearMeasurementPoints() {
        measurePoints.clear();
    }

    ImagePanel(Controller controller, View view) {
        this.controller = controller;
        this.view = view;
        this.size = new Dimension();
        curves = new ArrayList<Bezier>();
        curves.add(curve1);
        curves.add(curve2);
        curveListener = new CurveListener(curves, this);
        curveObserver = new CurveObserver(curve1, curve2);
        this.addMouseListener(curveListener);
        this.addMouseMotionListener(curveListener);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
        curve1.paint(g);
        curve2.paint(g);
        Color color = g.getColor();
        g.setColor(Color.yellow);
        for (int i = 0; i < measurePoints.size(); i += 2) {
            g.drawLine(measurePoints.get(i).x, measurePoints.get(i).y,
                    measurePoints.get(i + 1).x, measurePoints.get(i + 1).y);
        }
        g.setColor(color);
    }

    void toggleCurveBlock(int i) {
        ControlCurve curve = curves.get(i - 1);
        curve.toogleBlocked();
    }

    String getBlocked(int i) {
        
        return curves.get(i - 1).getBlocked();
    }

    boolean isImageSet() {
        return image != null;
    }

    void nextImage() {
            ImageStack stack = controller.getImageStackHolder().getStack();
            image = stack.getProcessor((imageCount = ++imageCount % stack.getSize()) + 1).createImage();
            size.width = image.getWidth(this);
            size.height = image.getHeight(this);
            setSize(size);
            setMaximumSize(size);
            setPreferredSize(size);
            repaint();
    }

    int getImageCount() {

        return imageCount;
    }

    void calibrateScalesHorizontally() {
        curveListener.calibrateScalesHorizontally(view);
    }

    void calibrateScaleVertically() {
        curveListener.calibrateScalesVertically(view);
    }
    
}
