package de.hdmstuttgart.financemanager.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import de.hdmstuttgart.financemanager.Fragments.StatisticFragment;

public class BarChartView extends View {

    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();

    public static float maxValue; //Der größter Wert unter den Kategorien
    public static float totalAmount; //Gesamte Summe
    public static float grocery;
    public static float food;
    public static float education;
    public static float leisure;
    public static float fee;
    public static float misc;

    private RectF mBar1; //Einkauf
    private Paint mPaintBar1;

    private RectF mBar2; //Essen
    private Paint mPaintBar2;

    private RectF mBar3; //Studium/Beruf
    private Paint mPaintBar3;

    private RectF mBar4; //Freizeit
    private Paint mPaintBar4;

    private RectF mBar5; //Gebühren
    private Paint mPaintBar5;

    private RectF mBar6; //Sonstige
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

    private void init(@Nullable AttributeSet set) {

        maxValue = StatisticFragment.maxValue + (StatisticFragment.maxValue*0.1f);
        totalAmount = StatisticFragment.totalAmount;
        grocery = StatisticFragment.totalGrocery;
        food = StatisticFragment.totalFood;
        education = StatisticFragment.totalEducation;
        leisure = StatisticFragment.totalLeisure;
        fee = StatisticFragment.totalFee;
        misc = StatisticFragment.totalMisc;
        Log.d("maxAmount", String.valueOf(totalAmount));
        Log.d("grocery", String.valueOf(grocery));
        Log.d("food", String.valueOf(food));
        Log.d("education", String.valueOf(education));
        Log.d("leisure", String.valueOf(leisure));
        Log.d("fee", String.valueOf(fee));
        Log.d("misc", String.valueOf(misc));


        gridPaint.setColor(Color.BLACK);
        gridPaint.setStrokeWidth(5);

        linePaint.setColor(Color.DKGRAY);

        mBar1 = new RectF();
        mPaintBar1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar1.setColor(Color.GREEN);

        mBar2 = new RectF();
        mPaintBar2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar2.setColor(Color.BLUE);

        mBar3 = new RectF();
        mPaintBar3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar3.setColor(Color.RED);

        mBar4 = new RectF();
        mPaintBar4 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar4.setColor(Color.CYAN);

        mBar5 = new RectF();
        mPaintBar5 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar5.setColor(Color.MAGENTA);

        mBar6 = new RectF();
        mPaintBar6 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar6.setColor(Color.YELLOW);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        drawBar(canvas);

        //Balkendiagram (X-Y-Achse)
        canvas.drawLine(30, 1500, 30, 500, gridPaint); //Vertikal
        canvas.drawLine(30, 1500, 1000, 1500, gridPaint); //Horizontal
        //Horizontale Linien
        canvas.drawLine(30, 1000, 1000, 1000, linePaint); //Horizontal
        canvas.drawLine(30, 750, 1000, 750, linePaint); //Horizontal
        canvas.drawLine(30, 500, 1000, 500, linePaint); //Horizontal
        canvas.drawLine(30, 1250, 1000, 1250, linePaint); //Horizontal
    }

    private void drawBar(Canvas canvas) {
        mBar1.left = 60; //Abstand links
        mBar1.top = 1500 - ((grocery / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar1.right = 150; //dicke (+90 zu left)
        mBar1.bottom = 1500; //Start
        Log.d("Groceryyy", String.valueOf(grocery));
        Log.d("Maxxxxxxxxxx", String.valueOf(totalAmount));
        Log.d("Mbaaaaaaaar", String.valueOf(mBar1.top));

        canvas.drawRect(mBar1, mPaintBar1);

        mBar2.left = 180; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar2.top = 1500 - ((food / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar2.right = 270; //dicke (+90 zu left)
        mBar2.bottom = 1500; //Start

        canvas.drawRect(mBar2, mPaintBar2);

        mBar3.left = 300; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar3.top = 1500 - ((education / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar3.right = 390; //dicke (+90 zu left)
        mBar3.bottom = 1500; //Start

        canvas.drawRect(mBar3, mPaintBar3);

        mBar4.left = 420; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar4.top = 1500 - ((leisure / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar4.right = 510; //dicke (+90 zu left)
        mBar4.bottom = 1500; //Start

        canvas.drawRect(mBar4, mPaintBar4);

        mBar5.left = 540; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar5.top = 1500 - ((fee / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar5.right = 630; //dicke (+90 zu left)
        mBar5.bottom = 1500; //Start

        canvas.drawRect(mBar5, mPaintBar5);

        mBar6.left = 670; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar6.top = 1500 - ((misc / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar6.right = 760; //dicke (+90 zu left)
        mBar6.bottom = 1500; //Start

        canvas.drawRect(mBar6, mPaintBar6);
    }

    private void drawBars(Canvas canvas) {
        mBar1.left = 60; //Abstand links
        mBar1.top = 1500; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar1.right = 150; //dicke (+90 zu left)
        mBar1.bottom = 1500; //Start

        canvas.drawRect(mBar1, mPaintBar1);

        mBar2.left = 180; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar2.top = 1500; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar2.right = 270; //dicke (+90 zu left)
        mBar2.bottom = 1500; //Start

        canvas.drawRect(mBar2, mPaintBar2);

        mBar3.left = 300; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar3.top = 1500; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar3.right = 390; //dicke (+90 zu left)
        mBar3.bottom = 1500; //Start

        canvas.drawRect(mBar3, mPaintBar3);

        mBar4.left = 420; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar4.top = 1500; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar4.right = 510; //dicke (+90 zu left)
        mBar4.bottom = 1500; //Start

        canvas.drawRect(mBar4, mPaintBar4);

        mBar5.left = 540; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar5.top = 1500; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar5.right = 630; //dicke (+90 zu left)
        mBar5.bottom = 1500; //Start

        canvas.drawRect(mBar5, mPaintBar5);

        mBar6.left = 670; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar6.top = 1500; //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar6.right = 760; //dicke (+90 zu left)
        mBar6.bottom = 1500; //Start

        canvas.drawRect(mBar6, mPaintBar6);
    }
}
