package com.example.pinball;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.color;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	//桌面的宽度
	private int tableWidth;
	//桌面的高度
	private int tableHeight;
	//下面定义球拍的高度和宽度
	private int racketY;
	private final int RACKET_HEIGHT=20;
	private final int RACKET_WIDTH = 70;
	//小球大小
	private final int BALL_SIZE =12;
	//
	private int ySpeed = 10;
	//
	Random rand = new Random();
	//返回一个-0.5～0.5的比率，用于控制小球的运行方向
	private double xyRate = rand.nextDouble()-0.5;
	//小球横向的运行速度
	private int xSpeed = (int)(ySpeed*xyRate*2);
	//ballX和ballY代表小球的坐标
	private int ballX = rand.nextInt(200)+20;
	private int ballY = rand.nextInt(10)+20;
	//racketX代表球拍的水平位置
	private int racketX = rand.nextInt(200);
	//游戏是否结束的旗标
	private boolean isLose = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		final GameView gameview = new GameView(this);
		setContentView(gameview);
		
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		tableWidth = metrics.widthPixels;
		tableHeight = metrics.heightPixels;
		racketY = tableHeight -80;
		
		final Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what==0x123) {
					gameview.invalidate();
				}
			}
			
		};
		
		gameview.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_A:
					if (racketX>0) {
						racketX -=10;
					}
					break;

				case KeyEvent.KEYCODE_D:
					if (racketX<tableWidth-RACKET_WIDTH) {
						racketX +=10;
					}
					break;
				}
				return true;
			}
		});
		
		
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (ballX<=0||ballX>=tableWidth-BALL_SIZE) {
					xSpeed = -xSpeed; 
				}
				
				if (ballY>=racketY-BALL_SIZE&&(ballX<racketX||ballX>racketX+RACKET_WIDTH)) {
					timer.cancel();
					isLose = true;
				}else if (ballY<=0||(ballY>=racketX-BALL_SIZE&&ballX>racketX&&ballX<=racketX+RACKET_WIDTH)) {
					ySpeed = -ySpeed;
				}
				
				ballY +=ySpeed;
				ballX +=xSpeed;
				handler.sendEmptyMessage(0x123);
			}
		}, 0, 100);
		
	}
	
	class GameView extends View{
		   Paint paint = new Paint();
			public GameView(Context context) {
				super(context);
				setFocusable(true);
				}
			@Override
			protected void onDraw(Canvas canvas) {
				// TODO Auto-generated method stub
				super.onDraw(canvas);
				paint.setAntiAlias(true);
				if (isLose) {
					paint.setColor(Color.RED);
					paint.setTextSize(40);
					canvas.drawText("游戏已结束", 50, 200, paint);
				}else {
					paint.setColor(Color.rgb(240, 240, 80));
					canvas.drawCircle(ballX, ballY, BALL_SIZE, paint);
					paint.setColor(Color.rgb(80, 80, 200));
					canvas.drawRect(racketX, racketY, racketX+RACKET_WIDTH, racketY+RACKET_HEIGHT, paint);
				}
			
			
			}
			
		}


}

