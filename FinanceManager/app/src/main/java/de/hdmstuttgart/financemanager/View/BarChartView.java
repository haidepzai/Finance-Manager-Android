package de.hdmstuttgart.financemanager.View;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

    private Paint mPaintBar1;

    private Paint mPaintBar2;

    private Paint mPaintBar3;

    private Paint mPaintBar4;

    private Paint mPaintBar5;

    private Paint mPaintBar6;

    private AnimatableRectF mRect1;
    private AnimatableRectF mRect2;
    private AnimatableRectF mRect3;
    private AnimatableRectF mRect4;
    private AnimatableRectF mRect5;
    private AnimatableRectF mRect6;

    public BarChartView(Context context) {
        super(context);

        init();
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {

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

        /*
         * Berechnung der Balken:
         * Y-Achse auf Höhe 1500 = 0%
         * Y-Achse auf Höhe 500 = 100%
         * Differenz = 1000 Einheiten = 100%
         * 1% = 10E
         * Maximale Y-Höhe: Höchster Kategoriewert + 10%
         * Höhe eines Balkens: ((Wert des Balken geteilt durch höchsten Kategoriewert) * 100(Prozent) * 10E) - 1500
         */

        //top: 1500 = Höhe 0 (Standardwert)
        mRect1 = new AnimatableRectF(90, 1500, 180, 1500);
        ObjectAnimator animate1 = ObjectAnimator.ofFloat(mRect1, "top", mRect1.top, 1500 - ((grocery / maxValue) * 1000)); //...values: Startwert , Endwert
        animate1.addUpdateListener(valueAnimator -> postInvalidate());

        mRect2 = new AnimatableRectF(210, 1500, 300, 1500);
        ObjectAnimator animate2 = ObjectAnimator.ofFloat(mRect2, "top", mRect2.top, 1500 - ((food / maxValue) * 1000));
        animate2.addUpdateListener(valueAnimator -> postInvalidate());

        mRect3 = new AnimatableRectF(330, 1500, 420, 1500);
        ObjectAnimator animate3 = ObjectAnimator.ofFloat(mRect3, "top", mRect3.top, 1500 - ((education / maxValue) * 1000));
        animate3.addUpdateListener(valueAnimator -> postInvalidate());

        mRect4 = new AnimatableRectF(450, 1500, 540, 1500);
        ObjectAnimator animate4 = ObjectAnimator.ofFloat(mRect4, "top", mRect4.top, 1500 - ((leisure / maxValue) * 1000));
        animate4.addUpdateListener(valueAnimator -> postInvalidate());

        mRect5 = new AnimatableRectF(570, 1500, 660, 1500);
        ObjectAnimator animate5 = ObjectAnimator.ofFloat(mRect5, "top", mRect5.top, 1500 - ((fee / maxValue) * 1000));
        animate5.addUpdateListener(valueAnimator -> postInvalidate());

        mRect6 = new AnimatableRectF(690, 1500, 780, 1500);
        ObjectAnimator animate6 = ObjectAnimator.ofFloat(mRect6, "top", mRect6.top, 1500 - ((misc / maxValue) * 1000));
        animate6.addUpdateListener(valueAnimator -> postInvalidate());

        AnimatorSet rectAnimation = new AnimatorSet();
        rectAnimation.playTogether(animate1, animate2, animate3, animate4, animate5, animate6);
        rectAnimation.setDuration(1000).start();


        mPaintBar1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar1.setColor(Color.rgb(25, 191, 0));

        mPaintBar2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar2.setColor(Color.BLUE);

        mPaintBar3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar3.setColor(Color.RED);

        mPaintBar4 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar4.setColor(Color.rgb(0, 194, 201));

        mPaintBar5 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar5.setColor(Color.MAGENTA);

        mPaintBar6 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBar6.setColor(Color.rgb(252, 151, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawRect(mRect1, mPaintBar1);
        canvas.drawRect(mRect2, mPaintBar2);
        canvas.drawRect(mRect3, mPaintBar3);
        canvas.drawRect(mRect4, mPaintBar4);
        canvas.drawRect(mRect5, mPaintBar5);
        canvas.drawRect(mRect6, mPaintBar6);


        //Balkendiagram (X-Y-Achse)
        canvas.drawLine(30, 1500, 1000, 1500, gridPaint); //Horizontal
        //Horizontale Linien
        canvas.drawLine(30, 1000, 1000, 1000, linePaint); //Horizontal
        canvas.drawLine(30, 750, 1000, 750, linePaint); //Horizontal
        canvas.drawLine(30, 500, 1000, 500, linePaint); //Horizontal
        canvas.drawLine(30, 1250, 1000, 1250, linePaint); //Horizontal
    }

    private static class AnimatableRectF extends RectF {

        public AnimatableRectF(float left, float top, float right, float bottom) {
            super(left, top, right, bottom);
        }

        @SuppressWarnings("unused")
        public void setTop(float top) {
            this.top = top;
        }

    }
}
