package little.cookie.vkcase1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity {

    ImageView btn_camera ;
    ImageView btn_micro;
    ImageView btn_hioridk;
    ImageView btn_endCall;
    ImageView btn_messages;
    ImageView btn_users;
    ImageView btn_iHaveNoIdea;

    ImageView pfp_user1;
    ImageView pfp_user2;

    TextView txt_username;

    boolean isCameraOn = false;
    boolean isMicroOn = false;
    boolean isChanged = false;

    Drawable drw_cameraOn;
    Drawable drw_cameraOff;
    Drawable drw_microOn;
    Drawable drw_microOff;

    int drw_miniMicroOff_id;

    CardView user1;
    CardView user2;
    ConstraintLayout usersPlace;

    long lastPress = 0;


    BlurView blurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        setOnTouch();
        blurImages();

    }


    private void blurImages()
    {

        blurView=findViewById(R.id.blurView1);
        blur(20);
        blurView=findViewById(R.id.blurView2);
        blur(20);
    }

    private void blur(float radius){

    }

    private void loadData()
    {
        loadChangeable();
        loadDrawables();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouch()
    {
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

        btn_endCall.setOnTouchListener((view, motionEvent) ->
        {
           System.exit(0);
            return false;
        });

        btn_hioridk.setOnTouchListener((view, motionEvent) ->
        {
            new AlertDialog.Builder(this)
                    .setTitle("Как делишки?")
                    .setMessage("привет")
                    .show();
            return false;
        });

        btn_users.setOnTouchListener((view, motionEvent) ->
        {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("content://contacts/people/"));
            startActivity(intent);

            return false;
        });

        btn_messages.setOnTouchListener((view, motionEvent) ->
        {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:"));
            startActivity(intent);

            return false;
        });

        btn_iHaveNoIdea.setOnTouchListener((view, motionEvent) ->
        {
            if(System.currentTimeMillis()-lastPress>=1000) {

                lastPress = System.currentTimeMillis();


                isChanged = !isChanged;


                if (isChanged) {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(usersPlace);

                    constraintSet.clear(user1.getId(), ConstraintSet.TOP);
                    constraintSet.clear(user1.getId(), ConstraintSet.BOTTOM);
                    constraintSet.clear(user2.getId(), ConstraintSet.TOP);
                    constraintSet.clear(user2.getId(), ConstraintSet.BOTTOM);


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

        pfp_user1 = findViewById(R.id.pfp1);
        pfp_user2 = findViewById(R.id.pfp2);

    }
}