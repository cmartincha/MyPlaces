
package es.cmartincha.myplaces.ui.principal.list;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;

import java.io.FileNotFoundException;

/**
 * Clase con funciones para manejar imagenes
 */
public class ImageUtil {
    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Context context, Uri imageUri,
                                                         int reqWidth,
                                                         int reqHeight) {

        try {
            Options options = new Options();

            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri),
                    null, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(imageUri), null, options);
        } catch (FileNotFoundException e) {
            Log.e("decode_image_error", "File not found");
            e.printStackTrace();
        }

        return null;
    }

    public static float convertDpToPixels(Context context, int dps) {
        Resources resources = context.getResources();

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, resources.getDisplayMetrics());
    }
}
