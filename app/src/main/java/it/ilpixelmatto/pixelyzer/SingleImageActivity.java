//CLIENT SECRET 774a3b21-89ef-4fec-97c5-fdf71b15e53d
//CLIENT ID (DEVELOPMENT MODE)* 06eede89c2474cfb96f7bdc72bcfe058

package it.ilpixelmatto.pixelyzer;


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
    private float luminosita = 0, prevRadians, prevRadiansContrasto, prevRadiansSaturazione, contrasto = 0, saturazione=0, rosso = 1, verde = 1, blu = 1;
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
        if (mFilter == null || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;


            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
            mGPUImage.setFilter(bMapUtils.addFilter(mFilter));
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
                bMapUtils.removeInstaFilter();
                if (bMapUtils.getLenghFilterGroup()==0)
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

            //    rotella.setRadiansAngle(radiantLuminosità);

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
               // rotellaContrasto.setRadiansAngle(radiantContrasto);
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
               // mGPUImage.setFilter(bMapUtils.lala());

                rotella.setRadiansAngle(0);
                luminosita=0;
                prevRadians=0;
                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageExposureFilter(0)));

                break;
            case R.id.resetRotellaContrasto:
                mSmallBang.bang(v);
                // mGPUImage.setFilter(bMapUtils.lala());

                rotellaContrasto.setRadiansAngle(0);
                contrasto=0;
                prevRadiansContrasto=0;
                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageContrastFilter(1)));

                break;
            case R.id.resetRotellaSaturazione:
                mSmallBang.bang(v);
                // mGPUImage.setFilter(bMapUtils.lala());

                rotellaSaturazione.setRadiansAngle(0);
                saturazione=0;
                prevRadiansSaturazione=0;
                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageSaturationFilter(1)));

                break;



        }
    }




    private void setupListener() {



        rotella.setEndLock(true);

        rotella.setListener(new HorizontalWheelView.Listener() {
            @Override
            public void onRotationChanged(double radians) {
                String temp="0";
                if(radians!=0)
                    temp = String.valueOf(radians).substring(0, 4);
                else
                    temp ="0";

                rotellaTextView.setText(temp);

                if((Math.abs(radians)-Math.abs(prevRadians)>0.5) || (Math.abs(radians)-Math.abs(prevRadians)<-0.5))
                {
                    if(radians>0) {
                        if (Math.abs(radians) - Math.abs(prevRadians) > 0.5) {
                            luminosita += 0.1;
                            if (applicaLuminosita) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageExposureFilter(luminosita)));
                            }
                        } else {
                            luminosita -= 0.1;
                            if (applicaLuminosita) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageExposureFilter(luminosita)));
                            }
                        }
                    }
                    else {
                        if(Math.abs(radians)-Math.abs(prevRadians)>0.2) {
                            luminosita -= 0.1;
                            if (applicaLuminosita) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageExposureFilter(luminosita)));
                            }
                        }
                        else {
                            luminosita += 0.1;
                            if (applicaLuminosita) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageExposureFilter(luminosita)));
                            }
                        }
                    }
                    prevRadians=(float)radians;

                }


//                if (applicaLuminosita) {
//
//
//                        mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageExposureFilter(luminosita)));
//
////                    mGPUImage.requestRender();
////                    mGPUImage.setFilter(new GPUImageExposureFilter((float) radians));
//                    radiantLuminosità = radians;
//                }

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
                String temp = "0";
                if (radians != 0)
                    temp = String.valueOf(radians).substring(0, 4);
                else
                    temp = "0";

                rotellaTextView.setText(temp);

                if ((Math.abs(radians) - Math.abs(prevRadiansContrasto) > 0.5) || (Math.abs(radians) - Math.abs(prevRadiansContrasto) < -0.5)) {
                    if (radians > 0) {
                        if (Math.abs(radians) - Math.abs(prevRadiansContrasto) > 0.5) {
                            contrasto += 0.1;
                            if (applicaContrasto) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageContrastFilter(contrasto + 1)));
                            }
                        } else {
                            contrasto -= 0.1;
                            if (applicaContrasto) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageContrastFilter(contrasto + 1)));
                            }
                        }
                    } else {
                        if (Math.abs(radians) - Math.abs(prevRadiansContrasto) > 0.2) {
                            contrasto -= 0.1;
                            if (applicaContrasto) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageContrastFilter(contrasto + 1)));
                            }
                        } else {
                            contrasto += 0.1;
                            if (applicaContrasto) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageContrastFilter(contrasto + 1)));
                            }
                        }
                    }
                    prevRadiansContrasto = (float) radians;

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
                String temp = "0";
                if (radians != 0)
                    temp = String.valueOf(radians).substring(0, 4);
                else
                    temp = "0";

                rotellaTextView.setText(temp);

                if ((Math.abs(radians) - Math.abs(prevRadiansSaturazione) > 0.5) || (Math.abs(radians) - Math.abs(prevRadiansSaturazione) < -0.5)) {
                    if (radians > 0) {
                        if (Math.abs(radians) - Math.abs(prevRadiansSaturazione) > 0.5) {
                            saturazione += 0.1;
                            if (applicaSaturazione) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageSaturationFilter(saturazione+1)));
                            }
                        } else {
                            saturazione -= 0.1;
                            if (applicaSaturazione) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageSaturationFilter(saturazione+1)));
                            }
                        }
                    } else {
                        if (Math.abs(radians) - Math.abs(prevRadiansSaturazione) > 0.2) {
                            saturazione -= 0.1;
                            if (applicaSaturazione) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageSaturationFilter(saturazione+1)));
                            }
                        } else {
                            saturazione += 0.1;
                            if (applicaSaturazione) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageSaturationFilter(saturazione+1)));
                            }
                        }
                    }
                    Log.wtf("MERDA", ("saturazione: " + String.valueOf(saturazione) + ", rad: " + prevRadiansSaturazione));
                    prevRadiansSaturazione = (float) radians;

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



}
