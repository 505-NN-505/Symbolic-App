package org.compilation.terror;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nex3z.fingerpaintview.FingerPaintView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EnglishAlphabetLearnActivity extends AppCompatActivity {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private EnglishAlphabetClassifier mClassifier;
    private EnglishAlphabetSmallClassifier mSmallClassifier;
    FingerPaintView mFpvPaint;

    int test;
    MediaPlayer mp;

    ImageView img;
    AnimatedVectorDrawable d;
    int time = 4000;
    ImageView dottedImage;

    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_alphabet_learn);
        getSupportActionBar().hide();


        mFpvPaint = findViewById(R.id.fpv_paint);

        Intent i = getIntent();
        test = i.getIntExtra("alphabet", 0);

        dottedImage = findViewById(R.id.dottedImage);
        setDottedAlphabet();


        img = findViewById(R.id.photos);

        setAnimation();

        img.setImageDrawable(d);
        d.start();

        int SPLASH_TIME_OUT = time;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                img.setVisibility(View.GONE);

            }
        }, SPLASH_TIME_OUT);



        setMpToTest();

        mp.start();

        init();




        Bitmap captured_image;

        ImageView btn_speaker = findViewById(R.id.speaker);
        btn_speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                setMpToTest();

                mp.start();

                img.setVisibility(View.VISIBLE);
                setAnimation();

                img.setImageDrawable(d);
                d.start();

                int SPLASH_TIME_OUT = time;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        img.setVisibility(View.GONE);

                    }
                }, SPLASH_TIME_OUT);
            }
        });


        Button btn_submit = findViewById(R.id.buttonSubmit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();

                if(test <26) {
                    mp.stop();
                    Bitmap image = mFpvPaint.exportToBitmap(
                            28, 28);


                    EnglishAlphabetResult result = mClassifier.classify(image);

                    String ans;

                    if(test == result.getNumber()){
                        Intent i = new Intent(EnglishAlphabetLearnActivity.this,SuccessPopUpActivity.class);
                        startActivity(i);

                        saveData();
                    }
                    else {
                        Intent i = new Intent(EnglishAlphabetLearnActivity.this,FailurePopUpActivity.class);
                        startActivity(i);
                    }
                }
                else {
                    Bitmap image = mFpvPaint.exportToBitmap(
                            40, 40);


                    EnglishAlphabetResult result = mSmallClassifier.classify(image);


                    if(test-26 == result.getNumber()){
                        Intent i = new Intent(EnglishAlphabetLearnActivity.this,SuccessPopUpActivity.class);
                        startActivity(i);

                        saveData();
                    }
                    else {
                        Intent i = new Intent(EnglishAlphabetLearnActivity.this,FailurePopUpActivity.class);
                        startActivity(i);
                    }
                }

            }
        });

        Button btn_clear = findViewById(R.id.buttonClear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearClick();

            }
        });

        Button btn_try_another = findViewById(R.id.buttonTryAnother);
        btn_try_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                Intent i = new Intent(EnglishAlphabetLearnActivity.this, EnglishAlphabetExampleActivity.class);
                i.putExtra("alphabet", test);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        mp.stop();
        super.onBackPressed();
    }


    void soundPlay() {
        setMpToTest();
        mp.start();
    }

    void onClearClick() {
        mFpvPaint.clear();
    }

    private void init() {

        try {
            mClassifier = new EnglishAlphabetClassifier(this);
        } catch (IOException e) {

        }

        try {
            mSmallClassifier = new EnglishAlphabetSmallClassifier(this);
        } catch (IOException e) {

        }

    }

    void setMpToTest() {
        if(test == 0)
            mp = MediaPlayer.create(this, R.raw.eat0);
        else if(test == 1)
            mp = MediaPlayer.create(this, R.raw.eat1);
        else if(test == 2)
            mp = MediaPlayer.create(this, R.raw.eat2);
        else if(test == 3)
            mp = MediaPlayer.create(this, R.raw.eat3);
        else if(test == 4)
            mp = MediaPlayer.create(this, R.raw.eat4);
        else if(test == 5)
            mp = MediaPlayer.create(this, R.raw.eat5);
        else if(test == 6)
            mp = MediaPlayer.create(this, R.raw.eat6);
        else if(test == 7)
            mp = MediaPlayer.create(this, R.raw.eat7);
        else if(test == 8)
            mp = MediaPlayer.create(this, R.raw.eat8);
        else if(test == 9)
            mp = MediaPlayer.create(this, R.raw.eat9);
        else if(test == 10)
            mp = MediaPlayer.create(this, R.raw.eat10);
        else if(test == 11)
            mp = MediaPlayer.create(this, R.raw.eat11);
        else if(test == 12)
            mp = MediaPlayer.create(this, R.raw.eat12);
        else if(test == 13)
            mp = MediaPlayer.create(this, R.raw.eat13);
        else if(test == 14)
            mp = MediaPlayer.create(this, R.raw.eat14);
        else if(test == 15)
            mp = MediaPlayer.create(this, R.raw.eat15);
        else if(test == 16)
            mp = MediaPlayer.create(this, R.raw.eat16);
        else if(test == 17)
            mp = MediaPlayer.create(this, R.raw.eat17);
        else if(test == 18)
            mp = MediaPlayer.create(this, R.raw.eat18);
        else if(test == 19)
            mp = MediaPlayer.create(this, R.raw.eat19);
        else if(test == 20)
            mp = MediaPlayer.create(this, R.raw.eat20);
        else if(test == 21)
            mp = MediaPlayer.create(this, R.raw.eat21);
        else if(test == 22)
            mp = MediaPlayer.create(this, R.raw.eat22);
        else if(test == 23)
            mp = MediaPlayer.create(this, R.raw.eat23);
        else if(test == 24)
            mp = MediaPlayer.create(this, R.raw.eat24);
        else if(test == 25)
            mp = MediaPlayer.create(this, R.raw.eat25);
        else if(test == 26)
            mp = MediaPlayer.create(this, R.raw.eat26);
        else if(test == 27)
            mp = MediaPlayer.create(this, R.raw.eat27);
        else if(test == 28)
            mp = MediaPlayer.create(this, R.raw.eat28);
        else if(test == 29)
            mp = MediaPlayer.create(this, R.raw.eat29);
        else if(test == 30)
            mp = MediaPlayer.create(this, R.raw.eat30);
        else if(test == 31)
            mp = MediaPlayer.create(this, R.raw.eat31);
        else if(test == 32)
            mp = MediaPlayer.create(this, R.raw.eat32);
        else if(test == 33)
            mp = MediaPlayer.create(this, R.raw.eat33);
        else if(test == 34)
            mp = MediaPlayer.create(this, R.raw.eat34);
        else if(test == 35)
            mp = MediaPlayer.create(this, R.raw.eat35);
        else if(test == 36)
            mp = MediaPlayer.create(this, R.raw.eat36);
        else if(test == 37)
            mp = MediaPlayer.create(this, R.raw.eat37);
        else if(test == 38)
            mp = MediaPlayer.create(this, R.raw.eat38);
        else if(test == 39)
            mp = MediaPlayer.create(this, R.raw.eat39);
        else if(test == 40)
            mp = MediaPlayer.create(this, R.raw.eat40);
        else if(test == 41)
            mp = MediaPlayer.create(this, R.raw.eat41);
        else if(test == 42)
            mp = MediaPlayer.create(this, R.raw.eat42);
        else if(test == 43)
            mp = MediaPlayer.create(this, R.raw.eat43);
        else if(test == 44)
            mp = MediaPlayer.create(this, R.raw.eat44);
        else if(test == 45)
            mp = MediaPlayer.create(this, R.raw.eat45);
        else if(test == 46)
            mp = MediaPlayer.create(this, R.raw.eat46);
        else if(test == 47)
            mp = MediaPlayer.create(this, R.raw.eat47);
        else if(test == 48)
            mp = MediaPlayer.create(this, R.raw.eat48);
        else if(test == 49)
            mp = MediaPlayer.create(this, R.raw.eat49);
        else if(test == 50)
            mp = MediaPlayer.create(this, R.raw.eat50);
        else if(test == 51)
            mp = MediaPlayer.create(this, R.raw.eat51);

    }


    void setAnimation() {

        if(test == 0)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea0);
        else if(test == 1)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea1);
        else if(test == 2)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea2);
        else if(test == 3)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea3);
        else if(test == 4)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea4);
        else if(test == 5)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea5);
        else if(test == 6)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea6);
        else if(test == 7)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea7);
        else if(test == 8)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea8);
        else if(test == 9)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea9);
        else if(test == 10)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea10);
        else if(test == 11)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea11);
        else if(test == 12)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea12);
        else if(test == 13)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea13);
        else if(test == 14)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea14);
        else if(test == 15)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea15);
        else if(test == 16)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea16);
        else if(test == 17)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea17);
        else if(test == 18)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea18);
        else if(test == 19)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea19);
        else if(test == 20)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea20);
        else if(test == 21)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea21);
        else if(test == 22)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea22);
        else if(test == 23)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea23);
        else if(test == 24)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea24);
        else if(test == 25)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea25);
        else if(test == 26)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea26);
        else if(test == 27)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea27);
        else if(test == 28)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea28);
        else if(test == 29)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea29);
        else if(test == 30)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea30);
        else if(test == 31)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea31);
        else if(test == 32)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea32);
        else if(test == 33)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea33);
        else if(test == 34)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea34);
        else if(test == 35)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea35);
        else if(test == 36)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea36);
        else if(test == 37)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea37);
        else if(test == 38)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea38);
        else if(test == 39)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea39);
        else if(test == 40)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea40);
        else if(test == 41)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea41);
        else if(test == 42)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea42);
        else if(test == 43)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea43);
        else if(test == 44)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea44);
        else if(test == 45)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea45);
        else if(test == 46)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea46);
        else if(test == 47)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea47);
        else if(test == 48)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea48);
        else if(test == 49)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea49);
        else if(test == 50)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea50);
        else if(test == 51)
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.ea51);

    }

    void setDottedAlphabet() {

        if(test == 0)
            dottedImage.setImageResource(R.drawable.eal0);
        else if(test == 1)
            dottedImage.setImageResource(R.drawable.eal1);
        else if(test == 2)
            dottedImage.setImageResource(R.drawable.eal2);
        else if(test == 3)
            dottedImage.setImageResource(R.drawable.eal3);
        else if(test == 4)
            dottedImage.setImageResource(R.drawable.eal4);
        else if(test == 5)
            dottedImage.setImageResource(R.drawable.eal5);
        else if(test == 6)
            dottedImage.setImageResource(R.drawable.eal6);
        else if(test == 7)
            dottedImage.setImageResource(R.drawable.eal7);
        else if(test == 8)
            dottedImage.setImageResource(R.drawable.eal8);
        else if(test == 9)
            dottedImage.setImageResource(R.drawable.eal9);
        else if(test == 10)
            dottedImage.setImageResource(R.drawable.eal10);
        else if(test == 11)
            dottedImage.setImageResource(R.drawable.eal11);
        else if(test == 12)
            dottedImage.setImageResource(R.drawable.eal12);
        else if(test == 13)
            dottedImage.setImageResource(R.drawable.eal13);
        else if(test == 14)
            dottedImage.setImageResource(R.drawable.eal14);
        else if(test == 15)
            dottedImage.setImageResource(R.drawable.eal15);
        else if(test == 16)
            dottedImage.setImageResource(R.drawable.eal16);
        else if(test == 17)
            dottedImage.setImageResource(R.drawable.eal17);
        else if(test == 18)
            dottedImage.setImageResource(R.drawable.eal18);
        else if(test == 19)
            dottedImage.setImageResource(R.drawable.eal19);
        else if(test == 20)
            dottedImage.setImageResource(R.drawable.eal20);
        else if(test == 21)
            dottedImage.setImageResource(R.drawable.eal21);
        else if(test == 22)
            dottedImage.setImageResource(R.drawable.eal22);
        else if(test == 23)
            dottedImage.setImageResource(R.drawable.eal23);
        else if(test == 24)
            dottedImage.setImageResource(R.drawable.eal24);
        else if(test == 25)
            dottedImage.setImageResource(R.drawable.eal25);
        else if(test == 26)
            dottedImage.setImageResource(R.drawable.eal26);
        else if(test == 27)
            dottedImage.setImageResource(R.drawable.eal27);
        else if(test == 28)
            dottedImage.setImageResource(R.drawable.eal28);
        else if(test == 29)
            dottedImage.setImageResource(R.drawable.eal29);
        else if(test == 30)
            dottedImage.setImageResource(R.drawable.eal30);
        else if(test == 31)
            dottedImage.setImageResource(R.drawable.eal31);
        else if(test == 32)
            dottedImage.setImageResource(R.drawable.eal32);
        else if(test == 33)
            dottedImage.setImageResource(R.drawable.eal33);
        else if(test == 34)
            dottedImage.setImageResource(R.drawable.eal34);
        else if(test == 35)
            dottedImage.setImageResource(R.drawable.eal35);
        else if(test == 36)
            dottedImage.setImageResource(R.drawable.eal36);
        else if(test == 37)
            dottedImage.setImageResource(R.drawable.eal37);
        else if(test == 38)
            dottedImage.setImageResource(R.drawable.eal38);
        else if(test == 39)
            dottedImage.setImageResource(R.drawable.eal39);
        else if(test == 40)
            dottedImage.setImageResource(R.drawable.eal40);
        else if(test == 41)
            dottedImage.setImageResource(R.drawable.eal41);
        else if(test == 42)
            dottedImage.setImageResource(R.drawable.eal42);
        else if(test == 43)
            dottedImage.setImageResource(R.drawable.eal43);
        else if(test == 44)
            dottedImage.setImageResource(R.drawable.eal44);
        else if(test == 45)
            dottedImage.setImageResource(R.drawable.eal45);
        else if(test == 46)
            dottedImage.setImageResource(R.drawable.eal46);
        else if(test == 47)
            dottedImage.setImageResource(R.drawable.eal47);
        else if(test == 48)
            dottedImage.setImageResource(R.drawable.eal48);
        else if(test == 49)
            dottedImage.setImageResource(R.drawable.eal49);
        else if(test == 50)
            dottedImage.setImageResource(R.drawable.eal50);
        else if(test == 51)
            dottedImage.setImageResource(R.drawable.eal51);

    }


    static String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


    void uploadImage() {

        Bitmap image = mFpvPaint.exportToBitmap();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] data = bytes.toByteArray();


        String folderName = Integer.toString(test);
        String randomString = getAlphaNumericString(10);

        String path = "images/english/alphabet/"+folderName+"/"+randomString+".jpg";
        StorageReference firememeReference = storage.getReference(path);

        UploadTask uploadTask = firememeReference.putBytes(data);

    }


    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch ((touchEvent.getAction())) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                if(x1 > x2) {
                    if(test<51) {
                        mp.stop();
                        Intent i = new Intent(EnglishAlphabetLearnActivity.this, EnglishAlphabetLearnActivity.class);
                        i.putExtra("alphabet", test + 1);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);
                        finish();
                    }
                    else {

                    }
                } else if(x1 < x2) {
                    if(test>0) {
                        mp.stop();
                        Intent i = new Intent(EnglishAlphabetLearnActivity.this, EnglishAlphabetLearnActivity.class);
                        i.putExtra("alphabet", test - 1);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left,
                                R.anim.slide_out_right);
                        finish();
                    }
                    else {

                    }
                }
                break;
        }
        return false;
    }

    void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(UserInformation.PREFERENCES, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(UserInformation.EA[test], true);

        editor.apply();
    }
}