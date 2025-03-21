package com.example.task_4_tic_tac_toe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class GameBoardView extends View {
    private Paint paint;
    private int boardColor;
    private int lineColor;
    private float lineWidth;
    private float cellSize;

    public GameBoardView(Context context) {
        super(context);
        init();
    }

    public GameBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        boardColor = Color.WHITE;
        lineColor = getContext().getColor(R.color.primary);
        lineWidth = 4f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cellSize = (float) getWidth() / 3;

        // Draw board background
        paint.setColor(boardColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        // Draw border
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        // Draw grid lines
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);

        // Vertical lines
        canvas.drawLine(cellSize, 0, cellSize, getHeight(), paint);
        canvas.drawLine(cellSize * 2, 0, cellSize * 2, getHeight(), paint);

        // Horizontal lines
        canvas.drawLine(0, cellSize, getWidth(), cellSize, paint);
        canvas.drawLine(0, cellSize * 2, getWidth(), cellSize * 2, paint);
    }
} 