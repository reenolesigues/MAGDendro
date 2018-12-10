package ConvexHull;

import java.util.Comparator;

public class Point2D implements Comparable<Point2D> {
	
	private final int xCoord;   
    private final int yCoord;   
	
	public final Comparator<Point2D> POLAR_ORDER = new PolarOrder();
    public final Comparator<Point2D> ATAN2_ORDER = new Atan2Order();
    public final Comparator<Point2D> DISTANCE_TO_ORDER = new DistanceToOrder();
    
    public static final Comparator<Point2D> X_ORDER = new XOrder();
    public static final Comparator<Point2D> Y_ORDER = new YOrder();
    public static final Comparator<Point2D> R_ORDER = new ROrder(); 

    public Point2D(int x, int y) {
        this.xCoord = x;
        this.yCoord = y;
    }

    public int x() { 
    	return xCoord; 
	}

    public int y() { 
    	return yCoord; 
	}

    private double angleTo(Point2D that) {
        double dx = that.xCoord - this.xCoord;
        double dy = that.yCoord - this.yCoord;
        return Math.atan2(dy, dx);
    }

    public static int ccw(Point2D a, Point2D b, Point2D c) {
        double area2 = (b.xCoord-a.xCoord)*(c.yCoord-a.yCoord) - (b.yCoord-a.yCoord)*(c.xCoord-a.xCoord);
        if      (area2 < 0) 
        	return -1;
        else if (area2 > 0) 
        	return +1;
        else                
        	return  0;
    }

    public double distanceTo(Point2D that) {
        double dx = this.xCoord - that.xCoord;
        double dy = this.yCoord - that.yCoord;
        return Math.sqrt(dx*dx + dy*dy);
    }

    public double distanceSquaredTo(Point2D that) {
        double dx = this.xCoord - that.xCoord;
        double dy = this.yCoord - that.yCoord;
        return dx*dx + dy*dy;
    }

    public int compareTo(Point2D that) {
        if (this.yCoord < that.yCoord) 
        	return -1;
        if (this.yCoord > that.yCoord) 
        	return +1;
        if (this.xCoord < that.xCoord) 
        	return -1;
        if (this.xCoord > that.xCoord) 
        	return +1;
        return 0;
    }

    private static class XOrder implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            if (p.xCoord < q.xCoord) 
            	return -1;
            if (p.xCoord > q.xCoord) 
            	return +1;
            return 0;
        }
    }

    private static class YOrder implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            if (p.yCoord < q.yCoord) 
            	return -1;
            if (p.yCoord > q.yCoord) 
            	return +1;
            return 0;
        }
    }

    private static class ROrder implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            double delta = (p.xCoord*p.xCoord + p.yCoord*p.yCoord) - (q.xCoord*q.xCoord + q.yCoord*q.yCoord);
            if (delta < 0) 
            	return -1;
            if (delta > 0) 
            	return +1;
            return 0;
        }
    }
 
    private class Atan2Order implements Comparator<Point2D> {
        public int compare(Point2D q1, Point2D q2) {
            double angle1 = angleTo(q1);
            double angle2 = angleTo(q2);
            if      (angle1 < angle2) 
            	return -1;
            else if (angle1 > angle2) 
            	return +1;
            else                      
            	return  0;
        }
    }

    private class PolarOrder implements Comparator<Point2D> {
        public int compare(Point2D q1, Point2D q2) {
            double dx1 = q1.xCoord - xCoord;
            double dy1 = q1.yCoord - yCoord;
            double dx2 = q2.xCoord - xCoord;
            double dy2 = q2.yCoord - yCoord;

            if      (dy1 >= 0 && dy2 < 0) 
            	return -1;    
            else if (dy2 >= 0 && dy1 < 0) 
            	return +1;    
            else if (dy1 == 0 && dy2 == 0) {            
                if      (dx1 >= 0 && dx2 < 0) 
                	return -1;
                else if (dx2 >= 0 && dx1 < 0) 
                	return +1;
                else                          
                	return  0;
            }
            else 
            	return -ccw(Point2D.this, q1, q2);        }
    }

    private class DistanceToOrder implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            double dist1 = distanceSquaredTo(p);
            double dist2 = distanceSquaredTo(q);
            if      (dist1 < dist2) 
            	return -1;
            else if (dist1 > dist2) 
            	return +1;
            else                    
            	return  0;
        }
    }
}