package com.szugyi.circlemenusample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Main2Activity extends Activity {
    ImageView imageView;
    ImageView dragHandle;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView = (ImageView) findViewById(R.id.imageView1);
        imageView.setBackgroundColor(Color.MAGENTA);
        dragHandle = (ImageView) findViewById(R.id.imageView2);
        dragHandle.setBackgroundColor(Color.CYAN);
        layout = (RelativeLayout) findViewById(R.id.relativeLayout2);
        layout.setBackgroundColor(Color.YELLOW);
        setUpResize();

    }

    public void setUpResize() {
        dragHandle.setOnTouchListener(new View.OnTouchListener() {

            float centerX, centerY, startR, startScale, startX, startY;

            float startAngle;
            float zeroAngle;
            int firstPointX;
            int firstPointY;

            public boolean onTouch(View v, MotionEvent e) {

                if (e.getAction() == MotionEvent.ACTION_DOWN) {

                    // calculate center of image
                    centerX = (imageView.getLeft() + imageView.getRight()) / 2f;
                    centerY = (imageView.getTop() + imageView.getBottom()) / 2f;

                    // recalculate coordinates of starting point
                    startX = e.getRawX() - dragHandle.getX() + centerX;
                    startY = e.getRawY() - dragHandle.getY() + centerY;

                    // get starting distance and scale
                    startR = (float) Math.hypot(e.getRawX() - startX, e.getRawY() - startY);
                    startScale = imageView.getScaleX();

                    /*
                     * Rotate code
                     */

                    int[] locationOfLayout = new int[2];
                    int[] locationOfDrag = new int[2];

                    layout.getLocationOnScreen(locationOfLayout);
                    dragHandle.getLocationOnScreen(locationOfDrag);

                    firstPointX = locationOfLayout[0];
                    firstPointY = locationOfLayout[1];

                    float secondPointX = e.getRawX();
                    float secondPointY = e.getRawY();

                    zeroAngle = findRotation(firstPointX, firstPointY, secondPointX, secondPointY); // remember
                    // "zero"
                    // angle
                    startAngle = layout.getRotation(); // remember angle at
                    // which layout is
                    // rotated at the start

                } else if (e.getAction() == MotionEvent.ACTION_MOVE) {

                    // calculate new distance
                    float newR = (float) Math.hypot(e.getRawX() - startX, e.getRawY() - startY);

                    // set new scale
                    float newScale = newR / startR * startScale;
                    imageView.setScaleX(newScale);
                    imageView.setScaleY(newScale);

                    // move handler image
                    dragHandle.setX(centerX + imageView.getWidth() / 2f * newScale);
                    dragHandle.setY(centerY + imageView.getHeight() / 2f * newScale);

                    /*
                     * Rotate code
                     */

                    layout.setRotation(findRotation(firstPointX, firstPointY, e.getRawX(), e.getRawY()) - zeroAngle
                            + startAngle); // rotate relative to start and zero
                    // angle

                } else if (e.getAction() == MotionEvent.ACTION_UP) {

                }
                return true;
            }
        });
    }

    private float findRotation(float firstPointX, float firstPointY, float secondPointX, float secondPointY) {
        double delta_x = (secondPointX - firstPointX);
        double delta_y = (secondPointY - firstPointY);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}