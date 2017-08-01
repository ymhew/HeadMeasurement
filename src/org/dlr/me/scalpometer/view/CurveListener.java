package org.dlr.me.scalpometer.view;

/**
 * This class is handling mouse events on ImagePanel
 * @author Matthias Bremser
 */
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.event.MouseInputListener;

public class CurveListener implements MouseInputListener {
    private ArrayList<Bezier> curves;
    private ImagePanel imagePanel;
    private boolean selected;
    private int scalePoints = 2;
    /** (0) no, (1) horizontal, (2) horizontal calibration */
    private int calibration = 0;
    private int x0, x1, y0, y1;
    private View view;

    public CurveListener(ArrayList<Bezier> curves, ImagePanel imagePanel) {
        this.curves = curves;
        this.imagePanel = imagePanel;
        imagePanel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent aMouseEvent) {
        if (scalePoints == 2) {
            int i = 0;
            if (imagePanel.isImageSet()) {
                for (Bezier curve: curves) {
                    if (curve.addPoint(aMouseEvent.getX(), aMouseEvent.getY()) != -1) {
                        break;
                    }
                }
                imagePanel.repaint();
            }
        } else {
            calibrate(aMouseEvent.getPoint());
        }
    }

    @Override
    public void mousePressed(MouseEvent aMouseEvent) {
        if (scalePoints == 2) {
            if (imagePanel.isImageSet()) {
                for (ControlCurve curve: curves) {
                    if (curve.selectPoint(aMouseEvent.getX(), aMouseEvent.getY()) != -1) {
                        selected = true;
                    }
                }
                imagePanel.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent aMouseEvent) {
        if (scalePoints == 2) {
            if (imagePanel.isImageSet()) {
                for (ControlCurve curve: curves) {
                    curve.unselectPoint();
                }
                selected = false;
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (scalePoints == 2) {
            if (imagePanel.isImageSet()) {
                if (selected) {
                    imagePanel.clearMeasurementPoints();
                    for (ControlCurve curve: curves) {
                        curve.setPoint(e.getX(), e.getY());
                    }
                    imagePanel.repaint();
                }
            }
        }
    }

    public void mouseEntered(MouseEvent aMouseEvent) {
        // No op.
    }

    public void mouseExited(MouseEvent aMouseEvent) {
        // No op.
    }

    public void mouseMoved(MouseEvent e) {
        // No op.
    }

    private void calibrate(Point point) {
        if (calibration == 1) {
            switch (scalePoints) {
                case 0:
                    x0 = point.x;
                case 1:
                    x1 = point.x;
                    view.setScale((Math.abs(x1 - x0)));
            }
        } else if (calibration == 2) {
            switch (scalePoints) {
                case 0:
                    y0 = point.y;
                case 1:
                    y1 = point.y;
                    view.setScale(Math.abs(y1 - y0));
            }
        }
        scalePoints++;
    }

    void calibrateScalesHorizontally(View view) {
        this.view = view;
        calibration = 1;
        scalePoints = 0;
    }

    void calibrateScalesVertically(View view) {
        this.view = view;
        calibration = 2;
        scalePoints = 0;
    }
    
} 