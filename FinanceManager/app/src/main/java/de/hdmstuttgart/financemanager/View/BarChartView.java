package de.hdmstuttgart.financemanager.View;

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

import de.hdmstuttgart.financemanager.presentation.ui.StatisticFragment;

public class BarChartView extends View {

    private Paint barPaint;
    private Paint gridPaint;
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

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30f);
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
        float leftPadding = 120f;
        float bottomPadding = 60f;
        float topPadding = 100f; // Added top padding to avoid navbar/header overlap
        float chartWidth = width - leftPadding - 40f;
        float chartHeight = height - bottomPadding - topPadding - 40f;
        
        float barWidth = chartWidth / 7;
        float spacing = barWidth / 4;

        // Draw Grid and Labels
        drawGrid(canvas, leftPadding, topPadding, chartHeight, chartWidth);

        // Draw Bars
        drawBar(canvas, 1, (float) grocery, Color.parseColor("#008577"), barWidth, spacing, leftPadding, topPadding, chartHeight);
        drawBar(canvas, 2, (float) food, Color.parseColor("#3F51B5"), barWidth, spacing, leftPadding, topPadding, chartHeight);
        drawBar(canvas, 3, (float) education, Color.RED, barWidth, spacing, leftPadding, topPadding, chartHeight);
        drawBar(canvas, 4, (float) leisure, Color.parseColor("#00BCD4"), barWidth, spacing, leftPadding, topPadding, chartHeight);
        drawBar(canvas, 5, (float) fee, Color.MAGENTA, barWidth, spacing, leftPadding, topPadding, chartHeight);
        drawBar(canvas, 6, (float) misc, Color.parseColor("#FF9800"), barWidth, spacing, leftPadding, topPadding, chartHeight);
    }

    private void drawGrid(Canvas canvas, float leftPadding, float topPadding, float chartHeight, float chartWidth) {
        // Draw Y-Axis lines and values (0, 50%, 100%)
        float[] percentages = {0f, 0.5f, 1f};
        for (float p : percentages) {
            float y = (topPadding + chartHeight) - (p * chartHeight);
            canvas.drawLine(leftPadding, y, leftPadding + chartWidth, y, gridPaint);
            canvas.drawText(String.format(Locale.GERMANY, "%.0f", p * maxValue), leftPadding - 10f, y + 10f, textPaint);
        }
        
        // Draw baseline
        Paint axisPaint = new Paint(gridPaint);
        axisPaint.setPathEffect(null);
        axisPaint.setColor(Color.BLACK);
        canvas.drawLine(leftPadding, topPadding + chartHeight, leftPadding + chartWidth, topPadding + chartHeight, axisPaint);
    }

    private void drawBar(Canvas canvas, int index, float value, int color, float barWidth, float spacing, float leftPadding, float topPadding, float chartHeight) {
        barPaint.setColor(color);
        float left = leftPadding + (index - 1) * (barWidth + spacing) + spacing;
        float barHeight = (value / maxValue * chartHeight) * animationFactor;
        float top = (topPadding + chartHeight) - barHeight;
        float right = left + barWidth;
        float bottom = topPadding + chartHeight;
        canvas.drawRect(left, top, right, bottom, barPaint);
    }
}
