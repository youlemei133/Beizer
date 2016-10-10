package com.chiigu.beizer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hudawei on 2016/10/10.
 *
 */
public class SimpleBeizerView extends View {
    private Paint paint;
    private int mHeight;
    private int mWidth;
    private Path mPath;
    private int r;//圆半径
    private float moveY;
    private float offsetX;//x方向的偏移量
    private float offsetY;//Y方向的偏移量

    private float waveHeight;//波高
    private Paint cPaint;

    public SimpleBeizerView(Context context) {
        super(context);
    }

    public SimpleBeizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(context,R.color.colorPrimary));
        paint.setStyle(Paint.Style.FILL);

        cPaint=new Paint(paint);
        cPaint.setStyle(Paint.Style.FILL);
        cPaint.setColor(Color.parseColor("#AEEEEE"));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mPath=new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        int layerId=canvas.saveLayer(0,0,mWidth,mHeight,null,Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(r,r,r,cPaint);

        mPath.reset();
        mPath.moveTo(-r-offsetX,r+r-offsetY);
        mPath.quadTo(-0.5f*r-offsetX,2*r-moveY+r-offsetY,0-offsetX,r+r-offsetY);
        mPath.quadTo(r/2-offsetX,moveY+r-offsetY,r-offsetX,r+r-offsetY);

        mPath.quadTo(1.5f*r-offsetX,2*r-moveY+r-offsetY,2*r-offsetX,r+r-offsetY);
        mPath.quadTo(2.5f*r-offsetX,moveY+r-offsetY,3*r-offsetX,r+r-offsetY);

        mPath.lineTo(mWidth,mHeight);
        mPath.lineTo(0,mHeight);
        mPath.close();
        canvas.drawPath(mPath,paint);

        canvas.restoreToCount(layerId);
    }

    private void startAnim(){
        ValueAnimator animator=ValueAnimator.ofFloat(r-waveHeight,r+waveHeight)
                .setDuration(1200);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(-1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveY=(float)animation.getAnimatedValue();
                offsetX=animation.getAnimatedFraction()*r;
                invalidate();
            }
        });
        animator.start();
    }

    private void startAnimOffsetY(){
        ValueAnimator animator=ValueAnimator.ofFloat(r-waveHeight,r+waveHeight)
                .setDuration(20000);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(-1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offsetY=animation.getAnimatedFraction()*2*r;
            }
        });
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight=MeasureSpec.getSize(heightMeasureSpec);
        mWidth=MeasureSpec.getSize(widthMeasureSpec);
        r=mHeight/2;
        waveHeight=0.3f*r;
        startAnim();
        startAnimOffsetY();
    }
}
