package org.dlr.me.scalpometer.view;

/** This class represents a curve defined by a sequence of control points */
import java.awt.*;

public class ControlCurve {

  public Polygon pts;
  protected int selection = -1;
  private boolean blocked = false;
  private boolean full = false;
  protected Polygon polygon;
  public ControlCurve() {
    polygon = new Polygon();
      pts = new Polygon();
//    initCurve();
  }

  static Font f = new Font("Courier",Font.PLAIN,12);

  /** paint curve control points into g.*/
  public void paint(Graphics g){
    Color color = g.getColor();
    g.setColor(Color.YELLOW);
    FontMetrics fm = g.getFontMetrics(f);
    g.setFont(f);
    int h = fm.getAscent()/2;

    for(int i = 0; i < pts.npoints; i++)  {
      String s = Integer.toString(i);
      int w = fm.stringWidth(s)/2;
      g.drawString(Integer.toString(i),pts.xpoints[i]-w,pts.ypoints[i]+h);
    }
    g.setColor(color);
  }

  static final int EPSILON = 36;  /* square of distance for picking */

  /** return index of control point near to (x,y) or -1 if nothing near */
  public int selectPoint(int x, int y) {
    if (!isBlocked()) {
        int mind = Integer.MAX_VALUE;
        selection = -1;
        for (int i = 0; i < pts.npoints; i++) {
          int d = sqr(pts.xpoints[i]-x) + sqr(pts.ypoints[i]-y);
          if (d < mind && d < EPSILON) {
            mind = d;
            selection = i;
          }
        }
      }
    return selection;
  }

  public void unselectPoint() {
      selection = -1;
  }

  // square of an int
  static int sqr(int x) {
    return x*x;
  }

  /** add a control point, return index of new control point */
  public int addPoint(int x, int y) {
      if (pts.npoints < 4) {
        pts.addPoint(x,y);
        if (pts.npoints == 4) {
            full = true;
        }
        return selection = pts.npoints - 1;
      }

      return -1;
    
  }

  /** set selected control point */
  public void setPoint(int x, int y) {
    if (selection >= 0) {
      pts.xpoints[selection] = x;
      pts.ypoints[selection] = y;
    }
  }

  /** remove selected control point */
  public void removePoint() {
    if (selection >= 0) {
      pts.npoints--;
      for (int i = selection; i < pts.npoints; i++) {
	pts.xpoints[i] = pts.xpoints[i+1];
	pts.ypoints[i] = pts.ypoints[i+1];
      }
    }
  }

  public String toString() {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < pts.npoints; i++) {
      result.append(" " + pts.xpoints[i] + " " + pts.ypoints[i]);
    }
    return result.toString();
  }

  /**
   * Supplies with some few points to start with.
   */
    private void initCurve() {
//        int[] points = {33, 189, 54, 39, 182, 42, 249, 113};
//        for (int i = 0; i < points.length; i+=2) {
//            this.addPoint(points[i], points[i + 1]);
//        }
      }

        /**
         * @return the blocked
         */
        public boolean isBlocked() {
            return blocked;
        }

        /**
         * @param blocked the blocked to set
         */
        public void setBlocked(boolean blocked) {
            this.blocked = blocked;
        }

        /**
         * Toggles if curve is blocked for
         */
        public void toogleBlocked() {
            this.blocked = !blocked;
        }

        public String getBlocked() {
            if (blocked) {

                return " (blocked)";
            } else {

                return "";
            }
        }

        public int getSize() {

            return pts.npoints;
        }

        // The extra polygon stuff is no longer needed. TBR - To Be Removed.
        public Polygon getPolygon() {
            throw new UnsupportedOperationException("Needs to be overridden by child");
        }

        public boolean isFull() {

            return full;
        }




    }
