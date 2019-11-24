package com.example.luca.biometricsystem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class FocusView extends View {

    private static final String TAG = "FocusView";
    private Paint mTransparentPaint;
    private Paint mSemiBlackPaint;
    private Path mPath = new Path();

    public FocusView(Context context) {
        super(context);
        initPaints();
    }

    public FocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public FocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        mTransparentPaint = new Paint();
        mTransparentPaint.setColor(Color.TRANSPARENT);
        mTransparentPaint.setStrokeWidth(10);

        mSemiBlackPaint = new Paint();
        mSemiBlackPaint.setColor(Color.TRANSPARENT);
        mSemiBlackPaint.setStrokeWidth(10);
    }

    public Integer getLeftRectangle(){
        Log.d(TAG, "getLeftRectangle: "+ (getLeft()+(getRight()-getLeft())/5));
        return getLeft()+(getRight()-getLeft())/5;

    }

    public Integer getRightRectangle(){
        Log.d(TAG, "getRightRectangle: "+ (getRight()-(getRight()-getLeft())/5));
        return getRight()-(getRight()-getLeft())/5;
    }

    public Integer getTopRectangle(){
        Log.d(TAG, "getTopRectangle: "+(getTop()+(getBottom()-getTop())/5));
        return getTop()+(getBottom()-getTop())/5;
    }

    public Integer getBottomRectangle(){
        Log.d(TAG, "getBottomRectangle: "+ (getBottom()-(getBottom()-getTop())/5));
        return getBottom()-(getBottom()-getTop())/5;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        Rect rect = new Rect();

        mPath.addRect(getLeftRectangle(), getTopRectangle(), getRightRectangle(), getBottomRectangle(), Path.Direction.CW);

        //mPath.addCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 550, Path.Direction.CW);
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawRect(getLeftRectangle(), getTopRectangle(), getRightRectangle(), getBottomRectangle(), mTransparentPaint);

        //canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 550, mTransparentPaint);

        canvas.drawPath(mPath, mSemiBlackPaint);
        canvas.clipPath(mPath);
        canvas.drawColor(Color.parseColor("#A6000000"));
    }
}