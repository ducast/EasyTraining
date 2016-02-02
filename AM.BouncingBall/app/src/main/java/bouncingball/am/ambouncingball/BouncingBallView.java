package bouncingball.am.ambouncingball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sbvb on 14/04/15.
 */
public class BouncingBallView extends View {

    private int xMin = 0; // This view's bounds
    private int xMax;
    private int yMin = 0;
    private int yMax;
    private float ballRadius = 30; // Ball's radius
    private float ballX; // Ball's center (x,y)
    private float ballY;
    private float ballSpeedX = 3; // Ball's speed (x,y)
    private float ballSpeedY = 6;
    private RectF ballBounds; // Needed for Canvas.drawOval
    private Paint paint; // The paint (e.g. style, color) used for drawing
    private int sleepMs = 0;
    private RectF obstacle;
    private Paint obstaclePaint;
    private float obstacleX = 400;
    private float obstacleY = 600;
    private float obstacleSide = 150;


    // Constructor
    public BouncingBallView(Context context) {
        super(context);
        ballBounds = new RectF();
        paint = new Paint();
        paint.setColor(Color.GREEN);
        obstacle = new RectF();
        obstaclePaint = new Paint();
        obstaclePaint.setColor(Color.GRAY);

    }

    // Called back to draw the view. Also called by invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the ball
        ballBounds.set(ballX - ballRadius, ballY - ballRadius, ballX
                + ballRadius, ballY + ballRadius);

        canvas.drawOval(ballBounds, paint);

        obstacle.set(obstacleX - obstacleSide, obstacleY - obstacleSide, obstacleX + obstacleSide, obstacleY + obstacleSide);
        canvas.drawRect(obstacle, obstaclePaint);

        // canvas.drawLine(startX, startY, stopX, stopY, paint)

//		// triangle points
//		int x [] = {0, 100, 200, 0};
//		int y [] = {0, 100, 0, 0};
//
//		Paint trianglePaint = new Paint();
//		trianglePaint.setColor(Color.GREEN);
//		trianglePaint.setStyle(Style.FILL);
//
//		Path trianglePath = new Path();
//		trianglePath.reset(); // only needed when reusing this path for a new build
//		trianglePath.moveTo(x[0], y[0]); // used for first point
//		trianglePath.lineTo(x[1], y[1]);
//		trianglePath.lineTo(x[2], y[2]);
//		trianglePath.lineTo(x[3], y[3]);
//		trianglePath.lineTo(x[0], y[0]); // there is a setLastPoint action but i
//										// found it not to work as expected
//		canvas.drawPath(trianglePath, trianglePaint);

        // Update the position of the ball, including collision detection and
        // reaction.
        update();

        // Delay
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException e) {
        }

        invalidate(); // Force a re-draw, call update method
    }

    // Detect collision and update the position of the ball.
    private boolean update() {
        // Get new (x,y) position
        ballX += ballSpeedX;
        ballY += ballSpeedY;
        // Detect collision and react
        if (ballX + ballRadius > xMax) {
            ballSpeedX = -ballSpeedX;
            ballX = xMax - ballRadius;
            paint.setColor(Color.BLUE);
        } else if (ballX - ballRadius < xMin) {
            ballSpeedX = -ballSpeedX;
            ballX = xMin + ballRadius;
            paint.setColor(Color.RED);
        }
        if (ballY + ballRadius > yMax) {
            ballSpeedY = -ballSpeedY;
            ballY = yMax - ballRadius;
            paint.setColor(Color.YELLOW);
        } else if (ballY - ballRadius < yMin) {
            ballSpeedY = -ballSpeedY;
            ballY = yMin + ballRadius;
            paint.setColor(Color.GREEN);
        }

        // Detect ball-rectangle collision
        if (ballX + ballRadius > obstacleX - obstacleSide
                && ballX < obstacleX + obstacleSide
                && ballY > obstacleY - obstacleSide
                && ballY < obstacleY + obstacleSide) {
            ballSpeedX = -ballSpeedX;
            ballX = obstacleX - obstacleSide - ballRadius;
            paint.setColor(Color.GRAY);
            return true;
        }
        else if (ballX - ballRadius < obstacleX + obstacleSide
                && ballX > obstacleX - obstacleSide
                && ballY > obstacleY - obstacleSide
                && ballY < obstacleY + obstacleSide) {
            ballSpeedX = -ballSpeedX;
            ballX = obstacleX + obstacleSide + ballRadius;
            paint.setColor(Color.GRAY);
            return true;
        }
        if (ballY - ballRadius < obstacleY + obstacleSide
                && ballY > obstacleY - obstacleSide
                && ballX > obstacleX - obstacleSide
                && ballX < obstacleX + obstacleSide) {
            ballSpeedY = -ballSpeedY;
            ballY = obstacleY + obstacleSide + ballRadius;
            paint.setColor(Color.GRAY);
            return true;
        }
        else if (ballY + ballRadius > obstacleY - obstacleSide
                    && ballY < obstacleY + obstacleSide
                    && ballX > obstacleX - obstacleSide
                    && ballX < obstacleX + obstacleSide) {
            ballSpeedY = -ballSpeedY;
            ballY = obstacleY - obstacleSide - ballRadius;
            paint.setColor(Color.GRAY);
            return true;
        }

    return false;


    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        xMax = w - 1;
        yMax = h - 1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        obstacleX = e.getX();
        obstacleY = e.getY();
        return true;
    }

}
