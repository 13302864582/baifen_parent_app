package com.daxiong.fun.function.homework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.daxiong.fun.util.DensityUtil;

public class CoordinateSystem extends View {
	public int XPoint = 40; // 原点的X坐标
	public int YPoint = 200; // 原点的Y坐标
	public int scale = 30; // 大格
	public int sScale ; //小格
//	public int YScale = 50; // Y的刻度长度
//	public int XLength = 440; // X轴的长度
//	public int YLength = 280; // Y轴的长度
	public int numSize = 8;
	public int textSize = 11;
	public String[] XLabel = new String[]{}; // X的刻度
	public String[] YLabel = new String[]{}; // Y的刻度
	public String[] Data; // 数据
	public int [] wrongNums;
	private Context mContext;

	public CoordinateSystem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public CoordinateSystem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CoordinateSystem(Context context) {
		super(context);
		init(context);
	}

	private int tran(float dp) {
		return DensityUtil.dip2px(mContext, dp);
	}
	private void init(Context context) {
		this.mContext = context;
		XPoint = tran(XPoint);
		YPoint = tran(YPoint);
		scale = tran(scale);
		numSize = tran(numSize);
		textSize = tran(textSize);
		sScale = scale / 5;
//		YScale = tran(YScale);
		
//		XLength = DensityUtil.dip2px(mContext, XLength);
//		YLength = DensityUtil.dip2px(mContext, YLength);
	}
	public void SetInfo(String[] XLabels, String[] YLabels, String[] AllData, int [] wrongNums) {
		XLabel = XLabels;
		YLabel = YLabels;
		Data = AllData;
		this.wrongNums = wrongNums;
		this.invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);// 重写onDraw方法

		Paint XYPaint = new Paint();
		XYPaint.setStyle(Paint.Style.STROKE);
		XYPaint.setAntiAlias(true);// 去锯齿
		XYPaint.setColor(Color.BLACK);// 颜色
		
		Paint circlePaint = new Paint();
		circlePaint.setStyle(Paint.Style.FILL);
		circlePaint.setAntiAlias(true);// 去锯齿
		circlePaint.setColor(Color.RED);
		
		Paint dataPaint = new Paint();
		dataPaint.setStyle(Paint.Style.STROKE);
		dataPaint.setAntiAlias(true);// 去锯齿
		dataPaint.setColor(Color.BLACK);// 颜色
		dataPaint.setStrokeWidth(tran(1));
		
		XYPaint.setTextSize(numSize); // 设置轴文字大小
		
		int ySize = YLabel.length;
		int yLen = (ySize - 1) * 5;
		// 设置Y轴
		canvas.drawLine(XPoint, YPoint, XPoint, scale , XYPaint); // 轴线
		for (int i = 0; i <= yLen; i++) {
			int hei = 2;
			//int j = i + 1;
			if ((i % 5) == 0) {
				hei = 5;
				int index = i / 5;
				canvas.drawText(YLabel[index], XPoint - tran(15), YPoint - index * scale + tran(2), XYPaint); // 文字
			}
			canvas.drawLine(XPoint, YPoint - i * sScale, XPoint + tran(hei), YPoint - i * sScale, XYPaint); // 刻度
		}
		XYPaint.setTextSize(textSize);
		canvas.drawText("错题数", XPoint - scale / 2, scale /2 + tran(5), XYPaint);

		int xSize = XLabel.length;
		int xLen = (xSize - 1) * 5;
		// 设置X轴
		canvas.drawLine(XPoint, YPoint, XPoint + (xSize + 1) * scale, YPoint, XYPaint); // 轴线
		XYPaint.setTextSize(numSize); // 设置轴文字大小

		int lastX = 0;
		int lastY = 0;
		for (int i = 0; i <= xLen; i++) {
			int hei = 2;
			//int j = i + 1;
			if ((i % 5) == 0) {
				hei = 5;
				int index = i / 5;
				canvas.drawText(XLabel[index], XPoint + i * sScale - tran(XLabel[index].length() * 2), YPoint + tran(15), XYPaint); // 文字
			}
			canvas.drawLine(XPoint + i * sScale, YPoint, XPoint + i * sScale, YPoint - tran(hei), XYPaint); // 刻度
			if (i > 0 && i <= wrongNums.length) {
				int wrongNum = wrongNums[i - 1];
				if (wrongNum > 25) {
					wrongNum = 25;
				}
				int nowX = XPoint + i * sScale;
				int nowY = YPoint - wrongNum * sScale;
				canvas.drawCircle(nowX, nowY, tran(2), circlePaint);
				
				if (lastX != 0 && lastY != 0) {
					canvas.drawLine(lastX, lastY, nowX, nowY, dataPaint);
				}

				lastX = nowX;
				lastY = nowY;
			}
//			try {
//				// 数据值
//				if (i > 0 && YCoord(Data[i - 1]) != -999 && YCoord(Data[i]) != -999) // 保证有效数据
//					
//				canvas.drawCircle(XPoint + i * scale, YCoord(Data[i]), tran(2), paint1);
//			} catch (Exception e) {
//			}
		}
		XYPaint.setTextSize(textSize);
		canvas.drawText("作业次数", XPoint + (xSize ) * scale - tran(5), YPoint + tran(15), XYPaint);
	}

}