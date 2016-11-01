package it.ilpixelmatto.pixelyzer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

/**
 * Created by Riccardo Russo on 05/10/2016.
 */
public class BitmapLoader {

    private LruCache<Integer, Bitmap> mMemoryCache;
    private static GPUImageFilterGroup mGPUImageFilterGroup;

    // il valore della massima memoria disponibile, in kilobytes
    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

    // usiamo 1/8 della memoria disponibile
    private final int cacheSize = maxMemory / 8;


    public BitmapLoader() {


        mGPUImageFilterGroup = new GPUImageFilterGroup();
//        mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(Integer key, Bitmap bitmap) {
//                // The cache size will be measured in kilobytes rather than
//                // number of items.
//                return bitmap.getByteCount() / 1024;
//            }
//        };


    }

//    public Bitmap loadBitmapFromFile(String path) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//
//        return BitmapFactory.decodeFile(path, options);
//    }


//    public /*Bitmap*/ void addBitmapToMemoryCache(Integer key, Bitmap bitmap) {

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
//        mMemoryCache.put(key, bitmap);
//        }
//        return bitmap;


//    }

//    public Bitmap getBitmapFromMemCache(Integer key) {
////        String filePath = Environment.getExternalStorageDirectory() + "/Pictures/GalleryApp/temp/GA" + key + ".png";
////
////       // File file = new File(filePath);
////
////
////        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
////
//
//
//        return mMemoryCache.get(key);
//    }

//    public int sizeof() {
//        return mMemoryCache.snapshot().size();
//    }


    public GPUImageFilterGroup lala () {
        mGPUImageFilterGroup.getFilters().set(0, new GPUImageExposureFilter(1));
        mGPUImageFilterGroup.getFilters().set(1, new GPUImageSaturationFilter(1));
        mGPUImageFilterGroup.getFilters().set(2, new GPUImageEmbossFilter(2));
        mGPUImageFilterGroup.updateMergedFilters();

        return mGPUImageFilterGroup;
    }

    public Class<? extends GPUImageFilter> findInstanceof(GPUImageFilter myFilter) {
        Log.wtf("CACCAAAAAAA", "FILTRI " + myFilter.getClass());

        return myFilter.getClass();

    }

    public GPUImageFilterGroup addFilter(GPUImageFilter myFilter) {






        if(mGPUImageFilterGroup.getFilters().size()==0)
            mGPUImageFilterGroup.addFilter(myFilter);

        boolean presente=false;

        for(int i=0; i<mGPUImageFilterGroup.getFilters().size(); i++) {

            if((mGPUImageFilterGroup.getFilters().get(i) instanceof  IFImageFilter) && (myFilter instanceof IFImageFilter)) {
                presente = true;
                mGPUImageFilterGroup.getFilters().set(i, myFilter);
                break;
            }

            if((mGPUImageFilterGroup.getFilters().get(i).getClass()) == myFilter.getClass()) {



                mGPUImageFilterGroup.getFilters().set(i, myFilter);
                presente=true;
            }




//            if(mGPUImageFilterGroup.getFilters().get(i) instanceof GPUImageExposureFilter)
//            {
//                mGPUImageFilterGroup.getFilters().set(i, myFilter);
//            } else
//            if(mGPUImageFilterGroup.getFilters().get(i) instanceof GPUImageContrastFilter)
//            {
//                mGPUImageFilterGroup.getFilters().set(i, myFilter);
//            } else
//            if(mGPUImageFilterGroup.getFilters().get(i) instanceof GPUImageSaturationFilter)
//            {
//                mGPUImageFilterGroup.getFilters().set(i, myFilter);
//            } else mGPUImageFilterGroup.addFilter(myFilter);
        }

        if(!presente)
            mGPUImageFilterGroup.addFilter(myFilter);


      //  Log.wtf("MERDA", "FILTRI " + mGPUImageFilterGroup.getFilters().size());
        mGPUImageFilterGroup.updateMergedFilters();
        return mGPUImageFilterGroup;
    }

    public void removeInstaFilter() {
        for (int i = 0; i < mGPUImageFilterGroup.getFilters().size(); i++) {

            if ((mGPUImageFilterGroup.getFilters().get(i) instanceof IFImageFilter)) {

                mGPUImageFilterGroup.getFilters().remove(i);
                break;
            }
        }
        mGPUImageFilterGroup.updateMergedFilters();

    }
    public int getLenghFilterGroup() {
        return mGPUImageFilterGroup.getFilters().size();
    }

}
