package pl.zoltowski.damian.utils;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import pl.zoltowski.damian.utils.dataType.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Util {


    public static double calculateDistanceBetweenPoints(Point point1, Point point2) {
        return Math.sqrt((point2.getY() - point1.getY()) * (point2.getY() - point1.getY()) + (point2.getX() - point1.getX()) * (point2.getX() - point1.getX()));
    }

    public static boolean isPointOutsideBoard(int x_dimension, int y_dimension, Point pointToCheck) {
        return pointToCheck.getX() > x_dimension || pointToCheck.getX() < 0
                || pointToCheck.getY() > y_dimension || pointToCheck.getY() < 0;
    }

    public static double pDistance(Point startingPoint, Point endingPoint, Point pointToCheckDistance) {

        double a = endingPoint.getY() - startingPoint.getY();
        double b = startingPoint.getX() - endingPoint.getX();
        double c = (-b) * endingPoint.getY() + (-a) * endingPoint.getX();


        double distance = Math.abs((a * pointToCheckDistance.getX() + b * pointToCheckDistance.getY() + c)) /
                (Math.sqrt(a * a + b * b));

        if(distance < 1E-12){
            distance = 0.0;
        }
        return distance;
    }

    public static boolean isPointOnTheLine(Point lineStartingPoint, Point lineEndingPoint, Point pointToCheck) {
        double a = lineEndingPoint.getY() - lineStartingPoint.getY();
        double b = lineStartingPoint.getX() - lineEndingPoint.getX();
        double c = (-b) * lineEndingPoint.getY() + (-a) * lineEndingPoint.getX();
        // equation Ax + By + C = 0

        return a * pointToCheck.getX() + b * pointToCheck.getY() + c == 0.0;
    }
}
