package it.ilpixelmatto.pixelyzer;

/**
 * Created by Riccardo Russo on 02/07/2016.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Bitmap placeHolderBitmap;
    private File imagesFile;
    private static RecycleViewClickInterface mPositionInterface;


    // mi serve per fixare la concorrenza degli effetti in basso
    public static class AsyncDrawble extends BitmapDrawable {
        final WeakReference<BitmapWorkerTask> taskWeakReference;

        public AsyncDrawble(Resources resources,
                            Bitmap bitmap,
                            BitmapWorkerTask bitmapWorkerTask) {
            super(resources, bitmap);
            taskWeakReference = new WeakReference(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return taskWeakReference.get();
        }

    }

    public ImageAdapter(File folderFile, RecycleViewClickInterface positionInterface) {
        imagesFile = folderFile;
        mPositionInterface = positionInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_images_relative_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //File imageFile = imagesFile;
        //Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        //holder.getImageView().setImageBitmap(imageBitmap);
        //Picasso.with(holder.getImageView().getContext()).load(imageFile).into(holder.getImageView());
        BitmapWorkerTask workerTask = new BitmapWorkerTask(holder.getImageView());
        workerTask.execute(imagesFile);
        /*if(checkBitmapWorkerTask(imagesFile, holder.getImageView())) {
            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(holder.getImageView());
            AsyncDrawble asyncDrawble = new AsyncDrawble(holder.getImageView().getResources(),
                    placeHolderBitmap,
                    bitmapWorkerTask);
            holder.getImageView().setImageDrawable(asyncDrawble);
            bitmapWorkerTask.execute();
            // tutto questo mi evita che vengano visualizzati gli effetti sbagliati, quando si scorre e vengono ricaricati
        }
*/

    }

    @Override
    public int getItemCount() {
        return 10;
        // return imagesFile.listFiles().length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.imageGalleryView);
        }

        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View v) {
            mPositionInterface.getRecycleViewAdapterPosition(this.getPosition());
        }
    }

    public static boolean checkBitmapWorkerTask(File imagesFile, ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final File workerFile = bitmapWorkerTask.getmImageFile();
            if (workerFile != null) {
                if (workerFile != imagesFile) {
                    bitmapWorkerTask.cancel(true);
                } else {
                    //bitmap worker task file è quello che dovrebbe essere, non faccio nulla
                    return false;
                }
            }
        }
        return true;
    }

    public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AsyncDrawble) {
            AsyncDrawble asyncDrawble = (AsyncDrawble) drawable;
            return asyncDrawble.getBitmapWorkerTask();
        }
        return null;
    }
}
