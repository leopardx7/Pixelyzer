//CLIENT SECRET 774a3b21-89ef-4fec-97c5-fdf71b15e53d
//CLIENT ID (DEVELOPMENT MODE)* 06eede89c2474cfb96f7bdc72bcfe058

package it.ilpixelmatto.Pixelyzer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.shchurov.horizontalwheelview.HorizontalWheelView;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import xyz.hanks.library.SmallBang;


/**
 * Created by Riccardo Russo on 30/06/2016.
 */
public class SingleImageActivity extends Activity implements View.OnClickListener/*OverMenuView.OnSelectionChangeListener*/ {
    private static final String IMAGE_FILE_LOCATION = "image_file_location";
    // private ImageView imageView;

    ArrayList<Integer> pc = new ArrayList<Integer>(20);

    private RecyclerView recyclerView;
    private File galleryFolder;
    private File imageFile;
    //    private SeekBar barraLuminosita;
//    private SeekBar barraContrasto, barraRosso, barraBlu, barraVerde;
    private Bitmap bMap, bMap2;
    private int luminosita = 0, contrasto = 10, rosso = 1, verde = 1, blu = 1;
    //    private HorizontalScrollView horizontalScrollView;
    private TextView rotellaTextView;
    private HorizontalScrollView filtriScrollView;

    //private PaintView mPaintView=null;
    private Vector<Bitmap> bitmapVector;
    private GPUImageView mGPUImage;
    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    private HorizontalWheelView rotella, rotellaContrasto, rotellaSaturazione;

    private SmallBang mSmallBang; // animazione bottoni

    private String filtroAttivo = "";

    private Context context;
    // private ApplicaEffettiTask mioTask;
    private BitmapLoader bMapUtils; // gestisce le bitmap
    private Integer counter = 1;
    private static final String TAG = "SingleImageActivity";
    private RelativeLayout rotellaLayout;
    private Button resetRotella, resetRotellaContrasto, resetRotellaSaturazione;
    private boolean applicaLuminosita = false, applicaContrasto = false, applicaSaturazione = false;
    private double radiantLuminosità = 0, radiantContrasto = 0, radiantSaturazione = 0;

    GPUImageFilterGroup group = new GPUImageFilterGroup();

//    ImageProcessor imageProcessor;
//    private GLSurfaceView mGPUImageView;


    //private UndoRedo undoRedo;

    public static void startWithUri(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(context, SingleImageActivity.class);
        intent.setData(uri);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mGPUImage = new GPUImageView(context);


        bMapUtils = new BitmapLoader();

//        imageProcessor = new ImageProcessor();


//        mGPUImageView = (GLSurfaceView) findViewById(R.id.visualizzaFotoView);
//        String croppedPhoto = getIntent().getStringExtra("MyPhoto");
        //  Uri uri = Uri.parse(getIntent().getExtras().getString("MyPhoto"));
        bMap = BitmapFactory.decodeFile(new File(getIntent().getData().getPath()).getAbsolutePath());

        //  bMap = bMapUtils.loadBitmapFromFile(new File(getIntent().getData().getPath()).getAbsolutePath());
        // bMap = bMapUtils.loadBitmapFromFile(new File(getIntent().getData().getPath()).getAbsolutePath());


        //undoRedo = new UndoRedo(bMap.copy(bMap.getConfig(), true));


//        float widthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,
//                pxToMm(bMap.getWidth(), context),
//                getResources().getDisplayMetrics());
//
//        float heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,
//                pxToMm(bMap.getHeight(), context),
//                getResources().getDisplayMetrics());
//
//        ViewGroup.LayoutParams layoutParams= mGPUImageView.getLayoutParams();
//        layoutParams.width= (int) widthPx;
//        layoutParams.height= (int) heightPx;
//
//        mGPUImageView.setLayoutParams(layoutParams);

        setContentView(R.layout.activity_single_image);
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
        // prendo le dimensioni dello schermo
        //DisplayMetrics displayMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // int width = displayMetrics.widthPixels;
        // int height = displayMetrics.heightPixels;


        //imageView = (ImageView) findViewById(R.id.visualizzaFoto);

        // mGPUImage.set

        mGPUImage = (GPUImageView) findViewById(R.id.visualizzaFotoView);

//        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.visualizzaFotoView));
        mGPUImage.setImage(bMap);
        mGPUImage.requestRender();
        bMapUtils.addBitmapToMemoryCache(counter, bMap);
        Log.wtf(TAG, "Initial counter = " + counter + ", cache = " + bMapUtils.sizeof());
        Toast.makeText(getApplicationContext(), "CACHE: " + bMapUtils.sizeof(), Toast.LENGTH_SHORT).show();

//        valoreLumCont = (TextView) findViewById(R.id.valoreLuminositaContrasto);


//        findViewById(R.id.tagliaRuota).setOnClickListener(this);
//        findViewById(R.id.blu2).setOnClickListener(this);
//        findViewById(R.id.blu3).setOnClickListener(this);
//        findViewById(R.id.blu4).setOnClickListener(this);
//        findViewById(R.id.red).setOnClickListener(this);
//        findViewById(R.id.red2).setOnClickListener(this);
//        findViewById(R.id.red3).setOnClickListener(this);
//        findViewById(R.id.purple4).setOnClickListener(this);
//        findViewById(R.id.green).setOnClickListener(this);
//        findViewById(R.id.green2).setOnClickListener(this);
//        findViewById(R.id.green3).setOnClickListener(this);
//        findViewById(R.id.green4).setOnClickListener(this);
//        findViewById(R.id.yellow).setOnClickListener(this);
//        findViewById(R.id.yellow2).setOnClickListener(this);
//        findViewById(R.id.yellow3).setOnClickListener(this);
//        findViewById(R.id.yellow4).setOnClickListener(this);
//        findViewById(R.id.purple2).setOnClickListener(this);
//        findViewById(R.id.purple3).setOnClickListener(this);


//        findViewById(R.id.button_choose_filter).setOnClickListener(this);
        filtriScrollView = (HorizontalScrollView) findViewById(R.id.FiltriScrollView);
        filtriScrollView.setOnClickListener(this);
        rotellaTextView = ((TextView) findViewById(R.id.rotellaTextView));
        findViewById(R.id.original).setOnClickListener(this);
        findViewById(R.id.earlybird).setOnClickListener(this);
        findViewById(R.id.xpro2).setOnClickListener(this);
        findViewById(R.id.lomofi).setOnClickListener(this);
        findViewById(R.id.sutro).setOnClickListener(this);
        findViewById(R.id.toaster).setOnClickListener(this);
        findViewById(R.id.inkwell).setOnClickListener(this);
        findViewById(R.id.walden).setOnClickListener(this);
        findViewById(R.id.hefe).setOnClickListener(this);
        findViewById(R.id.nashville).setOnClickListener(this);
        findViewById(R.id.millenovecentosettantasette).setOnClickListener(this);
        findViewById(R.id.lordkelvin).setOnClickListener(this);


        findViewById(R.id.luminosita).setOnClickListener(this);
        findViewById(R.id.contrasto).setOnClickListener(this);
        findViewById(R.id.saturazione).setOnClickListener(this);
//        findViewById(R.id.cornice).setOnClickListener(this);
        findViewById(R.id.filtri).setOnClickListener(this);
//        findViewById(R.id.ruotaDestra).setOnClickListener(this);
//        findViewById(R.id.ruotaSinistra).setOnClickListener(this);
//        findViewById(R.id.ruota).setOnClickListener(this);
//        findViewById(R.id.tagliaLibero).setOnClickListener(this);
//        findViewById(R.id.tagliaQuadrato).setOnClickListener(this);

        findViewById(R.id.undo).setOnClickListener(this);
        findViewById(R.id.done).setOnClickListener(this);
        rotellaLayout = (RelativeLayout) findViewById(R.id.rotellaLayout);
        rotella = (HorizontalWheelView) findViewById(R.id.rotella);
        rotellaContrasto = (HorizontalWheelView) findViewById(R.id.rotellaContrasto);
        rotellaSaturazione = (HorizontalWheelView) findViewById(R.id.rotellaSaturazione);
        resetRotella = (Button) findViewById(R.id.resetRotella);
        resetRotella.setOnClickListener(this);
        resetRotellaContrasto = (Button) findViewById(R.id.resetRotellaContrasto);
        resetRotellaContrasto.setOnClickListener(this);
        resetRotellaSaturazione = (Button) findViewById(R.id.resetRotellaSaturazione);
        resetRotellaSaturazione.setOnClickListener(this);
        setupListener();


        mSmallBang = SmallBang.attach2Window(this); // animazione bottoni

    }

    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;
            mGPUImage.setFilter(mFilter);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.filtri:
                mSmallBang.bang(v);

                if (filtriScrollView.getVisibility() == View.INVISIBLE) {
                    rotella.setVisibility(View.INVISIBLE);
                    rotellaSaturazione.setVisibility(View.INVISIBLE);
                    rotellaContrasto.setVisibility(View.INVISIBLE);
                    rotellaTextView.setVisibility(View.INVISIBLE);
                    resetRotella.setVisibility(View.INVISIBLE);
                    resetRotellaSaturazione.setVisibility(View.INVISIBLE);
                    resetRotellaContrasto.setVisibility(View.INVISIBLE);
                    filtriScrollView.setVisibility(View.VISIBLE);

                } else {
                    rotella.setVisibility(View.INVISIBLE);
                    rotellaTextView.setVisibility(View.INVISIBLE);
                    filtriScrollView.setVisibility(View.INVISIBLE);
                }


                break;
            case R.id.original:
//                switchFilterTo(new IFAmaroFilter(context));
                // togli filtro, si ma come?
                mGPUImage.setFilter(new GPUImageFilter());
                mGPUImage.requestRender();

                break;
            case R.id.earlybird:
                switchFilterTo(new IFEarlybirdFilter(context));
                mGPUImage.requestRender();
                break;
            case R.id.xpro2:
                switchFilterTo(new IFXprollFilter(context));
                mGPUImage.requestRender();
                break;
            case R.id.lomofi:
                switchFilterTo(new IFLomoFilter(context));
                mGPUImage.requestRender();
                break;
            case R.id.sutro:
                switchFilterTo(new IFSutroFilter(context));
                mGPUImage.requestRender();
                break;
            case R.id.toaster:
                switchFilterTo(new IFToasterFilter(context));
                mGPUImage.requestRender();
                break;
            case R.id.inkwell:
                switchFilterTo(new IFInkwellFilter(context));
                mGPUImage.requestRender();
                break;
            case R.id.walden:
                switchFilterTo(new IFWaldenFilter(context));
                mGPUImage.requestRender();
                break;
            case R.id.hefe:
                switchFilterTo(new IFHefeFilter(context));
                mGPUImage.requestRender();
                break;
            case R.id.nashville:
                switchFilterTo(new IFNashvilleFilter(context));
                mGPUImage.requestRender();
                break;
            case R.id.millenovecentosettantasette:
                switchFilterTo(new IF1977Filter(context));
                mGPUImage.requestRender();
                break;
            case R.id.lordkelvin:
                switchFilterTo(new IFLordKelvinFilter(context));
                mGPUImage.requestRender();
                break;
            case R.id.luminosita:
                mSmallBang.bang(v);

                rotella.setRadiansAngle(radiantLuminosità);

//                Toast.makeText(getApplicationContext(), "luminosità", Toast.LENGTH_SHORT).show();
                if (rotella.getVisibility() == View.INVISIBLE) {
                    applicaLuminosita = true;
                    rotella.setVisibility(View.VISIBLE);
                    rotellaContrasto.setVisibility(View.INVISIBLE);
                    rotellaSaturazione.setVisibility(View.INVISIBLE);
                    resetRotella.setVisibility(View.VISIBLE);
                    resetRotellaContrasto.setVisibility(View.INVISIBLE);
                    resetRotellaSaturazione.setVisibility(View.INVISIBLE);
                    filtriScrollView.setVisibility(View.INVISIBLE);
                } else {
                    applicaLuminosita = false;
                    rotella.setVisibility(View.INVISIBLE);
                    resetRotella.setVisibility(View.INVISIBLE);
                }

                break;

            case R.id.contrasto:
                rotellaContrasto.setRadiansAngle(radiantContrasto);
                mSmallBang.bang(v);
                if (rotellaContrasto.getVisibility() == View.INVISIBLE) {
                    applicaContrasto = true;
                    rotellaContrasto.setVisibility(View.VISIBLE);
                    rotella.setVisibility(View.INVISIBLE);
                    rotellaSaturazione.setVisibility(View.INVISIBLE);
                    resetRotellaContrasto.setVisibility(View.VISIBLE);
                    filtriScrollView.setVisibility(View.INVISIBLE);

                } else {
                    applicaContrasto = false;
                    rotellaContrasto.setVisibility(View.INVISIBLE);
                    resetRotellaContrasto.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.saturazione:
                rotellaSaturazione.setRadiansAngle(radiantSaturazione);
                mSmallBang.bang(v);
                if (rotellaSaturazione.getVisibility() == View.INVISIBLE) {
                    applicaSaturazione = true;
                    rotella.setVisibility(View.INVISIBLE);
                    rotellaContrasto.setVisibility(View.INVISIBLE);
                    rotellaSaturazione.setVisibility(View.VISIBLE);
                    resetRotellaSaturazione.setVisibility(View.VISIBLE);
                    resetRotellaContrasto.setVisibility(View.INVISIBLE);
                    resetRotella.setVisibility(View.INVISIBLE);
                    filtriScrollView.setVisibility(View.INVISIBLE);

                } else {
                    applicaSaturazione = false;
                    rotellaSaturazione.setVisibility(View.INVISIBLE);
                    resetRotellaSaturazione.setVisibility(View.INVISIBLE);
                }


                break;


            case R.id.undo:
                mSmallBang.bang(v);
                if (counter >= 0) {
                    counter--;
                    mGPUImage.setImage(bMapUtils.getBitmapFromMemCache(counter));
                    mGPUImage.requestRender();
                    Log.wtf(TAG, "Undo counter = " + counter + ", cache = " + bMapUtils.sizeof());
                }

//                Bitmap temp = undoRedo.undo();
//                if(temp==null)
//                    Toast.makeText(getApplicationContext(), "Impossibile tornare indietro", Toast.LENGTH_SHORT).show();
//                else
//                {
//                    mGPUImage.deleteImage();
//                    mGPUImage.setImage(temp.copy(temp.getConfig(), true));
//                    temp.recycle();
//                }


                break;


            case R.id.done:
                applicaLuminosita = false; //non cambio più il valore dell'esposizione
                mSmallBang.bang(v);
                rotella.setVisibility(View.INVISIBLE);
                rotellaContrasto.setVisibility(View.INVISIBLE);
                rotellaSaturazione.setVisibility(View.INVISIBLE);
                resetRotella.setVisibility(View.INVISIBLE);
                resetRotellaContrasto.setVisibility(View.INVISIBLE);
                resetRotellaSaturazione.setVisibility(View.INVISIBLE);
                filtriScrollView.setVisibility(View.INVISIBLE);

                counter++;
                bMapUtils.addBitmapToMemoryCache(counter, Bitmap.createBitmap(mGPUImage.getGPUImage().getBitmapWithFilterApplied()));

                Log.wtf(TAG, "Done counter = " + counter + ", cache = " + bMapUtils.sizeof());

                //mGPUImage.saveToPictures("GalleryApp", "temp.jpg", this);
                // mGPUImage.saveToPictures("GalleryApp", "temp.jpg", bMap.getWidth(), bMap.getHeight(), this);


//                Uri temp = Uri.parse("content://media/external/images/media/temp.jpg");
//                mGPUImage.getGPUImage().deleteImage();
//                mGPUImage.setImage(temp);
//                mGPUImage.requestRender();
//                Bitmap temp = Bitmap.createBitmap(mGPUImage.getBitmapWithFilterApplied());
//                mGPUImage.deleteImage();
//
//                mGPUImage.setImage(temp);
//                undoRedo.addBitmap(temp.copy(temp.getConfig(), true));
//                temp.recycle();
                break;
            case R.id.resetRotella:
                mSmallBang.bang(v);
                rotella.setRadiansAngle(0);
                break;
            /*case R.id.ruotaDestra: {
                //mGPUImage.setRotation(Rotation.ROTATION_90);
                bMap = rotate(bMap, 90);

                mGPUImage.setImage(bMap);
                mGPUImage.requestRender();

                //imageView.setImageBitmap(bMap);
                break;
            }
            case R.id.ruotaSinistra: {
                // mGPUImage.setRotation(Rotation.ROTATION_90);
                bMap = rotate(bMap, -90);
               // mGPUImage.deleteImage();
                mGPUImage.setImage(bMap);
                mGPUImage.requestRender();

                // imageView.setImageBitmap(rotate(bMap, -90));
                break;
            }
            case R.id.ruota: {
                break;
            }
            case R.id.tagliaLibero: {
                break;
            }
            case R.id.tagliaQuadrato: {
                break;
            }
            case R.id.purple2: {

                //imageView.setColorFilter(Color.MAGENTA, PorterDuff.Mode.LIGHTEN);

                break;
            }
            case R.id.purple3: {

                break;
            }
            case R.id.purple4: {

                break;
            }
            case R.id.blu2: {

                break;
            }
            case R.id.blu3: {

                break;
            }
            case R.id.blu4: {

                break;
            }*/

        }
    }


//    public Bitmap rotate(Bitmap src, float degree) {
//        // create new matrix
//        Matrix matrix = new Matrix();
//        // setup rotation degree
//        matrix.postRotate(degree);
//
//        //mGPUImage.setRotation(Rotation.ROTATION_90);
//        // return new bitmap rotated using matrix
//        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, false);
//    }


//    /**
//     * @param value      -10..10 0 is default per esposizione, 0..4 1 is default per contrasto
//     * @param operazione true esposizione, false contrasto
//     * @return new bitmap
//     */
//    public void cambiaLuminositaContrasto(float value, Boolean operazione) {
//        if (operazione)
//            //imageProcessor.doBrightness(bMap, (int)value);
//            mGPUImage.setFilter(new GPUImageExposureFilter(value));
//
//        else
//            mGPUImage.setFilter(new GPUImageContrastFilter(value));
//
////            imageProcessor.applyHueFilter(bMap, (int)value);
//
//        // mGPUImage.setImage(bMap);
//        mGPUImage.requestRender();

        /*
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);
        */
    // return mGPUImage.getBitmapWithFilterApplied();
//    }

//    public static Bitmap cambioColori(Bitmap src, int type, float percent) {
//        int width = src.getWidth();
//        int height = src.getHeight();
//        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
//
//        int A, R, G, B;
//        int pixel;
//
//        for (int x = 0; x < width; ++x) {
//            for (int y = 0; y < height; ++y) {
//                pixel = src.getPixel(x, y);
//                A = Color.alpha(pixel);
//                R = Color.red(pixel);
//                G = Color.green(pixel);
//                B = Color.blue(pixel);
//                if (type == 1) {
//                    R = (int) (R * (1 + percent));
//                    if (R > 255) R = 255;
//                } else if (type == 2) {
//                    G = (int) (G * (1 + percent));
//                    if (G > 255) G = 255;
//                } else if (type == 3) {
//                    B = (int) (B * (1 + percent));
//                    if (B > 255) B = 255;
//                }
//                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
//            }
//        }
//        return bmOut;
//    }


//    public void creaCanvas() {
//        //mGPUImage.setFilter(new GPUImageGrayscaleFilter(bMap));
//
//        /*Bitmap mutableBitmap = bMap.copy(Bitmap.Config.ARGB_8888, true); // 8888 ogni pixel è in 4 byte
//        //mPaintView = new PaintView(this, 150, 200);
//        Canvas canvas = new Canvas(mutableBitmap);
//        canvas.drawARGB(0x00, 255, 0, 0);
//       // canvas.drawColor(Color.RED);
//        canvas.drawBitmap(mutableBitmap, 0, 0, null);
//       // canvas.drawBitmap(bitmapStar, 0, 0, null);
//
//        BitmapDrawable dr = new BitmapDrawable(bMap);
//        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
//        imageView.setImageDrawable(dr);*/
//
//        // mPaintView.changeColor(Color.RED);
//
//
//        // mPaintView.fillLayer(40, 40, 40);
//        // Canvas effetti = new Canvas(mutableBitmap);
//        // effetti.drawColor(Color.CYAN);
//
//
//    }

//    /**
//     * Called when a touch screen event was not handled by any of the views
//     * under it.  This is most useful to process touch events that happen
//     * outside of your window bounds, where there is no view to receive it.
//     *
//     * @param event The touch screen event being processed.
//     * @return Return true if you have consumed the event, false if you haven't.
//     * The default implementation always returns false.
//     */
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//
//        DisplayMetrics coordinate = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(coordinate);
//
//        // ora bisogna far si che le coordinate si spostino a destra o sinistra entro un limite prestabilito.. sarà impossibile xD
//
//
//       // Toast.makeText(getApplicationContext(), (String.valueOf(coordinate.widthPixels)),Toast.LENGTH_SHORT).show();
//        float downXValue = 0;
//        float downYValue = 0;
//        String direction="";
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//
//                valoreLumCont.setVisibility(View.VISIBLE);
//            {
//                // store the X value when the user's finger was pressed down
//                downXValue = event.getX();
//                downYValue = event.getY();
//                Log.v("", "= " + downYValue);
//                break;
//            }
//
//            case MotionEvent.ACTION_MOVE: {
//
//
//                // Get the X value when the user released his/her finger
//                float currentX = event.getX();
//                float currentY = event.getY();
//                // check if horizontal or vertical movement was bigger
//
//
//                valoreLumCont.setText(String.valueOf(currentX-downXValue));
//                if (Math.abs(downXValue - currentX) > Math.abs(downYValue
//                        - currentY)) {
//                    Log.v("", "x");
//                    // going backwards: pushing stuff to the right
//                    if (downXValue < currentX) {
//
//
//                        direction="right";
//
//                    }
//
//                    // going forwards: pushing stuff to the left
//                    if (downXValue > currentX) {
//                        direction="left";
//                    }
//
//                } else {
//                    Log.v("", "y ");
//
//                    if (downYValue < currentY) {
//                        direction="down";
//                    }
//                    if (downYValue > currentY) {
//                        direction="up";
//                    }
//                }
//                break;
//            }
//            case MotionEvent.ACTION_UP:
//            {
//               // Toast.makeText(getApplicationContext(), direction,Toast.LENGTH_SHORT).show();
//                valoreLumCont.setVisibility(View.INVISIBLE);
//
//                break;
//            }
//
//        }
//
//        return super.onTouchEvent(event);
//    }

//    @Override
//    public void onPictureSaved(Uri uri) {
//        Toast.makeText(getApplicationContext(), "Saved: " + uri.toString(), Toast.LENGTH_SHORT).show();
//        // Uri temp = Uri.parse("content://media/external/images/media/temp.jpg");
//
//
//        String filePath = Environment.getExternalStorageDirectory() + "/Pictures/GalleryApp/temp.jpg";
//
//        File file = new File(filePath);
//
//
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
////        mGPUImage.getGPUImage().deleteImage();
////        mGPUImage.setImage(bitmap);
//        rotella.setRadiansAngle(0);
//
//        mGPUImage.requestRender();
//
//    }


//    @Override
//    public void onSelectionChanged(int i) {
//        // Log.d(TAG, "onSelectionChanged: " + position);
//
//    }
//
//    @Override
//    public void onVisibilityChanged(View view, boolean b) {
//        //Log.d(TAG, "onVisibilityChanged: " + view + ", " + visible);
//
//    }


//    private class ApplicaEffettiTask extends AsyncTask<Float, Float, Float> {
//
//        /**
//         * Override this method to perform a computation on a background thread. The
//         * specified parameters are the parameters passed to {@link #execute}
//         * by the caller of this task.
//         * <p/>
//         * This method can call {@link #publishProgress} to publish updates
//         * on the UI thread.
//         *
//         * @param params The parameters of the task.
//         * @return A result, defined by the subclass of this task.
//         * @see #onPreExecute()
//         * @see #onPostExecute
//         * @see #publishProgress
//         */
//        @SuppressWarnings("WrongThread")
//        @Override
//        protected Float doInBackground(Float... params) {
//
//            int value = params[0].intValue();
//
//
//            // 0 luminosità
//            // 1 contrasto
//            // 2 cambia il blu
//            // 3 cambia il verde
//            // 4 cambia il rosso
//            switch (value) {
//                case 0:
//                    cambiaLuminositaContrasto(params[1], true);
//                    //bMap = mGPUImage.getBitmapWithFilterApplied();
//                    break;
//                case 1:
//                    cambiaLuminositaContrasto(params[1], false);
//                    // bMap = mGPUImage.getBitmapWithFilterApplied();
//                    break;
//                case 2:
//                    mGPUImage.setFilter(new GPUImageRGBFilter(rosso, verde, params[1]));
//                    //.setFilter(new GPUImageRGBFilter(rosso, verde, params[1]));
//                    // GPUImageRGBFilter(params[1]));
//                    // bMap = mGPUImage.getBitmapWithFilterApplied();
//                    break;
//                case 3:
//                    mGPUImage.setFilter(new GPUImageRGBFilter(rosso, params[1], blu));
//
//                    //  bMap = mGPUImage.getBitmapWithFilterApplied();
//                    break;
//                case 4:
//                    mGPUImage.setFilter(new GPUImageRGBFilter(params[1], verde, blu));
//
//                    // bMap = mGPUImage.getBitmapWithFilterApplied();
//                    break;
//
//
//            }
//
//
//            return null;
//        }
//
//        /**
//         * <p>Runs on the UI thread after {@link #doInBackground}. The
//         * specified result is the value returned by {@link #doInBackground}.</p>
//         * <p/>
//         * <p>This method won't be invoked if the task was cancelled.</p>
//         *
//         * @param _float The result of the operation computed by {@link #doInBackground}.
//         * @see #onPreExecute
//         * @see #doInBackground
//         * @see #onCancelled(Object)
//         */
//        @Override
//        protected void onPostExecute(Float _float) {
//            super.onPostExecute(_float);
//            mGPUImage.requestRender();
//
//
//            //imageView.setImageBitmap(bMap);
//
//        }
//    }


    private void setupListener() {


        rotella.setEndLock(true);

        rotella.setListener(new HorizontalWheelView.Listener() {
            @Override
            public void onRotationChanged(double radians) {
                rotellaTextView.setText(String.valueOf(radians));

                if (applicaLuminosita) {
                    mGPUImage.setFilter(new GPUImageExposureFilter((float) radians));
                    radiantLuminosità = radians;
                }

            }

            @Override
            public void onScrollStateChanged(int state) {
                super.onScrollStateChanged(state);

            }

        });

        rotellaContrasto.setEndLock(true);


        rotellaContrasto.setListener(new HorizontalWheelView.Listener() {
            @Override
            public void onRotationChanged(double radians) {
                rotellaTextView.setText(String.valueOf(radians));

                if (applicaContrasto) {
                    mGPUImage.setFilter(new GPUImageContrastFilter((float) radians + 1));
                    radiantContrasto = radians;
                }

            }

            @Override
            public void onScrollStateChanged(int state) {
                super.onScrollStateChanged(state);

            }
        });

        rotellaSaturazione.setEndLock(true);

        rotellaSaturazione.setListener(new HorizontalWheelView.Listener() {
            @Override
            public void onRotationChanged(double radians) {
                rotellaTextView.setText(String.valueOf(radians));

                if (applicaSaturazione) {
                    mGPUImage.setFilter(new GPUImageSaturationFilter((float) radians));
                    radiantSaturazione = radians;
                }

            }

            @Override
            public void onScrollStateChanged(int state) {
                super.onScrollStateChanged(state);

            }
        });

//        rotellaTextView = ((TextView) findViewById(R.id.rotellaTextView));
        // ((HorizontalProgressWheelView) findViewById(R.id.rotella))


    }

    public void setFilter(int id) {

        switch (id) {
            case 1:


        }
    }

}
