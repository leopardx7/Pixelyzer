package it.ilpixelmatto.pixelyzer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

/**
 * Created by Riccardo Russo on 05/10/2016.
 */
public class BitmapLoader {

    private LruCache<Integer, Bitmap> mMemoryCache;

    // il valore della massima memoria disponibile, in kilobytes
    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

    // usiamo 1/8 della memoria disponibile
    private final int cacheSize = maxMemory / 8;


    public BitmapLoader() {


        mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };


    }

    public Bitmap loadBitmapFromFile(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        return BitmapFactory.decodeFile(path, options);
    }


    public /*Bitmap*/ void addBitmapToMemoryCache(Integer key, Bitmap bitmap) {

//        String filePath = Environment.getExternalStorageDirectory() + "/Pictures/GalleryApp/temp/GA" + key + ".png";
//
//
//        // new File(Environment.getExternalStorageDirectory() + "/Pictures/GalleryApp");
//
//        File file = new File(filePath);
//        File path = new File(file.getParent());
//
//        if (bitmap != null) {
//            try {
//                // build directory
//                if (file.getParent() != null && !path.isDirectory()) {
//                    path.mkdirs();
//                }
//                // output image to file
//                FileOutputStream fos = new FileOutputStream(filePath);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
//                fos.close();
//               // ret = true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            Log.i("BitmapLoader", "savePicture image parsing error");
//        }

//
//
//
//        File dir = new File(filepath);
//
//        if(!dir.exists())dir.mkdirs();
//
//        File picture = new File(Environment.getExternalStorageDirectory() + filepath, side + ".png");
//        FileOutputStream fOut = new FileOutputStream(file);
//
//        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
//        fOut.flush();
//        fOut.close();
//
//        System.out.println(filepath);
//
//        bitmap.recycle();
//        System.gc();
//
//

//        if (getBitmapFromMemCache(key) == null) {
        mMemoryCache.put(key, bitmap);
//        }
//        return bitmap;


    }

    public Bitmap getBitmapFromMemCache(Integer key) {
//        String filePath = Environment.getExternalStorageDirectory() + "/Pictures/GalleryApp/temp/GA" + key + ".png";
//
//       // File file = new File(filePath);
//
//
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//


        return mMemoryCache.get(key);
    }

    public int sizeof() {
        return mMemoryCache.snapshot().size();
    }


}
