//CLIENT SECRET 774a3b21-89ef-4fec-97c5-fdf71b15e53d
//CLIENT ID (DEVELOPMENT MODE)* 06eede89c2474cfb96f7bdc72bcfe058

package it.ilpixelmatto.pixelyzer;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.shchurov.horizontalwheelview.HorizontalWheelView;
import com.hanks.htextview.HTextView;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;
import me.wangyuwei.flipshare.FlipShareView;
import me.wangyuwei.flipshare.ShareItem;
import xyz.hanks.library.SmallBang;



/**
 * Created by Riccardo Russo on 30/06/2016.
 */
public class SingleImageActivity extends AppCompatActivity implements View.OnClickListener/*OverMenuView.OnSelectionChangeListener*/ {


    private Bitmap bMap;
    private float luminosita = 0, prevRadians, prevRadiansContrasto, prevRadiansSaturazione, prevRadiantsTemperatura, prevRadiantsLuci, prevRadiantsOmbre, luci=0, ombre=0, temperatura =5000, contrasto = 0, saturazione=0;
    private TextView rotellaTextView;
    private HTextView textSelected;
    private HorizontalScrollView filtriScrollView;

    private GPUImageView mGPUImage;
    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    private HorizontalWheelView rotella, rotellaContrasto, rotellaSaturazione, rotellaTemperatura, rotellaLuci;

    private SmallBang mSmallBang; // animazione bottoni

    private int gapRotella = 1;

    private Context context;
    private BitmapLoader bMapUtils; // gestisce le bitmap
    private static final String TAG = "SingleImageActivity";
    private RelativeLayout rotellaLayout;
    private Button resetRotella, resetRotellaContrasto, resetRotellaSaturazione, resetRotellaTemperatura, resetRotellaLuci, done;
    private boolean applicaLuminosita = false, applicaContrasto = false, applicaSaturazione = false, applicaTemperatura = false, applicaLuci=false;

    private ImageView selectLuci, selectTemperatura, selectContrasto, selectSaturazione, selectFiltri, selectLuminosita;
    private Uri imageUri;
    private FlipShareView shareView;

    public static void startWithUri(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(context, SingleImageActivity.class);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mGPUImage = new GPUImageView(context);


        bMapUtils = new BitmapLoader();


        bMap = BitmapFactory.decodeFile(new File(getIntent().getData().getPath()).getAbsolutePath());


        setContentView(R.layout.activity_single_image);
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);

        mGPUImage = (GPUImageView) findViewById(R.id.visualizzaFotoView);

        mGPUImage.setImage(bMap);
        mGPUImage.requestRender();

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
        findViewById(R.id.filtri).setOnClickListener(this);
        findViewById(R.id.temperatura).setOnClickListener(this);
        findViewById(R.id.luci).setOnClickListener(this);

        selectLuci = (ImageView) findViewById(R.id.selectLuci);
        selectLuci.setOnClickListener(this);
        selectTemperatura = (ImageView) findViewById(R.id.selectTemperatura);
        selectTemperatura.setOnClickListener(this);
        selectContrasto = (ImageView) findViewById(R.id.selectContrasto);
        selectContrasto.setOnClickListener(this);
        selectSaturazione = (ImageView) findViewById(R.id.selectSaturazione);
        selectSaturazione.setOnClickListener(this);
        selectFiltri = (ImageView) findViewById(R.id.selectFiltri);
        selectFiltri.setOnClickListener(this);
        selectLuminosita = (ImageView) findViewById(R.id.selectLuminosita);
        selectLuminosita.setOnClickListener(this);

        textSelected = (HTextView) findViewById(R.id.textSelected);

        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(),  "fonts/font.ttf");

        textSelected.setTypeface(custom_font);
        textSelected.animateText("Pixelyzer");



        done = (Button) findViewById(R.id.done);
        findViewById(R.id.done).setOnClickListener(this);
        rotellaLayout = (RelativeLayout) findViewById(R.id.rotellaLayout);
        rotella = (HorizontalWheelView) findViewById(R.id.rotella);
        rotellaContrasto = (HorizontalWheelView) findViewById(R.id.rotellaContrasto);
        rotellaSaturazione = (HorizontalWheelView) findViewById(R.id.rotellaSaturazione);
        rotellaTemperatura = (HorizontalWheelView) findViewById(R.id.rotellaTemperatura);
        rotellaLuci = (HorizontalWheelView) findViewById(R.id.rotellaLuci);


        resetRotella = (Button) findViewById(R.id.resetRotella);
        resetRotella.setOnClickListener(this);
        resetRotellaContrasto = (Button) findViewById(R.id.resetRotellaContrasto);
        resetRotellaContrasto.setOnClickListener(this);
        resetRotellaSaturazione = (Button) findViewById(R.id.resetRotellaSaturazione);
        resetRotellaSaturazione.setOnClickListener(this);
        resetRotellaTemperatura = (Button) findViewById(R.id.resetRotellaTemperatura);
        resetRotellaTemperatura.setOnClickListener(this);
        resetRotellaLuci = (Button) findViewById(R.id.resetRotellaLuci);
        resetRotellaLuci.setOnClickListener(this);


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
                    textSelected.animateText("Filtri");
                    textSelected.setVisibility(View.VISIBLE);
                    rotella.setVisibility(View.INVISIBLE);
                    rotellaSaturazione.setVisibility(View.INVISIBLE);
                    rotellaTemperatura.setVisibility(View.INVISIBLE);
                    rotellaContrasto.setVisibility(View.INVISIBLE);
                    rotellaLuci.setVisibility(View.INVISIBLE);
                    rotellaTemperatura.setVisibility(View.INVISIBLE);
                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);

                    selectFiltri.setVisibility(View.VISIBLE);
                    rotellaTextView.setVisibility(View.INVISIBLE);
                    resetRotella.setVisibility(View.INVISIBLE);
                    resetRotellaTemperatura.setVisibility(View.INVISIBLE);
                    resetRotellaLuci.setVisibility(View.INVISIBLE);

                    resetRotellaSaturazione.setVisibility(View.INVISIBLE);
                    resetRotellaContrasto.setVisibility(View.INVISIBLE);
                    filtriScrollView.setVisibility(View.VISIBLE);

                } else {
                    textSelected.animateText("Pixelyzer");
                    rotella.setVisibility(View.INVISIBLE);
                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);
                    rotellaTextView.setVisibility(View.INVISIBLE);
                    filtriScrollView.setVisibility(View.INVISIBLE);
                }

                break;
            case R.id.original:

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
            case R.id.temperatura:
                mSmallBang.bang(v);

                if (rotellaTemperatura.getVisibility() == View.INVISIBLE) {
                    textSelected.animateText("Temperatura");
                    textSelected.setVisibility(View.VISIBLE);
                    applicaTemperatura = true;
                    rotella.setVisibility(View.INVISIBLE);
                    rotellaContrasto.setVisibility(View.INVISIBLE);
                    rotellaSaturazione.setVisibility(View.INVISIBLE);
                    rotellaLuci.setVisibility(View.INVISIBLE);

                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);
                    rotellaTextView.setVisibility(View.VISIBLE);

                    String temp;
                    if (rotellaTemperatura.getRadiansAngle() != 0)
                        temp = String.valueOf(rotellaTemperatura.getRadiansAngle()).substring(0, 4);
                    else
                        temp = "0";

                    rotellaTextView.setText(temp);

                    selectTemperatura.setVisibility(View.VISIBLE);
                    rotellaTemperatura.setVisibility(View.VISIBLE);
                    resetRotellaTemperatura.setVisibility(View.VISIBLE);
                    resetRotellaContrasto.setVisibility(View.INVISIBLE);
                    resetRotellaSaturazione.setVisibility(View.INVISIBLE);
                    resetRotellaLuci.setVisibility(View.INVISIBLE);
                    resetRotella.setVisibility(View.INVISIBLE);
                    filtriScrollView.setVisibility(View.INVISIBLE);
                } else {
                    applicaTemperatura = false;
                    textSelected.animateText("Pixelyzer");

                    rotellaTemperatura.setVisibility(View.INVISIBLE);
                    resetRotellaTemperatura.setVisibility(View.INVISIBLE);
                    rotellaTextView.setVisibility(View.INVISIBLE);

                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);
                }


                break;
            case R.id.luminosita:
                mSmallBang.bang(v);

                if (rotella.getVisibility() == View.INVISIBLE) {
                    textSelected.animateText("Luminosità");
                    textSelected.setVisibility(View.VISIBLE);
                    applicaLuminosita = true;
                    rotellaTextView.setVisibility(View.VISIBLE);

                    String temp;
                    if (rotella.getRadiansAngle() != 0)
                        temp = String.valueOf(rotella.getRadiansAngle()).substring(0, 4);
                    else
                        temp = "0";

                    rotellaTextView.setText(temp);
                    rotella.setVisibility(View.VISIBLE);
                    rotellaContrasto.setVisibility(View.INVISIBLE);
                    rotellaLuci.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.VISIBLE);
                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    rotellaSaturazione.setVisibility(View.INVISIBLE);
                    rotellaTemperatura.setVisibility(View.INVISIBLE);
                    resetRotella.setVisibility(View.VISIBLE);
                    resetRotellaContrasto.setVisibility(View.INVISIBLE);
                    resetRotellaSaturazione.setVisibility(View.INVISIBLE);
                    resetRotellaLuci.setVisibility(View.INVISIBLE);
                    resetRotellaTemperatura.setVisibility(View.INVISIBLE);

                    filtriScrollView.setVisibility(View.INVISIBLE);
                } else {
                    applicaLuminosita = false;
                    textSelected.animateText("Pixelyzer");
                    rotellaTextView.setVisibility(View.INVISIBLE);

                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);
                    rotella.setVisibility(View.INVISIBLE);
                    resetRotella.setVisibility(View.INVISIBLE);
                }

                break;

            case R.id.contrasto:

                mSmallBang.bang(v);
                if (rotellaContrasto.getVisibility() == View.INVISIBLE) {
                    textSelected.animateText("Contrasto");
                    textSelected.setVisibility(View.VISIBLE);
                    applicaContrasto = true;
                    rotellaTextView.setVisibility(View.VISIBLE);

                    String temp;
                    if (rotellaContrasto.getRadiansAngle() != 0)
                        temp = String.valueOf(rotellaContrasto.getRadiansAngle()).substring(0, 4);
                    else
                        temp = "0";

                    rotellaTextView.setText(temp);

                    rotellaContrasto.setVisibility(View.VISIBLE);
                    rotellaLuci.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.VISIBLE);
                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);
                    rotella.setVisibility(View.INVISIBLE);
                    rotellaTemperatura.setVisibility(View.INVISIBLE);
                    rotellaSaturazione.setVisibility(View.INVISIBLE);
                    resetRotellaContrasto.setVisibility(View.VISIBLE);
                    resetRotella.setVisibility(View.INVISIBLE);
                    resetRotellaLuci.setVisibility(View.INVISIBLE);
                    resetRotellaTemperatura.setVisibility(View.INVISIBLE);
                    resetRotellaSaturazione.setVisibility(View.INVISIBLE);

                    filtriScrollView.setVisibility(View.INVISIBLE);

                } else {
                    applicaContrasto = false;
                    textSelected.animateText("Pixelyzer");
                    rotellaTextView.setVisibility(View.INVISIBLE);

                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);
                    rotellaContrasto.setVisibility(View.INVISIBLE);
                    resetRotellaContrasto.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.saturazione:

                mSmallBang.bang(v);
                if (rotellaSaturazione.getVisibility() == View.INVISIBLE) {
                    textSelected.animateText("Saturazione");
                    textSelected.setVisibility(View.VISIBLE);
                    applicaSaturazione = true;
                    rotellaLuci.setVisibility(View.INVISIBLE);

                    selectSaturazione.setVisibility(View.VISIBLE);
                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);
                    rotellaTextView.setVisibility(View.VISIBLE);

                    String temp;
                    if (rotellaSaturazione.getRadiansAngle() != 0)
                        temp = String.valueOf(rotellaSaturazione.getRadiansAngle()).substring(0, 4);
                    else
                        temp = "0";

                    rotellaTextView.setText(temp);

                    rotella.setVisibility(View.INVISIBLE);
                    rotellaContrasto.setVisibility(View.INVISIBLE);
                    rotellaSaturazione.setVisibility(View.VISIBLE);
                    rotellaTemperatura.setVisibility(View.INVISIBLE);
                    resetRotellaSaturazione.setVisibility(View.VISIBLE);
                    resetRotellaContrasto.setVisibility(View.INVISIBLE);
                    resetRotella.setVisibility(View.INVISIBLE);
                    resetRotellaLuci.setVisibility(View.INVISIBLE);
                    resetRotellaTemperatura.setVisibility(View.INVISIBLE);

                    filtriScrollView.setVisibility(View.INVISIBLE);

                } else {
                    applicaSaturazione = false;
                    textSelected.animateText("Pixelyzer");
                    rotellaTextView.setVisibility(View.INVISIBLE);

                    rotellaSaturazione.setVisibility(View.INVISIBLE);
                    resetRotellaSaturazione.setVisibility(View.INVISIBLE);
                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);
                }

                break;

            case R.id.luci:
                mSmallBang.bang(v);

                if (rotellaLuci.getVisibility() == View.INVISIBLE) {
                    textSelected.animateText("Luci");
                    textSelected.setVisibility(View.VISIBLE);
                    applicaLuci = true;
                    rotellaLuci.setVisibility(View.VISIBLE);
                    rotella.setVisibility(View.INVISIBLE);
                    rotellaTextView.setVisibility(View.VISIBLE);

                    String temp;
                    if (rotellaLuci.getRadiansAngle() != 0)
                        temp = String.valueOf(rotellaLuci.getRadiansAngle()).substring(0, 4);
                    else
                        temp = "0";

                    rotellaTextView.setText(temp);

                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.VISIBLE);
                    rotellaContrasto.setVisibility(View.INVISIBLE);
                    rotellaSaturazione.setVisibility(View.INVISIBLE);
                    rotellaTemperatura.setVisibility(View.INVISIBLE);
                    resetRotellaLuci.setVisibility(View.VISIBLE);
                    resetRotellaContrasto.setVisibility(View.INVISIBLE);
                    resetRotellaSaturazione.setVisibility(View.INVISIBLE);
                    resetRotella.setVisibility(View.INVISIBLE);
                    resetRotellaTemperatura.setVisibility(View.INVISIBLE);

                    filtriScrollView.setVisibility(View.INVISIBLE);
                } else {
                    applicaLuci = false;
                    textSelected.animateText("Pixelyzer");
                    rotellaTextView.setVisibility(View.INVISIBLE);

                    rotellaLuci.setVisibility(View.INVISIBLE);
                    resetRotellaLuci.setVisibility(View.INVISIBLE);
                    selectTemperatura.setVisibility(View.INVISIBLE);
                    selectContrasto.setVisibility(View.INVISIBLE);
                    selectLuci.setVisibility(View.INVISIBLE);
                    selectSaturazione.setVisibility(View.INVISIBLE);
                    selectFiltri.setVisibility(View.INVISIBLE);
                    selectLuminosita.setVisibility(View.INVISIBLE);
                }


                break;


            case R.id.done:
                textSelected.animateText("Pixelyzer");
                applicaLuminosita = false; //non cambio più il valore dell'esposizione
                applicaLuci = false;
                applicaLuminosita = applicaSaturazione = applicaContrasto = false;
                mSmallBang.bang(v);
                rotella.setVisibility(View.INVISIBLE);
                rotellaContrasto.setVisibility(View.INVISIBLE);
                rotellaSaturazione.setVisibility(View.INVISIBLE);
                rotellaTemperatura.setVisibility(View.INVISIBLE);
                rotellaLuci.setVisibility(View.INVISIBLE);
                selectTemperatura.setVisibility(View.INVISIBLE);
                selectContrasto.setVisibility(View.INVISIBLE);
                selectLuci.setVisibility(View.INVISIBLE);
                selectSaturazione.setVisibility(View.INVISIBLE);
                selectFiltri.setVisibility(View.INVISIBLE);
                selectLuminosita.setVisibility(View.INVISIBLE);
                resetRotella.setVisibility(View.INVISIBLE);
                resetRotellaContrasto.setVisibility(View.INVISIBLE);
                resetRotellaSaturazione.setVisibility(View.INVISIBLE);
                resetRotellaLuci.setVisibility(View.INVISIBLE);
                resetRotellaTemperatura.setVisibility(View.INVISIBLE);
                rotellaTextView.setVisibility(View.INVISIBLE);

                filtriScrollView.setVisibility(View.INVISIBLE);

                shareView = new FlipShareView.Builder(this, done)
                        .addItem(new ShareItem("Salva", Color.WHITE, 0xff795548, BitmapFactory.decodeResource(getResources(), R.drawable.ic_save_white_48dp)))
                        .addItem(new ShareItem("Facebook", Color.WHITE, 0xff43549C, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_facebook)))
                        .addItem(new ShareItem("Twitter", Color.WHITE, 0xff4999F0, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_twitter)))
                        .addItem(new ShareItem("Google+", Color.WHITE, 0xffD9392D, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_google)))
                        .addItem(new ShareItem("Whatsapp", Color.WHITE, 0xff27ae60, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_whatsapp)))
                        .addItem(new ShareItem("Tumblr", Color.WHITE, 0xff2980b9, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_tumblr)))
                        .addItem(new ShareItem("Instagram", Color.WHITE, 0xffAB47BC, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_instagram)))
                        .addItem(new ShareItem("Other...", Color.WHITE, 0xffEF6C00, BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_white_48dp)))
                        .setItemDuration(200)
                        .setBackgroundColor(0x60000000)
                        .setAnimType(FlipShareView.TYPE_SLIDE)
                        .create();
                shareView.setOnFlipClickListener(new FlipShareView.OnFlipClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        mGPUImage.saveToPictures("Pixelyzer", "Pizelyzer_" + DateFormat.getDateTimeInstance().format(new Date()) + ".jpg", bMap.getWidth(), bMap.getHeight(), new GPUImageView.OnPictureSavedListener() {
                            @Override
                            public void onPictureSaved(Uri uri) {

                                imageUri = uri;

                            }
                        });
                        Intent shareIntent;
                        switch (position) {

                            case 0:
                                Toast.makeText(getApplicationContext(), "Foto salvata", Toast.LENGTH_SHORT).show();

                                break;
                            case 6:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "via @PixelyzerApp");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.instagram.android");
                                startActivity(shareIntent);
                                break;
                            case 1:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "via @PixelyzerApp");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.facebook.katana");
                                startActivity(shareIntent);
                                break;
                            case 2:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "via @PixelyzerApp");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.twitter.android");
                                startActivity(shareIntent);
                                break;
                            case 5:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "via @PixelyzerApp");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.tumblr");
                                startActivity(shareIntent);
                                break;
                            case 3:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "via @PixelyzerApp");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.google.android.apps.plus");
                                startActivity(shareIntent);
                                break;
                            case 4:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "via @PixelyzerApp");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.whatsapp");
                                startActivity(shareIntent);
                                break;
                            case 7:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "via @PixelyzerApp");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                startActivity(shareIntent);
                                break;

                        }
                    }

                    @Override
                    public void dismiss() {
                    }
                });


                break;
            case R.id.resetRotella:
                mSmallBang.bang(v);
                rotella.setRadiansAngle(0);
                luminosita=0;
                prevRadians=0;
                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageExposureFilter(0)));

                break;
            case R.id.resetRotellaContrasto:
                mSmallBang.bang(v);
                rotellaContrasto.setRadiansAngle(0);
                contrasto=0;
                prevRadiansContrasto=0;
                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageContrastFilter(1)));

                break;
            case R.id.resetRotellaSaturazione:
                mSmallBang.bang(v);
                rotellaSaturazione.setRadiansAngle(0);
                saturazione=0;
                prevRadiansSaturazione=0;
                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageSaturationFilter(1)));

                break;

            case R.id.resetRotellaTemperatura:
                mSmallBang.bang(v);
                rotellaTemperatura.setRadiansAngle(0);
                temperatura=5000;
                prevRadiantsTemperatura=0;
                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageWhiteBalanceFilter(5000, 0)));

                break;
            case R.id.resetRotellaLuci:
                mSmallBang.bang(v);
                rotellaLuci.setRadiansAngle(0);
                luci=0;
                prevRadiantsLuci=0;
                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageBrightnessFilter(luci)));

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

                if((Math.abs(radians)-Math.abs(prevRadians)>gapRotella) || (Math.abs(radians)-Math.abs(prevRadians)<-gapRotella))
                {
                    if(radians>0) {
                        if (Math.abs(radians) - Math.abs(prevRadians) > gapRotella) {
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
                        if(Math.abs(radians)-Math.abs(prevRadians)>gapRotella) {
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

                if ((Math.abs(radians) - Math.abs(prevRadiansContrasto) > gapRotella) || (Math.abs(radians) - Math.abs(prevRadiansContrasto) < -gapRotella)) {
                    if (radians > 0) {
                        if (Math.abs(radians) - Math.abs(prevRadiansContrasto) > gapRotella) {
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
                        if (Math.abs(radians) - Math.abs(prevRadiansContrasto) > gapRotella) {
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

                if ((Math.abs(radians) - Math.abs(prevRadiansSaturazione) > gapRotella) || (Math.abs(radians) - Math.abs(prevRadiansSaturazione) < -gapRotella)) {
                    if (radians > 0) {
                        if (Math.abs(radians) - Math.abs(prevRadiansSaturazione) > gapRotella) {
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
                        if (Math.abs(radians) - Math.abs(prevRadiansSaturazione) > gapRotella) {
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
                    prevRadiansSaturazione = (float) radians;
                }
            }

            @Override
            public void onScrollStateChanged(int state) {
                super.onScrollStateChanged(state);

            }
        });

        rotellaTemperatura.setEndLock(true);

        rotellaTemperatura.setListener(new HorizontalWheelView.Listener() {
            @Override
            public void onRotationChanged(double radians) {
                String temp = "0";
                if (radians != 0)
                    temp = String.valueOf(radians).substring(0, 4);
                else
                    temp = "0";

                rotellaTextView.setText(temp);

                if ((Math.abs(radians) - Math.abs(prevRadiantsTemperatura) > gapRotella) || (Math.abs(radians) - Math.abs(prevRadiantsTemperatura) < -gapRotella)) {
                    if (radians > 0) {
                        if (Math.abs(radians) - Math.abs(prevRadiantsTemperatura) > gapRotella) {
                            temperatura += 200;
                            if (applicaTemperatura) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageWhiteBalanceFilter(temperatura, 0)));
                            }
                        } else {
                            temperatura -= 200;
                            if (applicaTemperatura) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageWhiteBalanceFilter(temperatura, 0)));
                            }
                        }
                    } else {
                        if (Math.abs(radians) - Math.abs(prevRadiantsTemperatura) > gapRotella) {
                            temperatura -= 200;
                            if (applicaTemperatura) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageWhiteBalanceFilter(temperatura, 0)));
                            }
                        } else {
                            temperatura += 200;
                            if (applicaTemperatura) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageWhiteBalanceFilter(temperatura, 0)));
                            }
                        }
                    }
                    prevRadiantsTemperatura = (float) radians;
                }
            }

            @Override
            public void onScrollStateChanged(int state) {
                super.onScrollStateChanged(state);

            }
        });
        rotellaLuci.setEndLock(true);
        rotellaLuci.setListener(new HorizontalWheelView.Listener() {
            @Override
            public void onRotationChanged(double radians) {
                String temp = "0";
                if (radians != 0)
                    temp = String.valueOf(radians).substring(0, 4);
                else
                    temp = "0";

                rotellaTextView.setText(temp);

                if ((Math.abs(radians) - Math.abs(prevRadiantsLuci) > gapRotella) || (Math.abs(radians) - Math.abs(prevRadiantsLuci) < -gapRotella)) {
                    if (radians > 0) {
                        if (Math.abs(radians) - Math.abs(prevRadiantsLuci) > gapRotella) {
                            luci += 0.1;
                            if (applicaLuci) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageBrightnessFilter(luci)));
                            }
                        } else {
                            luci -= 0.1;
                            if (applicaLuci) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageBrightnessFilter(luci)));
                            }
                        }
                    } else {
                        if (Math.abs(radians) - Math.abs(prevRadiantsLuci) > gapRotella) {
                            luci -= 0.1;
                            if (applicaLuci) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageBrightnessFilter(luci)));
                            }
                        } else {
                            luci += 0.1;
                            if (applicaLuci) {
                                mGPUImage.setFilter(bMapUtils.addFilter(new GPUImageBrightnessFilter(luci)));
                            }
                        }
                    }
                    prevRadiantsLuci = (float) radians;
                }
            }
            @Override
            public void onScrollStateChanged(int state) {
                super.onScrollStateChanged(state);
            }
        });
    }
}
