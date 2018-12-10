package ConvexHull;

import java.util.Arrays;
import java.util.Stack;

public class GrahamScan {
    private Stack<Point2D> hull = new Stack<Point2D>();

    public GrahamScan(Point2D[] ringEdgePoints) {

        int len = ringEdgePoints.length;
        int index1,index2;
        
        Point2D[] ringEdgePointsCopy = new Point2D[len];
        for (int i = 0; i < len; i++)
            ringEdgePointsCopy[i] = ringEdgePoints[i];

        Arrays.sort(ringEdgePointsCopy);
        Arrays.sort(ringEdgePointsCopy, 1, len, ringEdgePointsCopy[0].POLAR_ORDER);

        hull.push(ringEdgePointsCopy[0]);
        
        for (index1 = 1; index1 < len; index1++){
            if (!ringEdgePointsCopy[0].equals(ringEdgePointsCopy[index1])) 
            	break;
        }
        
        if (index1 == len) 
        	return;
        
        for (index2 = index1 + 1; index2 < len; index2++){
            if (Point2D.ccw(ringEdgePointsCopy[0], ringEdgePointsCopy[index1], ringEdgePointsCopy[index2]) != 0) 
            	break;
            }
        
        hull.push(ringEdgePointsCopy[index2-1]);

        for (int i = index2; i < len; i++) {
            Point2D top = hull.pop();
            while (Point2D.ccw(hull.peek(), top, ringEdgePointsCopy[i]) <= 0) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(ringEdgePointsCopy[i]);
        }

        assert isConvex(): "END";
    }

    public Iterable<Point2D> hull() {
        Stack<Point2D> stack = new Stack<Point2D>();
        for (Point2D p : hull) 
        	stack.push(p);
        return stack;
    }

    private boolean isConvex() {
        int N = hull.size();
        int n = 0;
        Point2D[] points = new Point2D[N];
        
        if (N <= 2) 
        	return true;
        
        for (Point2D p : hull()) {
            points[n++] = p;
        }

        for (int i = 0; i < N; i++) {
            if (Point2D.ccw(points[i], points[(i+1) % N], points[(i+2) % N]) <= 0) {
                return false;
            }
        }
        
        return true;
    }
}