package com.hdmstuttgart.financemanager.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import java.util.Locale;

import com.hdmstuttgart.financemanager.presentation.ui.StatisticFragment;

public class BarChartView extends View {

    private Paint barPaint;
    private Paint gridPaint;
    private Paint axisPaint;
    private Paint textPaint;
    
    private float animationFactor = 0f;
    private float maxValue;
    private double grocery, food, education, leisure, fee, misc;

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(2f);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(3f);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(26f);
        textPaint.setTextAlign(Paint.Align.RIGHT);

        startAnimation();
    }

    public void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            animationFactor = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        maxValue = (float) (StatisticFragment.maxValue * 1.1f);
        grocery = StatisticFragment.totalGrocery;
        food = StatisticFragment.totalFood;
        education = StatisticFragment.totalEducation;
        leisure = StatisticFragment.totalLeisure;
        fee = StatisticFragment.totalFee;
        misc = StatisticFragment.totalMisc;

        if (maxValue == 0) return;

        float width = getWidth();
        float height = getHeight();
        float leftPadding = 100f;
        float bottomPadding = 40f; 
        float topPadding = 100f;
        
        float chartWidth = width - leftPadding - 20f;
        float chartHeight = height - bottomPadding - topPadding;
        
        float baselineY = topPadding + chartHeight;

        // 1. Draw Grid Lines (Dashed) at 25%, 50%, 75%, 100%
        float[] percentages = {0.25f, 0.5f, 0.75f, 1.0f};
        for (float p : percentages) {
            float y = baselineY - (p * chartHeight);
            canvas.drawLine(leftPadding, y, leftPadding + chartWidth, y, gridPaint);
            canvas.drawText(String.format(Locale.GERMANY, "%.0f", p * maxValue), leftPadding - 10f, y + 10f, textPaint);
        }

        // 2. Draw Axes (Solid)
        // X-Axis (Baseline)
        canvas.drawLine(leftPadding, baselineY, leftPadding + chartWidth, baselineY, axisPaint);
        // Y-Axis
        canvas.drawLine(leftPadding, topPadding, leftPadding, baselineY, axisPaint);

        // 3. Draw Bars
        float barWidth = chartWidth / 7;
        float spacing = barWidth / 4;

        drawBar(canvas, 1, (float) grocery, Color.parseColor("#008577"), barWidth, spacing, leftPadding, baselineY, chartHeight);
        drawBar(canvas, 2, (float) food, Color.parseColor("#3F51B5"), barWidth, spacing, leftPadding, baselineY, chartHeight);
        drawBar(canvas, 3, (float) education, Color.RED, barWidth, spacing, leftPadding, baselineY, chartHeight);
        drawBar(canvas, 4, (float) leisure, Color.parseColor("#00BCD4"), barWidth, spacing, leftPadding, baselineY, chartHeight);
        drawBar(canvas, 5, (float) fee, Color.MAGENTA, barWidth, spacing, leftPadding, baselineY, chartHeight);
        drawBar(canvas, 6, (float) misc, Color.parseColor("#FF9800"), barWidth, spacing, leftPadding, baselineY, chartHeight);
    }

    private void drawBar(Canvas canvas, int index, float value, int color, float barWidth, float spacing, float leftPadding, float baselineY, float chartHeight) {
        barPaint.setColor(color);
        float left = leftPadding + (index - 1) * (barWidth + spacing) + spacing;
        float barHeight = (value / maxValue * chartHeight) * animationFactor;
        float top = baselineY - barHeight;
        float right = left + barWidth;
        float bottom = baselineY;
        canvas.drawRect(left, top, right, bottom, barPaint);
    }
}
