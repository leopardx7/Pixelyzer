package it.ilpixelmatto.Pixelyzer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Riccardo Russo on 02/07/2016.
 * Si occupa del caricamento in background
 */
public class BitmapWorkerTask extends AsyncTask<File, Void, Bitmap> {


    WeakReference<ImageView> imageViewWeakReference;
    final static int TARGET_IMAGE_VIEW_WIDTH = 150;
    final static int TARGET_IMAGE_VIEW_HEIGHT = 150;
    private File mImageFile;

    public BitmapWorkerTask(ImageView imageView) {
        imageViewWeakReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(File... params) {
        mImageFile = params[0];

        return decodeBitmapFromFile(params[0]);
        //return BitmapFactory.decodeFile(params[0].getAbsolutePath()); // prendo il path del file
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null && imageViewWeakReference != null) {
            ImageView viewImage = imageViewWeakReference.get();
            if (viewImage != null) {
                viewImage.setImageBitmap(bitmap);
            }
        }
        // super.onPostExecute();
        /*
        if(isCancelled()) {
            bitmap = null;
        }
        if (bitmap!= null && imageViewWeakReference != null) {
            ImageView imageView = imageViewWeakReference.get();
            BitmapWorkerTask bitmapWorkerTask = ImageAdapter.getBitmapWorkerTask(imageView);
            if(this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }*/
    }

    private int calculateInSampleSize(BitmapFactory.Options bmOptions) {
        final int photoWidth = bmOptions.outWidth;
        final int photoHeight = bmOptions.outHeight;
        int scaleFactor = 1;

        if (photoWidth > TARGET_IMAGE_VIEW_WIDTH || photoHeight > TARGET_IMAGE_VIEW_HEIGHT) {
            final int halfPhotoWidth = photoWidth / 2;
            final int halfPhotoHeight = photoHeight / 2;
            while (halfPhotoWidth / scaleFactor > TARGET_IMAGE_VIEW_WIDTH
                    || halfPhotoHeight / scaleFactor > TARGET_IMAGE_VIEW_HEIGHT) {
                scaleFactor *= 2;
            }
        }
        return scaleFactor;
    }

    private Bitmap decodeBitmapFromFile(File imageFile) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions);
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
    }

    public File getmImageFile() {
        return mImageFile;
    }
}
