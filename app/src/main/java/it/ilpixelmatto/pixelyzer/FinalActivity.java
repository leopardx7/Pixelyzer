package it.ilpixelmatto.pixelyzer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import in.championswimmer.libsocialbuttons.fabs.FABFacebook;
import in.championswimmer.libsocialbuttons.fabs.FABGoogleplus;
import in.championswimmer.libsocialbuttons.fabs.FABInstagram;
import in.championswimmer.libsocialbuttons.fabs.FABTumblr;
import in.championswimmer.libsocialbuttons.fabs.FABTwitter;
import in.championswimmer.libsocialbuttons.fabs.FABWhatsapp;
import me.wangyuwei.flipshare.FlipShareView;
import me.wangyuwei.flipshare.ShareItem;

public class FinalActivity extends Activity implements View.OnClickListener {

    private Bitmap bMap;
    private ImageView imageView;
    private Uri imageUri;
    private FABInstagram instagram;
    private FABFacebook facebook;
    private FABGoogleplus gplus;
    private FABTumblr tumblr;
    private FABTwitter twitter;
    private FABWhatsapp whatsapp;
    private ImageButton share;
    private FlipShareView shareView;

    public static void startWithUri(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(context, FinalActivity.class);
        intent.setData(uri);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);





        imageUri = getIntent().getData();
        try {
            bMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView = (ImageView) findViewById(R.id.imageViewFinal);

        instagram = (FABInstagram) findViewById(R.id.instagram);
        instagram.setOnClickListener(this);
        facebook = (FABFacebook) findViewById(R.id.facebook);
        facebook.setOnClickListener(this);
        gplus = (FABGoogleplus) findViewById(R.id.gplus);
        gplus.setOnClickListener(this);
        whatsapp = (FABWhatsapp) findViewById(R.id.whatsapp);
        whatsapp.setOnClickListener(this);
        twitter = (FABTwitter) findViewById(R.id.twitter);
        twitter.setOnClickListener(this);
        tumblr = (FABTumblr) findViewById(R.id.tumblr);
        tumblr.setOnClickListener(this);

        share = (ImageButton) findViewById(R.id.share);
        share.setOnClickListener(this);

        imageView.setImageBitmap(bMap);



    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.share:


                shareView = new FlipShareView.Builder(this, share)
                        .addItem(new ShareItem("Facebook", Color.WHITE, 0xff43549C, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_facebook)))
                        .addItem(new ShareItem("Twitter", Color.WHITE, 0xff4999F0, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_twitter)))
                        .addItem(new ShareItem("Google+", Color.WHITE, 0xffD9392D, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_google)))
                        .addItem(new ShareItem("Whatsapp", Color.WHITE, 0xff27ae60, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_whatsapp)))
                        .addItem(new ShareItem("Tumblr", Color.WHITE, 0xff2980b9, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_tumblr)))
                        .addItem(new ShareItem("Instagram", Color.WHITE, 0xffffd965, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_instagram)))
                        .addItem(new ShareItem("Other...", Color.WHITE, 0xffffd965, BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_white_48dp)))


                        .setItemDuration(200)
                        .setBackgroundColor(0x60000000)
                        .setAnimType(FlipShareView.TYPE_SLIDE)
                        .create();
                shareView.setOnFlipClickListener(new FlipShareView.OnFlipClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        Intent shareIntent;
                        Toast.makeText(FinalActivity.this, "position " + position + " is clicked.", Toast.LENGTH_SHORT).show();
                        switch (position) {

                            case 5:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT TO SHARE IN INSTAGRAM");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.instagram.android");
                                startActivity(shareIntent);
                                break;
                            case 0:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT TO SHARE IN INSTAGRAM");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.facebook.katana");
                                startActivity(shareIntent);
                                break;
                            case 1:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT TO SHARE IN INSTAGRAM");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.twitter.android");
                                startActivity(shareIntent);
                                break;
                            case 4:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT TO SHARE IN INSTAGRAM");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.tumblr");
                                startActivity(shareIntent);
                                break;
                            case 2:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT TO SHARE IN INSTAGRAM");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.google.android.apps.plus");
                                startActivity(shareIntent);
                                break;
                            case 3:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT TO SHARE IN INSTAGRAM");
                                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                                shareIntent.setPackage("com.whatsapp");
                                startActivity(shareIntent);
                                break;
                            case 6:
                                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT TO SHARE IN INSTAGRAM");
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
        }


    }
}
