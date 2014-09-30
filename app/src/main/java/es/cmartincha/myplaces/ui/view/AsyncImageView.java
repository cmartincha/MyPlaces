package es.cmartincha.myplaces.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import es.cmartincha.myplaces.ui.principal.list.ImageCacheManager;
import es.cmartincha.myplaces.ui.principal.list.ImageUtil;

/**
 * Created by Carlos on 30/09/2014.
 */
public class AsyncImageView extends ImageView {
    private static final int DEFAULT_HEIGHT_DP = 48;
    private float mHeight = ImageUtil.convertDpToPixels(getContext(), DEFAULT_HEIGHT_DP);
    private static final int DEFAULT_WIDTH_DP = 48;
    private float mWidth = ImageUtil.convertDpToPixels(getContext(), DEFAULT_WIDTH_DP);

    private ImageCacheManager mImageCacheManager = ImageCacheManager.getInstance();

    public AsyncImageView(Context context) {
        super(context);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageUriAsync(Uri imageUri) {
        BitmapWorkerTask task = new BitmapWorkerTask(this);

        task.execute(imageUri);
    }

    public class BitmapWorkerTask extends AsyncTask<Uri, Void, Bitmap> {
        private WeakReference<AsyncImageView> imageViewReference;

        public BitmapWorkerTask(AsyncImageView asyncImageView) {
            imageViewReference = new WeakReference<AsyncImageView>(asyncImageView);
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {
            Uri imageUri = params[0];
            Bitmap bitmap = mImageCacheManager.get(imageUri.getPath());

            if (bitmap == null) {
                bitmap = ImageUtil.decodeSampledBitmapFromResource(getContext(), imageUri, (int) mWidth, (int) mHeight);
                if (bitmap != null) {
                    mImageCacheManager.put(imageUri.getPath(), bitmap);
                }
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();

                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}