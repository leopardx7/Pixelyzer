package it.ilpixelmatto.Pixelyzer;

import android.Manifest;
import android.app.Activity;
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

import com.yalantis.ucrop.UCrop;

import java.io.File;

public class Principale extends AppCompatActivity {
    //private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 2;
    private static final int RESULT_LOAD_IMG = 1, CROPIMAGE = 2;
    private String imgDecodableString;
    Uri imageUri; // imageUri contiene uri dell'immagine da croppare
    Uri imageCropped;
    private Activity act;
    private static final int TIMER_LENGTH = 30;
    private PaintView mPaintView;
    private Button fotocameraButton;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    private String uri;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act = this;


        imageView = (ImageView) findViewById(R.id.prova);
        setContentView(R.layout.activity_principale);


        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);

        fotocameraButton = (Button) findViewById(R.id.FotocameraButton);
        // mPaintView = (PaintView) findViewById(R.id.animazione);
        //mPaintView.start(TIMER_LENGTH);
        // int statoPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (statoPermission!=0) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
//        }


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

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.CAMERA)) {
//
//                    // Show an expanation to the user *asynchronously* -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//
//                } else {
//
//                    // No explanation needed, we can request the permission.
//
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.CAMERA},
//                            MY_PERMISSIONS_REQUEST_CAMERA);
//
//                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                    // app-defined int constant. The callback method gets the
//                    // result of the request.
//                }
//            }
//           // return;
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                    // Show an expanation to the user *asynchronously* -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//
//                } else {
//
//                    // No explanation needed, we can request the permission.
//
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            MY_PERMISSIONS_REQUEST_STORAGE);
//
//                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                    // app-defined int constant. The callback method gets the
//                    // result of the request.
//                }
//            }
//            //return;
//        }
//
        fotocameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log.e(TAG, "mBtnShot clicked");

                apriFoto();
                //Intent intent = new Intent(Principale.this, GalleryPicker.class);
                //startActivity(intent);
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


//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        // Start the Intent
//        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
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


//
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                // Get the cursor
//                Cursor cursor = getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//                // Move to first row
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imgDecodableString = cursor.getString(columnIndex);
//                cursor.close();
//
//                // Get the Image from data
//                imageUri = data.getData();
//                //carry out the crop operation
            //  performCrop();

            //ImageView imgView = (ImageView) findViewById(R.id.imgView);
            // Set the Image in ImageView after decoding the String
            // imgView.setImageBitmap(BitmapFactory
            //       .decodeFile(imgDecodableString));

            //    }

//            if (requestCode == CROPIMAGE){
//
//
//                Bundle extras = data.getExtras();
//                //get the cropped bitmap
//                Bitmap thePic = extras.getParcelable("data");
//
//
//
//                //Write file
//                String filename = "bitmap.png";
//                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
//                assert thePic != null;
//                thePic.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//                //Cleanup
//                stream.close();
//                thePic.recycle();
//
//                Intent sendFileIntent = new Intent(this, SingleImageActivity/*ModifyPhotoUI*/.class);
//                sendFileIntent.putExtra("MyPhoto",filename);
//                startActivity(sendFileIntent);
//
//
//            }

            /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                   Uri resultUri = result.getUri();
                    imageView.setImageURI(null);
                    imageView.setImageURI(resultUri);
                    //Bitmap bMap = result.getBitmap();


                    Intent sendFileIntent = new Intent(this, SingleImageActivity*//*ModifyPhotoUI*//*.class);
                    sendFileIntent.putExtra("MyPhoto",resultUri);
                    startActivity(sendFileIntent);
                   // Toast.makeText(getApplicationContext(),  resultUri.toString(), Toast.LENGTH_SHORT).show();

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }*/
//
//            if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
//                final Uri resultUri = UCrop.getOutput(data);
//                Intent sendFileIntent = new Intent(this, SingleImageActivity/*ModifyPhotoUI*/.class);
//                sendFileIntent.putExtra("MyPhoto",resultUri);
//                startActivity(sendFileIntent);
//
//
//            } else if (resultCode == UCrop.RESULT_ERROR) {
//                final Throwable cropError = UCrop.getError(data);
//            }

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
        /*try {

            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(imageUri, "image*//*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROPIMAGE);

        }
        catch(ActivityNotFoundException e){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }*/
    }


}


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            // When an Image is picked
//            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
//                    && null != data) {
//                // Get the Image from data
//
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                // Get the cursor
//                Cursor cursor = getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//                // Move to first row
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imgDecodableString = cursor.getString(columnIndex);
//                cursor.close();
//                Intent sendFileIntent = new Intent(this, SingleImageActivityModifyPhotoUI.class);
//                sendFileIntent.putExtra("MyPhoto", imgDecodableString);
//                startActivity(sendFileIntent);
//                //ImageView imgView = (ImageView) findViewById(R.id.imgView);
//                // Set the Image in ImageView after decoding the String
//                // imgView.setImageBitmap(BitmapFactory
//                //       .decodeFile(imgDecodableString));
//            } else {
//                Toast.makeText(this, "You haven't picked Image",
//                        Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
//                    .show();
//        }
//    }