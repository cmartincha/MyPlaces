
package es.cmartincha.myplaces.ui.principal.ui.list;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

public class ImageCacheManager {
    private static int MAX_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);
    private static ImageCacheManager imageCacheManagerInstance;

    private LruCache<String, Bitmap> mMemoryChache;

    private ImageCacheManager() {
        mMemoryChache = new LruCache<String, Bitmap>(MAX_CACHE_SIZE) {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                } else {
                    return bitmap.getByteCount();
                }
            }
        };
    }

    public synchronized boolean put(String key, Bitmap bitmap) {
        return mMemoryChache.put(key, bitmap) != null;
    }

    public Bitmap get(String key) {
        return mMemoryChache.get(key);
    }

    public void remove(String key) {
        mMemoryChache.remove(key);
    }

    public static ImageCacheManager getInstance() {
        if (imageCacheManagerInstance == null) {
            imageCacheManagerInstance = new ImageCacheManager();
        }

        return imageCacheManagerInstance;
    }
}
