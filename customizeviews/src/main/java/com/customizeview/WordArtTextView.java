package com.customizeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by liubin on 2017/1/17.
 */

public class WordArtTextView extends TextView {
    private Paint mPaint;
    private TextPaint mStrokePaint;

    //渐变
    private int startColor;
    private int endColor;
    private float radioHeight;  //渐变终止点相对高度的比例
    private float radioWith;  //渐变终止点相对宽度的比例

    //描边
    private int strokeColor;
    private float strokeWidth;

    public WordArtTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public WordArtTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.WordArtText);
        startColor = typeArray.getColor(R.styleable.WordArtText_startColor, 0);
        endColor = typeArray.getColor(R.styleable.WordArtText_endColor, 0);
        strokeColor = typeArray.getColor(R.styleable.WordArtText_strokeColor, 0);
        radioHeight = typeArray.getFloat(R.styleable.WordArtText_radioHeight, 1);
        radioWith = typeArray.getFloat(R.styleable.WordArtText_radioWith, 0);
        strokeWidth = typeArray.getDimension(R.styleable.WordArtText_strokeWidth, 1f);
        typeArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Rect textBound = new Rect();
        mPaint = getPaint();
        String tipText = getText().toString();
        mPaint.getTextBounds(tipText, 0, tipText.length(), textBound);
        int vector2Baseline = textBound.bottom/2 + textBound.top/2;

        if (0 != startColor && 0 != endColor) {
            gradient();
            canvas.drawText(tipText, getMeasuredWidth() / 2 - textBound.width() / 2, getMeasuredHeight() / 2 - vector2Baseline, mPaint);
        }

        if (0 != strokeColor) {
            stroke();
            canvas.drawText(tipText, getMeasuredWidth() / 2 - textBound.width() / 2, getMeasuredHeight() / 2 - vector2Baseline, mStrokePaint);
        }
    }

    private void gradient() {
        float radioHeight = getMeasuredHeight() * this.radioHeight;
        float radioWith = getMeasuredWidth() * this.radioWith;
        LinearGradient linearGradient = new LinearGradient(0, 0, radioWith, radioHeight,
                new int[] {startColor, endColor},
                null, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
    }

    private void stroke() {
        // 复制原来View画笔中的一些参数
        if (mStrokePaint == null) {
            mStrokePaint = new TextPaint();
        }
        mStrokePaint.setTextSize(mPaint.getTextSize());
        mStrokePaint.setTypeface(mPaint.getTypeface());
        mStrokePaint.setFlags(mPaint.getFlags());
        mStrokePaint.setAlpha(mPaint.getAlpha());

        // 自定义描边效果
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(strokeColor);
        mStrokePaint.setStrokeWidth(strokeWidth);
    }
}
