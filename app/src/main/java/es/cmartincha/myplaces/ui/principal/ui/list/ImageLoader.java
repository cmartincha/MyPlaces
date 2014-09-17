
package es.cmartincha.myplaces.ui.principal.ui.list;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoader {
    private Uri mImageUri;
    private ImageView mImageView;
    private Context mContext;
    private ImageCacheManager mImageCacheManager;

    public ImageLoader(Uri imageUri, ImageView imageView, Context context) {
        mImageView = imageView;
        mImageUri = imageUri;
        mContext = context;

        mImageCacheManager = ImageCacheManager.getInstance();
    }

    public void loadBitmap() {
        BitmapWorkerTask task = new BitmapWorkerTask();

        task.execute(mImageUri);
    }

    public class BitmapWorkerTask extends AsyncTask<Uri, Void, Bitmap> {
        private WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask() {
            imageViewReference = new WeakReference<ImageView>(mImageView);
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {
            Uri imageUri = params[0];
            Bitmap bitmap = mImageCacheManager.get(imageUri.getPath());

            if (bitmap == null) {
                bitmap = ImageUtil.decodeSampledBitmapFromResource(mContext, imageUri,
                        imageViewReference
                                .get()
                                .getWidth(), imageViewReference.get().getHeight());
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
