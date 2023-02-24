package little.cookie.vkcase1;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.transition.TransitionManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;

import jp.wasabeef.blurry.Blurry;

import static little.cookie.vkcase1.LogFunctions.log;

public class MainActivity extends AppCompatActivity {

    //ImageViews
    ImageView btn_camera ;
    ImageView btn_micro;
    ImageView btn_hioridk;
    ImageView btn_endCall;
    ImageView btn_messages;
    ImageView btn_users;
    ImageView btn_iHaveNoIdea;

    //TextViews
    TextView txt_username;

    //Booleans
    boolean isCameraOn = false;
    boolean isMicroOn = false;
    boolean isChanged = false;

    //Drawables
    Drawable drw_cameraOn;
    Drawable drw_cameraOff;
    Drawable drw_microOn;
    Drawable drw_microOff;

    //ints
    int drw_miniMicroOff_id; //(basically didn't found way to get id of drawable back then)

    //Layouts
    MaterialCardView user1;
    MaterialCardView user2;
    MaterialCardView pfp_user1;
    MaterialCardView pfp_user2;
    ConstraintLayout usersPlace;
    ImageView userBG1;
    ImageView userBG2;


    //Timer
    long lastPress = 0;

    //Sounds
    MediaPlayer snd_hello;
    MediaPlayer snd_mute;
    MediaPlayer snd_unmute;
    MediaPlayer snd_came;
    MediaPlayer snd_leave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        setOnTouch();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //If i start that in OnCreate method - program crashes.
        blurImages();

        threadManager();


        snd_came.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        recorder.stop();
        recorder.release();
        recorder = null;

        snd_came.release();
        snd_leave.release();
        snd_hello.release();
        snd_mute.release();
        snd_unmute.release();
    }
    MediaRecorder recorder;
    private void threadManager(){



        Thread myThread = new Thread(
                () -> {
                    while (true) {
                            if(isMicroOn) {
                                try {
                                    manageSound();
                                }catch(RuntimeException e)
                                {
                                    e.printStackTrace();
                                }
                            }

                    }


                }
        );
        myThread.setDaemon(true);
        myThread.start();



    }

    private void manageSound(){


        // Wait for 1 second to allow the microphone to pick up sound
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// Get the maximum amplitude of the audio signal
        int maxAmplitude = recorder.getMaxAmplitude();
// Check if the maximum amplitude is above a threshold value
        if (maxAmplitude > 1000) {
            this.runOnUiThread(() -> {
                user1.setStrokeWidth(10);
            });

        }else
        {
            this.runOnUiThread(() -> {
                user1.setStrokeWidth(0);
            });
        }



    }


    Bitmap userbg;
    private void blurImages()
    {
        //Gets view's bitmap, blur it, then put it back.
        userbg = ((BitmapDrawable)userBG1.getDrawable()).getBitmap();
        Blurry.with(this).radius(25).sampling(2).from(userbg).into(userBG1);

        Bitmap bitmap = ((BitmapDrawable)userBG2.getDrawable()).getBitmap();
        Blurry.with(this).radius(25).sampling(2).from(bitmap).into(userBG2);
    }


    private void loadData()
    {
        loadChangeable();
        loadDrawables();
        loadSounds();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouch()
    {

        //makes camera change it's icon on tap
        btn_camera.setOnTouchListener((view, motionEvent) -> {


            isCameraOn=!isCameraOn;
            if(isCameraOn) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            11);

                isCameraOn=false;
                }else {
                    pfp_user1.setVisibility(View.INVISIBLE);
                    btn_camera.setImageDrawable(drw_cameraOn);
                    Glide.with(this).asGif().load(R.drawable.video).into(userBG1);
                    snd_unmute.start();

                }


            }
            else {
                pfp_user1.setVisibility(View.VISIBLE);
                btn_camera.setImageDrawable(drw_cameraOff);
                userBG1.setImageDrawable(new BitmapDrawable(getResources(), userbg));
                snd_mute.start();

            }
            return false;
        });

        //makes micro change it's icon and icon of YOU-ser textView on tap



        btn_micro.setOnTouchListener((view, motionEvent) -> {
            try {

                if (!isMicroOn) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                                10);

                        isMicroOn = false;

                    } else {
                        try {
                        btn_micro.setImageDrawable(drw_microOn);
                        txt_username.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                        recorder = new MediaRecorder();
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        recorder.setOutputFile("/dev/null"); // Set the output file to null to discard audio data


                            recorder.prepare();
                            recorder.start();
                            isMicroOn = true;
                            snd_unmute.start();
                        } catch (IOException | RuntimeException e) {
                            e.printStackTrace();
                        }

                    }


                } else {
                    try {
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                        btn_micro.setImageDrawable(drw_microOff);
                        user1.setStrokeWidth(0);
                        txt_username.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, drw_miniMicroOff_id, 0);
                        isMicroOn = false;
                        snd_mute.start();
                    }catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }catch (RuntimeException e)
            {
                new AlertDialog.Builder(this)
                        .setTitle("Как делишки?")
                        .setMessage(e.getMessage())
                        .show();

                e.printStackTrace();
            }
            return false;
        });

        //Makes program shut on tap
        btn_endCall.setOnTouchListener((view, motionEvent) ->
        {
            snd_leave.start();
            try{Thread.sleep(snd_leave.getDuration());} catch (InterruptedException e) {e.printStackTrace();} ;
           System.exit(0);
            return false;
        });

        //Makes alertDialog on tap
        btn_hioridk.setOnTouchListener((view, motionEvent) ->
        {
            snd_hello.start();
            new AlertDialog.Builder(this)
                    .setTitle("Как делишки?")
                    .setMessage("привет")
                    .show();
            return false;
        });

        //Open contacts app on tap
        btn_users.setOnTouchListener((view, motionEvent) ->
        {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("content://contacts/people/"));
            startActivity(intent);

            return false;
        });

        //Open sms activity on tap
        btn_messages.setOnTouchListener((view, motionEvent) ->
        {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:"));
            startActivity(intent);

            return false;
        });


        //Swap users on tap if tap on that button were made later than 1000ms after the last one
        btn_iHaveNoIdea.setOnTouchListener((view, motionEvent) ->
        {
            if(System.currentTimeMillis()-lastPress>=1000) {
                //Update timer
                lastPress = System.currentTimeMillis();

                isChanged = !isChanged;


                if (isChanged) {
                    //Get set of constraint
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(usersPlace);

                    //Clear all needed constraints
                    constraintSet.clear(user1.getId(), ConstraintSet.TOP);
                    constraintSet.clear(user1.getId(), ConstraintSet.BOTTOM);
                    constraintSet.clear(user2.getId(), ConstraintSet.TOP);
                    constraintSet.clear(user2.getId(), ConstraintSet.BOTTOM);

                    //Put constrains in opposite order
                    constraintSet.connect(user1.getId(), ConstraintSet.BOTTOM, usersPlace.getId(), ConstraintSet.BOTTOM);
                    constraintSet.connect(user2.getId(), ConstraintSet.TOP, usersPlace.getId(), ConstraintSet.TOP);

                    constraintSet.connect(user1.getId(), ConstraintSet.TOP, user2.getId(), ConstraintSet.BOTTOM);
                    constraintSet.connect(user2.getId(), ConstraintSet.BOTTOM, user1.getId(), ConstraintSet.TOP,16);

                    TransitionManager.beginDelayedTransition(usersPlace);
                    constraintSet.applyTo(usersPlace);


                } else {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(usersPlace);

                    constraintSet.clear(user1.getId(), ConstraintSet.TOP);
                    constraintSet.clear(user1.getId(), ConstraintSet.BOTTOM);
                    constraintSet.clear(user2.getId(), ConstraintSet.TOP);
                    constraintSet.clear(user2.getId(), ConstraintSet.BOTTOM);


                    constraintSet.connect(user2.getId(), ConstraintSet.BOTTOM, usersPlace.getId(), ConstraintSet.BOTTOM);
                    constraintSet.connect(user1.getId(), ConstraintSet.TOP, usersPlace.getId(), ConstraintSet.TOP);

                    constraintSet.connect(user2.getId(), ConstraintSet.TOP, user1.getId(), ConstraintSet.BOTTOM);
                    constraintSet.connect(user1.getId(), ConstraintSet.BOTTOM, user2.getId(), ConstraintSet.TOP,16);

                    TransitionManager.beginDelayedTransition(usersPlace);
                    constraintSet.applyTo(usersPlace);


                }
            }

            return false;
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void loadDrawables(){
        drw_cameraOn = getDrawable(R.drawable.button_camera_on);
        drw_cameraOff = getDrawable(R.drawable.button_camera_off);

        drw_microOn = getDrawable(R.drawable.button_micro_on);
        drw_microOff = getDrawable(R.drawable.button_micro_off);

        drw_miniMicroOff_id = R.drawable.micro_incall_off;
    }

    private void loadSounds()
    {
        snd_hello = MediaPlayer.create(this,R.raw.hi);
        snd_mute = MediaPlayer.create(this,R.raw.mute);
        snd_unmute = MediaPlayer.create(this,R.raw.unmute);
        snd_leave = MediaPlayer.create(this,R.raw.leave);
        snd_came = MediaPlayer.create(this,R.raw.came);
    }

    //Loading for every stuff that will be changed later
    private void loadChangeable(){
        btn_camera = findViewById(R.id.button_camera);
        btn_micro = findViewById(R.id.button_micro);
        btn_hioridk = findViewById(R.id.button_hiordk);
        btn_endCall = findViewById(R.id.button_end_call);
        txt_username = findViewById(R.id.user1_name);
        btn_users = findViewById(R.id.users_button);
        btn_messages = findViewById(R.id.message_button);
        btn_iHaveNoIdea = findViewById(R.id.idk_button  );

        user1 = findViewById(R.id.user1_place);
        user2 = findViewById(R.id.user2_place);
        pfp_user1 = findViewById(R.id.pfp1);
        pfp_user2 = findViewById(R.id.pfp2);
        usersPlace = findViewById(R.id.usersplace);
        userBG1 = findViewById(R.id.blurView1);
        userBG2 = findViewById(R.id.blurView2);

    }




}