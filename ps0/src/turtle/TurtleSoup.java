/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import rules.RulesOf6005;
import java.util.ArrayList;
import java.lang.Math;


public class TurtleSoup {

    /**
     * Draw a square.
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
    	final int sideNumberOfSquare = 4;
    	final int angleOfSquare = 90; 
        for(int i=0;i<sideNumberOfSquare;i++) { 
            turtle.forward(sideLength);
            turtle.turn(angleOfSquare);
        }
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        return 180.-360./((double) sides);
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        return Math.round((float) (360./(180. - angle)));
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        for(int i = 0;i<sides;i++) {
            turtle.forward(sideLength); 
            turtle.turn(180.-calculateRegularPolygonAngle(sides)); // from interior angle to exterior angle  
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {

        double dY = targetY-currentY;
        double dX = targetX-currentX;
        double angle = 0.; 
        // because the range of Math.atan is from -Pi/2 to Pi/2, we need below if else 
        // code to fix the angle range. 
        if(dX>=0) {
            if(dY>=0) {
                angle = Math.atan((double) (dY) /(double) (dX))*180/Math.PI;
            }else {
                angle = Math.atan((double) (dY) /(double) (dX))*180/Math.PI ;
            }
            
        }else {
            if(dY>=0) {
                angle = Math.atan((double) (dY) /(double) (dX))*180/Math.PI+180.;
            }else {
                angle = Math.atan((double) (dY) /(double) (dX))*180/Math.PI-180.;
            }            
        }
        angle = angle - 90; // starting from y-axis instead of starting from x-axis 
        angle = - angle; // from counter clock-wise to clock-wise  
        if(angle<0) angle = angle + 360; 
        double result_angle = angle-currentHeading; 
        if(result_angle<0) result_angle = result_angle + 360;
        return result_angle;
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        //throw new RuntimeException("implement me!");
        double original_angle = 0.;
        double new_angle = 0.;
        List<Double> angle_list = new ArrayList<Double>();
        for(int i = 0;i<xCoords.size()-1;i++) {
            new_angle = calculateHeadingToPoint(original_angle,xCoords.get(i),yCoords.get(i),xCoords.get(i+1),yCoords.get(i+1));
            angle_list.add(new_angle);
            original_angle = new_angle;
        }
        return angle_list;
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        //throw new RuntimeException("implement me!");
        
        /**
        List<Integer> xpoints = new ArrayList<>();
        List<Integer> ypoints = new ArrayList<>();
        for(int i = 0; i<100;i++) {
            xpoints.add(i);
            ypoints.add(i*i);
        }
        List<Double> headings = calculateHeadings(xpoints,ypoints);
        for(int i=0;i<headings.size();i++) {
            turtle.turn(headings.get(i));
            turtle.forward(10);
        }
        **/ 
        
        
        int repeat = 50;
        for(int i=0;i<repeat;i++) {
            turtle.forward(i * 10);
            turtle.turn(144);
        }

    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();
        //drawSquare(turtle, 40);
        //drawRegularPolygon(turtle,5, 50);
        drawPersonalArt(turtle);
        turtle.draw();
    }

}
