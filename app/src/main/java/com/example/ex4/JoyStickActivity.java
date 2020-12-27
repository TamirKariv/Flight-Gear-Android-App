package com.example.ex4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

//Joystick Activity
public class JoyStickActivity extends AppCompatActivity {
    private Client client;

//onCreate event
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joy_stick);
        Intent intent = getIntent();
        this.client = new Client();
        //connect to the simulator
        client.execute(intent.getStringExtra("IP"), intent.getStringExtra("Port"), null);
        //setup the joystickView
        JoyStickView joyStickView = new JoyStickView(this);
        setContentView(joyStickView);
    }


    //Joystick View inner class
    public class JoyStickView extends View {
        private Paint outerCircle = new Paint();
        private Paint innerCircle = new Paint();
        private Paint background = new Paint();
        private float curX;
        private float curY;
        private float r =70;
        private float widthBegin;
        private float widthEnd;
        private float heightBegin;
        private float heightEnd;
        private RectF rect;
        private boolean stopMovement = true;


        //constrictor
        public JoyStickView(Context context) {
            super(context);
        }

        //onDraw event
        protected void onDraw(Canvas canvas) {

            super.onDraw(canvas);
           //setup background and circles
            background = new Paint(Paint.ANTI_ALIAS_FLAG);
            background.setColor(Color.BLUE);
            background.setStyle(Paint.Style.FILL);

            outerCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
            outerCircle.setColor(Color.GRAY);
            outerCircle.setStyle(Paint.Style.FILL);


            innerCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
            innerCircle.setColor(Color.GREEN);
            innerCircle.setStyle(Paint.Style.FILL);

            canvas.drawRect(0,0,getWidth(),getHeight(),background);
            canvas.drawOval(rect, outerCircle);
            canvas.drawCircle(curX, curY, r, innerCircle);

        }
        //onSizeChanged event
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            //update the width,height and rect
            widthBegin = (float)getWidth()/8;
            widthEnd = (float)getWidth()-widthBegin;
            heightBegin = (float)getHeight()/8;
            heightEnd = getHeight()-heightBegin;
            rect = new RectF(widthBegin, heightBegin, widthEnd, heightEnd);
            MoveBackToCenter();
        }
        //move the joystick back to the center and reset the simulator values
        public void MoveBackToCenter() {
            curX = (float)getWidth()/2;
            curY = (float)getHeight()/2;
            SendCommandToSimulator(curX, curY);
        }

        //onTouchEvent
        public boolean onTouchEvent(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            //check for limits move accordingly to the touch input
            if(e.getAction()==MotionEvent.ACTION_DOWN&&
                    Math.sqrt((curX -x)*(curX -x) + (curY -y)*(curY -y))<=r){
                stopMovement = false;
            }
            else if(e.getAction()==MotionEvent.ACTION_MOVE && IsInside(x,y)&& !stopMovement){
                curX = x;
                curY = y;
                SendCommandToSimulator(x, y);
                invalidate();

            }
            else if (e.getAction()== MotionEvent.ACTION_UP){
                stopMovement = true;
                MoveBackToCenter();
                invalidate();

            }
            return true;
        }

        //check if the joystick is inside the limits
        public boolean IsInside(float x, float y) {

            if(!this.rect.contains(x, y)){
                return false;
            }
            if(!this.rect.contains(x+ r, y)){
                return false;
            }
            if(!this.rect.contains(x, y+ r)){
                return false;
            }
            if(!this.rect.contains(x- r, y)){
                return false;
            }
            if(!this.rect.contains(x, y- r)){
                return false;
            }

            return true;
        }

        //send a command from the joystick to the simulator
        public void SendCommandToSimulator(float x,float y){
            //values are normalized
            x = (x-((widthBegin +widthEnd)/2))/((widthEnd -widthBegin)/2);
            y = (y-((this.heightBegin +this.heightEnd)/2))/((this.heightBegin -this.heightEnd)/2);
            //send the command
            JoyStickActivity.this.client.AddCommandToQueue("set /controls/flight/aileron " + x);
            JoyStickActivity.this.client.AddCommandToQueue("set /controls/flight/elevator "+ y);

        }

    }

}
