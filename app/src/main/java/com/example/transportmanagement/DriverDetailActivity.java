package com.example.transportmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.view.View;

import com.example.transportmanagement.databinding.ActivityDriverDetailBinding;

import adapter.DriverHistoryAdapter;

public class DriverDetailActivity extends AppCompatActivity {

    ActivityDriverDetailBinding mBinding;
    Animator downToRightAnimator;
    Animator rightToDownAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDriverDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        downToRightAnimator = AnimatorInflater.loadAnimator(this.getApplicationContext(), R.animator.anim_downtoright);
        rightToDownAnimator = AnimatorInflater.loadAnimator(this.getApplicationContext(), R.animator.anim_righttodown);
        downToRightAnimator.setTarget(mBinding.historyToggle);
        rightToDownAnimator.setTarget(mBinding.historyToggle);
        DriverHistoryAdapter adapter = new DriverHistoryAdapter();

        mBinding.historyRecyclerview.setAdapter(adapter);
        mBinding.historyRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mBinding.historyRecyclerview.setVisibility(View.GONE);
        downToRightAnimator.start();
        mBinding.historyToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mBinding.historyRecyclerview.getVisibility())
                {
                    case View.VISIBLE:
                    {
                        mBinding.historyRecyclerview.setVisibility(View.GONE);
                        downToRightAnimator.start();
                        break;
                    }
                    default:
                    {
                        mBinding.historyRecyclerview.setVisibility(View.VISIBLE);
                        rightToDownAnimator.start();
                        break;
                    }
                }
            }
        });
        mBinding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverDetailActivity.this.finish();
            }
        });
    }
}