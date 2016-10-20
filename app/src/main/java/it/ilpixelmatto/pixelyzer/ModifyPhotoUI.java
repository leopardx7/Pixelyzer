package it.ilpixelmatto.Pixelyzer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.mukesh.image_processing.ImageProcessor;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;

public class ModifyPhotoUI extends Activity implements View.OnClickListener/*implements View.OnClickListener*/ {

    private GLSurfaceView mGPUImageView;

    private static Integer counter = 0;

    private BitmapLoader bMapUtils;
    //PaintView myView;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    private Bitmap bMap;
    GLSurfaceView myView;
    GPUImage mGPUImage;
    private int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_photo_ui);

        mGPUImage = new GPUImage(this);
        mGPUImageView = (GLSurfaceView) findViewById(R.id.GPUImageView);
        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.GPUImageView));


        bMapUtils = new BitmapLoader();

        //bMap = bMapUtils.loadBitmapFromFile(new File(getIntent().getData().getPath()).getAbsolutePath());

        bMap = BitmapFactory.decodeFile(new File(getIntent().getData().getPath()).getAbsolutePath());

        ImageProcessor imageProcessor = new ImageProcessor();


        mGPUImage.setImage(imageProcessor.applySnowEffect(bMap));

        // DEVO FARLO PER RICEVERE DAL UCROP LA ROBA LA SECONDA VOLTA

        Toast.makeText(getApplicationContext(), "CACHE: " + bMapUtils.sizeof(), Toast.LENGTH_SHORT).show();


        SeekBar barraLuminosita = (SeekBar) findViewById(R.id.seekBar);
        barraLuminosita.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGPUImage.setFilter(new GPUImageExposureFilter(((progress) - 100) / 10.f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        findViewById(R.id.indietro).setOnClickListener(this);
        findViewById(R.id.salva).setOnClickListener(this);
        findViewById(R.id.aggiungi).setOnClickListener(this);


//        mGPUImageView = (GLSurfaceView) findViewById(R.id.GPUImageView);
//        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.GPUImageView));


//        myView = (GLSurfaceView) findViewById(R.id.GPUImageView);


        String croppedPhoto = getIntent().getStringExtra("MyPhoto");
        //  Uri uri = Uri.parse(getIntent().getExtras().getString("MyPhoto"));
        // bMap = BitmapFactory.decodeFile(new File(getIntent().getData().getPath()).getAbsolutePath());


        // mGPUImage.setImage(bMap);
//
//        float widthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,
//                pxToMm(bMap.getWidth(), this),
//                getResources().getDisplayMetrics());
//
//        float heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,
//                pxToMm(bMap.getHeight(), this),
//                getResources().getDisplayMetrics());
//
//        ViewGroup.LayoutParams layoutParams= myView.getLayoutParams();
//        layoutParams.width= (int) widthPx;
//        layoutParams.height= (int) heightPx;
//
//        myView.setLayoutParams(layoutParams);
//
//
//        //Bitmap bMap = BitmapFactory.decodeFile(getIntent().getStringExtra("MyPhoto"));
//
//
//        myView.setRenderer(new PaintView(this, bMap));
        // myView = new PaintView(this, Bitmap.createBitmap(bMap));
        //setContentView(myView);

//
//        Button myButton = (Button) findViewById(R.id.button);
//        myButton.setOnClickListener(this);
//        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mGPUImage.setFilter(new GPUImageExposureFilter((progress-100.f)/10.f));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//


        // Uri.parse(getIntent().getStringExtra("MyPhoto"));
//        mGPUImage = new GPUImage(this);
//        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.GPUImageView));
//        mGPUImage.setImage(bMap); // this loads image on the current thread, should be run in a thread

    }

//
//    /**
//     * Called when a view has been clicked.
//     *
//     * @param v The view that was clicked.
//     */
//    @Override
//    public void onClick(View v) {
//
//
//        if(v.getId() == R.id.button) {
//            mGPUImage.setFilter(new GPUImageSepiaFilter());
//        }
//
//    }

//
//    public static float pxToMm(final float px, final Context context)
//    {
//        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, dm);
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.salva:

                bMapUtils.addBitmapToMemoryCache(counter++, mGPUImage.getBitmapWithFilterApplied());

                break;
            case R.id.indietro:

                mGPUImage.deleteImage();
                mGPUImage.setImage(bMapUtils.getBitmapFromMemCache(--counter));

                Toast.makeText(getApplicationContext(), "CACHE: " + bMapUtils.sizeof(), Toast.LENGTH_SHORT).show();

                break;

            case R.id.aggiungi:


//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                Toast.makeText(getApplicationContext(), "CACHE: " + bMapUtils.sizeof(), Toast.LENGTH_SHORT).show();
//
//                startActivityForResult(Intent.createChooser(intent, "Select picture"), RESULT_LOAD_IMG);
                break;
        }
    }


    public static void startWithUri(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(context, ModifyPhotoUI.class);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     * <p>This method is never invoked if your activity sets
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    performCrop(data.getData());
                } else {
                    Toast.makeText(this, "MERDA", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) { // qua ho già croppato la foto
                handleCropResult(data);
            }
        } catch (Exception e) {
            Toast.makeText(this, "perché vai qua?", Toast.LENGTH_LONG)
                    .show();
        }


    }


    private void performCrop(Uri data) {

        String uri = SAMPLE_CROPPED_IMAGE_NAME;
        uri += "jpg";
        UCrop ucrop = UCrop.of(data, Uri.fromFile(new File(getCacheDir(), uri)))
                .withAspectRatio(1, 1);
        ucrop.start(this);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            ModifyPhotoUI.startWithUri(this, resultUri);
//            SingleImageActivity.startWithUri(this, resultUri);
        } else {
            Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show();
        }
    }
}
