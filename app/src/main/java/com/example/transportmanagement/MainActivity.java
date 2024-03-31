package com.example.transportmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.example.transportmanagement.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ActivityMainBinding mBinding;
    Animator scale_down_anim1,
            scale_down_anim2,
            scale_down_anim3,
            scale_down_anim4,
            scale_down_anim5,
            scale_down_anim6,
            scale_up_anim1,
            scale_up_anim2,
            scale_up_anim3,
            scale_up_anim4,
            scale_up_anim5,
            scale_up_anim6;
    private void setUpAnimation()
    {
        scale_down_anim1 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_down);
        scale_down_anim2 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_down);
        scale_down_anim3 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_down);
        scale_down_anim4 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_down);
        scale_down_anim5 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_down);
        scale_down_anim6 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_down);
        scale_up_anim1 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_up);
        scale_up_anim2 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_up);
        scale_up_anim3 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_up);
        scale_up_anim4 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_up);
        scale_up_anim5 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_up);
        scale_up_anim6 = AnimatorInflater.loadAnimator(this, R.animator.anim_scale_up);
        scale_down_anim1.setTarget(mBinding.firstCard);
        scale_down_anim2.setTarget(mBinding.secCard);
        scale_down_anim3.setTarget(mBinding.thirdCard);
        scale_down_anim4.setTarget(mBinding.fourthCard);
        scale_down_anim5.setTarget(mBinding.fifthCard);
        scale_down_anim6.setTarget(mBinding.sixthCard);
        scale_up_anim1.setTarget(mBinding.firstCard);
        scale_up_anim2.setTarget(mBinding.secCard);
        scale_up_anim3.setTarget(mBinding.thirdCard);
        scale_up_anim4.setTarget(mBinding.fourthCard);
        scale_up_anim5.setTarget(mBinding.fifthCard);
        scale_up_anim6.setTarget(mBinding.sixthCard);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setUpAnimation();
        mBinding.openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.getRoot().openDrawer(GravityCompat.START);
            }
        });
        mBinding.firstCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VehicleActivity.class);
                startActivity(intent);
            }
        });
        mBinding.firstCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventID = event.getAction();
                if(eventID == MotionEvent.ACTION_DOWN)
                {
                    scale_down_anim1.start();
                    return true;
                }
                else if(eventID == MotionEvent.ACTION_UP)
                {
                    scale_up_anim1.start();
                    v.performClick();
                    return true;
                }
                else if(eventID == MotionEvent.ACTION_CANCEL)
                {
                    scale_up_anim1.start();
                    return true;
                }
                return false;
            }
        });
        mBinding.secCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DriverActivity.class);
                startActivity(intent);
            }
        });
        mBinding.secCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventID = event.getAction();
                if(eventID == MotionEvent.ACTION_DOWN)
                {
                    scale_down_anim2.start();
                    return true;
                }
                else if(eventID == MotionEvent.ACTION_UP)
                {
                    scale_up_anim2.start();
                    v.performClick();
                    return true;
                }
                else if(eventID == MotionEvent.ACTION_CANCEL)
                {
                    scale_up_anim2.start();
                    return true;
                }
                return false;
            }
        });
        mBinding.thirdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RouteActivity.class);
                startActivity(intent);
            }
        });
        mBinding.thirdCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventID = event.getAction();
                if(eventID == MotionEvent.ACTION_DOWN)
                {
                    scale_down_anim3.start();
                    return true;
                }
                else if(eventID == MotionEvent.ACTION_UP)
                {
                    scale_up_anim3.start();
                    v.performClick();
                    return true;
                }
                else if(eventID == MotionEvent.ACTION_CANCEL)
                {
                    scale_up_anim3.start();
                    return true;
                }
                return false;
            }
        });
        mBinding.fourthCard.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   int eventID = event.getAction();
                   if(eventID == MotionEvent.ACTION_DOWN)
                   {
                       scale_down_anim4.start();
                       return true;
                   }
                   else if(eventID == MotionEvent.ACTION_UP)
                   {
                       scale_up_anim4.start();
                       v.performClick();
                       return true;
                   }
                   else if(eventID == MotionEvent.ACTION_CANCEL)
                   {
                       scale_up_anim4.start();
                       return true;
                   }
                   return false;
               }
           });
        mBinding.fifthCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventID = event.getAction();
                if(eventID == MotionEvent.ACTION_DOWN)
                {
                    scale_down_anim5.start();
                    return true;
                }
                else if(eventID == MotionEvent.ACTION_UP)
                {
                    scale_up_anim5.start();
                    v.performClick();
                    return true;
                }
                else if(eventID == MotionEvent.ACTION_CANCEL)
                {
                    scale_up_anim5.start();
                    return true;
                }
                return false;
            }
        });
        mBinding.sixthCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventID = event.getAction();
                if(eventID == MotionEvent.ACTION_DOWN)
                {
                    scale_down_anim6.start();
                    return true;
                }
                else if(eventID == MotionEvent.ACTION_CANCEL)
                {
                    scale_up_anim6.start();
                    return true;
                }
                else if(eventID == MotionEvent.ACTION_UP)
                {
                    scale_up_anim6.start();
                    v.performClick();
                    return true;
                }

                return false;
            }
        });

    }
}