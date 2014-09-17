
package es.cmartincha.myplaces.ui.place;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.ImageView;

public class PhotoImageView extends ImageView implements OnScaleGestureListener {

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1;

    public PhotoImageView(Context context, Bitmap photo) {
        super(context);

        mScaleDetector = new ScaleGestureDetector(context, this);

        setImageBitmap(photo);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        mScaleDetector.onTouchEvent(event);

        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mScaleFactor *= detector.getScaleFactor();
        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

        invalidate();

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }

}
