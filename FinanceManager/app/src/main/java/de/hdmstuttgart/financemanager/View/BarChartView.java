package de.hdmstuttgart.financemanager.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BarChartView extends View {

    private Paint gridPaint = new Paint();

    private Rect mBar1;
    private Paint mPaintBar1;

    private Rect mBar2;
    private Paint mPaintBar2;

    private Rect mBar3;
    private Paint mPaintBar3;

    private Rect mBar4;
    private Paint mPaintBar4;

    private Rect mBar5;
    private Paint mPaintBar5;

    private Rect mBar6;
    private Paint mPaintBar6;

    public BarChartView(Context context) {
        super(context);

        init(null);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        gridPaint.setColor(Color.BLACK);
        gridPaint.setStrokeWidth(5);

        mBar1 = new Rect();
        mPaintBar1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar1.setColor(Color.GREEN);

        mBar2 = new Rect();
        mPaintBar2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar2.setColor(Color.BLUE);

        mBar3 = new Rect();
        mPaintBar3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar3.setColor(Color.RED);

        mBar4 = new Rect();
        mPaintBar4 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar4.setColor(Color.CYAN);

        mBar5 = new Rect();
        mPaintBar5 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar5.setColor(Color.MAGENTA);

        mBar6 = new Rect();
        mPaintBar6 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar6.setColor(Color.YELLOW);
    }


    @Override
    protected void onDraw(Canvas canvas){

        mBar1.left = 60; //Abstand links
        mBar1.top = 1000; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar1.right = 150; //dicke (+90 zu left)
        mBar1.bottom = 1500; //Start

        canvas.drawRect(mBar1, mPaintBar1);

        mBar2.left = 180; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar2.top = 1200; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar2.right = 270; //dicke (+90 zu left)
        mBar2.bottom = 1500; //Start

        canvas.drawRect(mBar2, mPaintBar2);

        mBar3.left = 300; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar3.top = 750; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar3.right = 390; //dicke (+90 zu left)
        mBar3.bottom = 1500; //Start

        canvas.drawRect(mBar3, mPaintBar3);

        mBar4.left = 420; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar4.top = 900; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar4.right = 510; //dicke (+90 zu left)
        mBar4.bottom = 1500; //Start

        canvas.drawRect(mBar4, mPaintBar4);

        mBar5.left = 540; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar5.top = 600; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar5.right = 630; //dicke (+90 zu left)
        mBar5.bottom = 1500; //Start

        canvas.drawRect(mBar5, mPaintBar5);

        mBar6.left = 670; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar6.top = 1400; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar6.right = 760; //dicke (+90 zu left)
        mBar6.bottom = 1500; //Start

        canvas.drawRect(mBar6, mPaintBar6);

        //Balkendiagram (X-Y-Achse)
        canvas.drawLine(30, 1500, 30, 500, gridPaint); //Vertikal
        canvas.drawLine(30, 1500, 1000, 1500, gridPaint); //Horizontal
    }
}
