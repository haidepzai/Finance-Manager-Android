package de.hdmstuttgart.financemanager.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class StatisticView extends View {

    private static final int SQUARE_SIZE = 500;

    private Rect mRectSquare;
    private Paint mPaintSquare;

    public StatisticView(Context context) {
        super(context);

        init(null);
    }

    public StatisticView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public StatisticView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public StatisticView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        mRectSquare = new Rect();
        mPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSquare.setColor(Color.BLACK);
        mPaintSquare.setStrokeWidth(5);
    }

    public void swapColor(){
        mPaintSquare.setColor(mPaintSquare.getColor() == Color.GREEN ? Color.RED : Color.GREEN);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){

        canvas.drawLine(30, 1200, 30, 200, mPaintSquare); //Vertikal
        canvas.drawLine(30, 1200, 1030, 1200, mPaintSquare); //Horizontal

        mRectSquare.left = 300;
        mRectSquare.top = 500;
        mRectSquare.right = mRectSquare.left + SQUARE_SIZE;
        mRectSquare.bottom = mRectSquare.top + SQUARE_SIZE;

        canvas.drawRect(mRectSquare, mPaintSquare);
    }
}
