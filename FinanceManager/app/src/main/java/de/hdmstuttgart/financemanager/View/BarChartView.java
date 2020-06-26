package de.hdmstuttgart.financemanager.View;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

    private AnimatableRectF mRect1;
    private AnimatableRectF mRect2;
    private AnimatableRectF mRect3;
    private AnimatableRectF mRect4;
    private AnimatableRectF mRect5;
    private AnimatableRectF mRect6;

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

        maxValue = StatisticFragment.maxValue + (StatisticFragment.maxValue * 0.1f);
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

        mRect1 = new AnimatableRectF(90, 1500, 180, 1500);          //Startwert , Endwert
        ObjectAnimator animate1 = ObjectAnimator.ofFloat(mRect1, "top", mRect1.top, 1500 - ((grocery / maxValue) * 1000));
        animate1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                postInvalidate();
            }
        });

        mRect2 = new AnimatableRectF(210, 1500, 300, 1500);
        ObjectAnimator animate2 = ObjectAnimator.ofFloat(mRect2, "top", mRect2.top, 1500 - ((food / maxValue) * 1000));
        animate2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                postInvalidate();
            }
        });

        mRect3 = new AnimatableRectF(330, 1500, 420, 1500);
        ObjectAnimator animate3 = ObjectAnimator.ofFloat(mRect3, "top", mRect3.top, 1500 - ((education / maxValue) * 1000));
        animate3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                postInvalidate();
            }
        });

        mRect4 = new AnimatableRectF(450, 1500, 540, 1500);
        ObjectAnimator animate4 = ObjectAnimator.ofFloat(mRect4, "top", mRect4.top, 1500 - ((leisure / maxValue) * 1000));
        animate4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                postInvalidate();
            }
        });

        mRect5 = new AnimatableRectF(570, 1500, 660, 1500);
        ObjectAnimator animate5 = ObjectAnimator.ofFloat(mRect5, "top", mRect5.top, 1500 - ((fee / maxValue) * 1000));
        animate5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                postInvalidate();
            }
        });

        mRect6 = new AnimatableRectF(690, 1500, 780, 1500);
        ObjectAnimator animate6 = ObjectAnimator.ofFloat(mRect6, "top", mRect6.top, 1500 - ((misc / maxValue) * 1000));
        animate6.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                postInvalidate();
            }
        });

        AnimatorSet rectAnimation = new AnimatorSet();
        rectAnimation.playTogether(animate1, animate2, animate3, animate4, animate5, animate6);
        rectAnimation.setDuration(1000).start();


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

        canvas.drawRect(mRect1, mPaintBar1);
        canvas.drawRect(mRect2, mPaintBar2);
        canvas.drawRect(mRect3, mPaintBar3);
        canvas.drawRect(mRect4, mPaintBar4);
        canvas.drawRect(mRect5, mPaintBar5);
        canvas.drawRect(mRect6, mPaintBar6);

        //drawBar(canvas);

        //Balkendiagram (X-Y-Achse)
        //canvas.drawLine(30, 1500, 30, 500, gridPaint); //Vertikal
        canvas.drawLine(30, 1500, 1000, 1500, gridPaint); //Horizontal
        //Horizontale Linien
        canvas.drawLine(30, 1000, 1000, 1000, linePaint); //Horizontal
        canvas.drawLine(30, 750, 1000, 750, linePaint); //Horizontal
        canvas.drawLine(30, 500, 1000, 500, linePaint); //Horizontal
        canvas.drawLine(30, 1250, 1000, 1250, linePaint); //Horizontal
    }

    /**
     * Formel für die Berechnung der Höhe:
     * Y-Achse auf Höhe 1500 = 0%
     * Y-Achse auf Höhe 500 = 100%
     * Differenz = 1000 Einheiten = 100%
     * 1% = 10E
     * Maximale Y-Höhe: Höchster Kategoriewert + 10%
     * Höhe eines Balkens: ((Wert des Balken geteilt durch höchsten Kategoriewert) * 100(Prozent) * 10E) - 1500
     */

    private void drawBar(Canvas canvas) {
        mBar1.left = 90; //Abstand links
        mBar1.top = 1500 - ((grocery / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar1.right = 180; //dicke (+90 zu left)
        mBar1.bottom = 1500; //Start
        Log.d("Grocery", String.valueOf(grocery));
        Log.d("TotalCost", String.valueOf(totalAmount));
        Log.d("Mbar", String.valueOf(mBar1.top));

        canvas.drawRect(mBar1, mPaintBar1);

        mBar2.left = 210; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar2.top = 1500 - ((food / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar2.right = 300; //dicke (+90 zu left)
        mBar2.bottom = 1500; //Start

        canvas.drawRect(mBar2, mPaintBar2);

        mBar3.left = 330; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar3.top = 1500 - ((education / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar3.right = 420; //dicke (+90 zu left)
        mBar3.bottom = 1500; //Start

        canvas.drawRect(mBar3, mPaintBar3);

        mBar4.left = 450; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar4.top = 1500 - ((leisure / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar4.right = 540; //dicke (+90 zu left)
        mBar4.bottom = 1500; //Start

        canvas.drawRect(mBar4, mPaintBar4);

        mBar5.left = 570; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar5.top = 1500 - ((fee / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar5.right = 660; //dicke (+90 zu left)
        mBar5.bottom = 1500; //Start

        canvas.drawRect(mBar5, mPaintBar5);

        mBar6.left = 690; //Abstand links (+30 zu right von dem vorigen Balken)
        mBar6.top = 1500 - ((misc / maxValue) * 1000); //Höhe (500 = 100%; 1500 = 0%; 1000 = 50%)
        mBar6.right = 780; //dicke (+90 zu left)
        mBar6.bottom = 1500; //Start

        canvas.drawRect(mBar6, mPaintBar6);
    }


    private class AnimatableRectF extends RectF {
        public AnimatableRectF() {
            super();
        }

        public AnimatableRectF(float left, float top, float right, float bottom) {
            super(left, top, right, bottom);
        }

        public AnimatableRectF(RectF r) {
            super(r);
        }

        public AnimatableRectF(Rect r) {
            super(r);
        }

        public void setTop(float top) {
            this.top = top;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }

        public void setRight(float right) {
            this.right = right;
        }

        public void setLeft(float left) {
            this.left = left;
        }

    }
}
