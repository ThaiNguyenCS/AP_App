package com.example.transportmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.view.View;

import com.example.transportmanagement.databinding.ActivityDriverDetailBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import adapter.DriverHistoryAdapter;
import bottomsheet.DriverEditBottomSheet;
import myinterface.OnSendDataToActivity;

public class DriverDetailActivity extends AppCompatActivity implements OnSendDataToActivity {

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
        mBinding.historyRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mBinding.historyLayoutClick.setOnClickListener(new View.OnClickListener() {
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
        mBinding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverEditBottomSheet sheetDialogFragment = new DriverEditBottomSheet(DriverDetailActivity.this);
                sheetDialogFragment.show(getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public void onSendData(String name, String id, String license, String phone, String address, String year) {
        mBinding.idTextview.setText(id);
        mBinding.licenseTextview.setText(license);
        mBinding.phoneTextview.setText(phone);
        mBinding.YOETextview.setText(year + " years");
        mBinding.addressTextview.setText(address);
        mBinding.nameTextview.setText(name);
    }
}