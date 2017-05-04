//package com.example.apple.nooneleftbehind.ControlsPackage;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.apple.nooneleftbehind.ControlsPackage.PathCoordinates;
//
//import static com.example.apple.nooneleftbehind.ControlsPackage.CurrentlyCountingActivity.carCommands;
//import static com.example.apple.nooneleftbehind.ControlsPackage.CurrentlyCountingActivity.numPeople;
//import static com.example.apple.nooneleftbehind.ControlsPackage.CurrentlyCountingActivity.pathCoordinates;
//
///**
// * Created by apple on 4/16/17.
// * This is a layout class for Canvas that is used
// * to draw the path of the robot
// */
//
//public class CanvasLayout extends SurfaceView implements Runnable {
//
//    Thread thread = null;
//    boolean canDraw = false;
//    boolean atInit = true;
//
//    Paint tealBrush;
//    int pointX, pointY;
//    Canvas canvas;
//    SurfaceHolder surfaceHolder;
//
//    Path path = new Path();
//
//    static final int INITIAL_X = 130;
//    static final int INITIAL_Y = 130;
//
//
//    public CanvasLayout(Context context) {
//
//        super(context);
//        surfaceHolder = getHolder();
//        pathCoordinates = new PathCoordinates();
//
//        pointX = INITIAL_X;
//        pointY = INITIAL_Y;
//
//        pathCoordinates.setX(pointX);
//        pathCoordinates.setY(pointY);
//    }
//
//    @Override
//    public void run() {
//
//        prepBrush();
//
//        while(canDraw) {
//            //carry out drawing
//
//            if(!surfaceHolder.getSurface().isValid()) {
//                continue;
//            }
//
//            canvas = surfaceHolder.lockCanvas();
//
//            int tempX = pointX;
//            int tempY = pointY;
//            boolean didTheyChange = true;
//
//
//            path.moveTo(pointX, pointY);
//            motionPoint();
//
//            if(tempX == pointX && tempY == pointY) {
//                didTheyChange = false;
//            }
//
//            if(!didTheyChange) {
//                continue;
//            }
//            path.lineTo(pointX, pointY);
//            canvas.drawPath(path, tealBrush);
//
//            surfaceHolder.unlockCanvasAndPost(canvas);
//
//
//            if((pointX == INITIAL_X) && (pointY == INITIAL_Y)) {
//                canDraw = false;
//                carCommands.setAbsoluteSpeed(0, 0);
//                Toast.makeText(getContext(), "Count Recorded!", Toast.LENGTH_SHORT).show();
//                TextView displayCount = new TextView(getContext());
//                displayCount.setBackgroundColor(Color.WHITE);
//                displayCount.setText(numPeople);
//            }
//        }
//    }
//
//    public void pause() {
//
//        canDraw = false;
//
//        while(true) {
//            try {
//                thread.join();
//                break;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        thread = null;
//    }
//
//    public void resume() {
//        canDraw = true;
//        thread = new Thread(this);
//        thread.start();
//    }
//
//    public void prepBrush() {
//
//        tealBrush = new Paint();
//        tealBrush.setColor(Color.parseColor("#008080"));
//        tealBrush.setStyle(Paint.Style.STROKE);
//        tealBrush.setStrokeWidth(10);
//
//    }
//
//   private void motionPoint() {
//
//       pathCoordinates.trackPathCoordinates(carCommands.getSpeed(), carCommands.getHeading());
//       pointX = pathCoordinates.getX()/5;
//       pointY = pathCoordinates.getY()/5;
//
//   }
//
//}
