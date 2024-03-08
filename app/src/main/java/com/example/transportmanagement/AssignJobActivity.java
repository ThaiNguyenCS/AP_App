package com.example.transportmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.transportmanagement.databinding.ActivityAssignJobBinding;

public class AssignJobActivity extends AppCompatActivity {

    ActivityAssignJobBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAssignJobBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("hi", 10);
                setResult(Activity.RESULT_OK, result);
                AssignJobActivity.this.finish();
            }
        });
    }
}