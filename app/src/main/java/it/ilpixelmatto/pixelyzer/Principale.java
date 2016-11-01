package it.ilpixelmatto.pixelyzer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class Principale extends AppCompatActivity {
    //private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 2;
    private static final int RESULT_LOAD_IMG = 1, CROPIMAGE = 2;

    private Button fotocameraButton;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";


    private TitanicTextView titanicTextView;
    private Titanic titanic;
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    private String uri;
    private ImageView imageView;

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        setContentView(R.layout.activity_principale);
    titanicTextView = (TitanicTextView) findViewById(R.id.titanic_tv);

        titanic = new Titanic();
        titanic.start(titanicTextView);

        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);

        animatedCircleLoadingView.startIndeterminate();

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);

        fotocameraButton = (Button) findViewById(R.id.FotocameraButton);



        // controlliamo se la permission è concessa
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // se arriviamo qui è perchè la permission non è stata ancora concessa
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // mostriamo ulteriori informazioni all'utente riguardante l'uso della permission nell'app ed eventualmente richiediamo la permission
            } else {

                // se siamo qui è perchè non si è mostrata alcuna spiegazione all'utente, richiesta di permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
            }
        }

        fotocameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log.e(TAG, "mBtnShot clicked");

               // animatedCircleLoadingView.stopOk();


                apriFoto();

            }
        });




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Il permesso è necessario per il funzionamento dell'app", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void apriFoto() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), RESULT_LOAD_IMG);



    }

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

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
//            ModifyPhotoUI.startWithUri(this, resultUri);
            SingleImageActivity.startWithUri(this, resultUri);
        } else {
            Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show();
        }
    }


    private void performCrop(Uri data) {

        uri = SAMPLE_CROPPED_IMAGE_NAME;
        uri += "jpg";
        UCrop ucrop = UCrop.of(data, Uri.fromFile(new File(getCacheDir(), uri)))
                .withAspectRatio(1, 1);
        ucrop.start(this);

    }


}
