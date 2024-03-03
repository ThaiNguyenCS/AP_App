package com.example.transportmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.transportmanagement.databinding.ActivityMainBinding;

import myfragment.VehicleFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        ////// TEST ////////
        switchToFragment(new VehicleFragment());
    }

    private void switchToFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(mBinding.fragmentContainer.getId(), fragment, null)
                .commit();
    }
}