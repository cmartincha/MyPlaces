package es.cmartincha.myplaces.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import es.cmartincha.mislugares.R;

/**
 * View que muestra una imagen y permite redimensionarla y moverte por ella
 */
public class ZoomableView extends View implements ScaleGestureDetector.OnScaleGestureListener {
    private static int SCALE_FACTOR_MULTIPLIER = 4;

    private ScaleGestureDetector mDetector;
    private Bitmap mImageBitmap;
    private Matrix mMatrix = new Matrix();
    private float mOldX;
    private float mOldY;
    private RectF mSourceRectF;
    private RectF mDestinationRectF;
    private boolean mSuspendTouchEvent = false;

    public ZoomableView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setScaleGestureDetector(context);
        setBitmap(context, attrs);
        setInitialBounds();
    }

    public ZoomableView(Context context) {
        super(context);

        setScaleGestureDetector(context);
        setInitialBounds();
    }

    private void setInitialBounds() {
        mSourceRectF = new RectF(0, 0, mImageBitmap.getWidth(), mImageBitmap.getHeight());
        mDestinationRectF = new RectF();
    }

    private void setBitmap(Context context, AttributeSet attrs) {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ZoomableView, 0, 0);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(attributes.getResourceId(R.styleable.ZoomableView_image, 0));

        mImageBitmap = bitmapDrawable.getBitmap();
    }

    private void setScaleGestureDetector(Context context) {
        mDetector = new ScaleGestureDetector(context, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSuspendTouchEvent) {
            return true;
        }

        boolean isMultiTouch = event.getPointerCount() > 1;

        //Se ha pulsado con mas de un dedo
        if (isMultiTouch) {
            //Detecto si es un gesto de zoom
            mDetector.onTouchEvent(event);

            //Hace que se refresque la vista
            invalidate();
        } else {
            float mCurrentX = event.getX();
            float mCurrentY = event.getY();

            //Se esta moviendo un dedo
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float DX = mCurrentX - mOldX;
                float DY  = mCurrentY - mOldY;

                mOldX = mCurrentX;
                mOldY = mCurrentY;

                if (mDestinationRectF.top + DY <= 0) {
                    mDestinationRectF.offset(0, DY);
                }
                if (mDestinationRectF.left + DX <= 0) {
                    mDestinationRectF.offset(DX, 0);
                }

                invalidate();
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) { //Se ha pulsado con un dedo
                mOldX = mCurrentX;
                mOldY = mCurrentY;
            }
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        //Calculo el area de dibujado inicial
        mDestinationRectF.set(0, 0, width, height);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        mMatrix.setRectToRect(mSourceRectF, mDestinationRectF, Matrix.ScaleToFit.START);
        canvas.drawBitmap(mImageBitmap, mMatrix, null);

        canvas.restore();
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        float scaleFactor = scaleGestureDetector.getScaleFactor();
        float inverseScaleFactor = 2 - scaleFactor;

        if (mDestinationRectF.right >= 0) {
            mDestinationRectF.right *= scaleFactor;
        } else {
            mDestinationRectF.right *= inverseScaleFactor;
        }

        if (mDestinationRectF.bottom >= 0) {
            mDestinationRectF.bottom *= scaleFactor;
        } else {
            mDestinationRectF.bottom *= inverseScaleFactor;
        }

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        //Cuando termino el escalado desactivo temporalmente el evento de mover para que no haga un desplazamiento involuntario al apartar los dedos.
        //Es muy probable que no los apartes a la vez y uno de ellos siga desplazandose momentaneamente por la pantalla
        mSuspendTouchEvent = true;

        new CountDownTimer(SCALE_FACTOR_MULTIPLIER, 3000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                mSuspendTouchEvent = false;
                cancel();
            }
        }.start();
    }

    public void setImage(Bitmap bitmap) {
        mImageBitmap = bitmap;

        invalidate();
        requestLayout();
    }
}
