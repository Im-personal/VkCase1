package little.cookie.vkcase1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import jp.wasabeef.blurry.Blurry;

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
    CardView user1;
    CardView user2;
    ConstraintLayout usersPlace;
    ImageView userBG1;
    ImageView userBG2;

    //Timer
    long lastPress = 0;


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
    }



    private void blurImages()
    {
        //Gets view's bitmap, blur it, then put it back.
        Bitmap bitmap = ((BitmapDrawable)userBG1.getDrawable()).getBitmap();
        Blurry.with(this).radius(25).sampling(2).from(bitmap).into(userBG1);

        bitmap = ((BitmapDrawable)userBG2.getDrawable()).getBitmap();
        Blurry.with(this).radius(25).sampling(2).from(bitmap).into(userBG2);
    }


    private void loadData()
    {
        loadChangeable();
        loadDrawables();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouch()
    {

        //makes camera change it's icon on tap
        btn_camera.setOnTouchListener((view, motionEvent) -> {


            isCameraOn=!isCameraOn;
            if(isCameraOn) {
                btn_camera.setImageDrawable(drw_cameraOn);
            }
            else {
                btn_camera.setImageDrawable(drw_cameraOff);
            }
            return false;
        });

        //makes micro change it's icon and icon of YOU-ser textView on tap
        btn_micro.setOnTouchListener((view, motionEvent) -> {


            isMicroOn=!isMicroOn;
            if(isMicroOn) {
                btn_micro.setImageDrawable(drw_microOn);
                txt_username.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);

            }
            else {
                btn_micro.setImageDrawable(drw_microOff);
                txt_username.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,drw_miniMicroOff_id,0);
            }

            return false;
        });

        //Makes program shut on tap
        btn_endCall.setOnTouchListener((view, motionEvent) ->
        {
           System.exit(0);
            return false;
        });

        //Makes alertDialog on tap
        btn_hioridk.setOnTouchListener((view, motionEvent) ->
        {
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
        usersPlace = findViewById(R.id.usersplace);
        userBG1 = findViewById(R.id.blurView1);
        userBG2 = findViewById(R.id.blurView2);

    }




}