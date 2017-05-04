//package com.example.apple.nooneleftbehind.ControlsPackage;
//
///**
// * Created by apple on 4/12/17.
// * This class uses the car commands, car status
// * to map to an x and y coordinate at every point
// * in real time.
// * It is used to draw the path of the robot.
// */
//
//public class PathCoordinates {
//
//    private int x;
//    private int y;
//
//    public int getX() {
//        return x;
//    }
//
//    public void setX(int x) {
//        this.x = x;
//    }
//
//    public int getY() {
//        return y;
//    }
//
//    public void setY(int y) {
//        this.y = y;
//    }
//
//    public void trackPathCoordinates(int stepsTraversed, int heading) {
////        if ((y == 130) && (x < 650)) {
////           x += 5;
////       }
////
////       if ((y < 650) && (x == 650)) {
////           y += 5;
////       }
////
////       if ((y == 650) && (x > 130)) {
////           x -= 5;
////       }
////
////       if ((y > 130) && (x == 130)) {
////           y -= 5;
////       }
//
//        int newX, newY;
//
//        newX = x + stepsTraversed*(int)Math.cos(heading);
//        newY = y + stepsTraversed*(int)Math.sin(heading);
//
//        x = newX;
//        y = newY;
//
//    }
//}
