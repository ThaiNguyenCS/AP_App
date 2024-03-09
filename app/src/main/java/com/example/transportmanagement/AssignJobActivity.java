package com.example.transportmanagement;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.transportmanagement.databinding.ActivityAssignJobBinding;

import data.Driver;
import kotlin.Result;
import myfragment.DriverFragment;
import myfragment.RouteFragment;
import myfragment.VehicleFragment;
import myinterface.OnRVItemClickListener;
import viewmodel.AssignJobViewModel;

public class AssignJobActivity extends AppCompatActivity implements AssignJobViewModel.UpdateCallback {

    private static final String TAG = "AssignJobActivity";
    ActivityAssignJobBinding mBinding;
    int currentPosition;
    int driverID;
    VehicleFragment mVehicleFragment;
    RouteFragment mRouteFragment;
    private AssignJobViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAssignJobBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        currentPosition = 0;
        mViewModel = new ViewModelProvider(this).get(AssignJobViewModel.class);
        getDriverID();
        mViewModel.setCurrentDriverID(driverID);
        mViewModel.setCallback(this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                 if(currentPosition == 1)
                 {
                     handlePresentView(0);
                 }
                 else
                 {
                     AssignJobActivity.this.finish();
                 }
            }
        });
        handlePresentView(currentPosition);

    }

    private void getDriverID()
    {
        Bundle receiveBundle =  getIntent().getExtras();
        if(receiveBundle != null)
        {
            driverID = receiveBundle.getInt("DRIVERID");
        }
        else {
            Toast.makeText(this, "Can't get driver ID", Toast.LENGTH_SHORT).show();
        }
    }
    private void handlePresentView(int position)
    {
        switch(position)
        {
            case 0:
            {
                mBinding.prevButton.setVisibility(View.GONE);
                mBinding.nextButton.setText("Next");
                currentPosition = 0;
                mBinding.nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handlePresentView(1);
                    }
                });

//                RouteFragment routeFragment = (RouteFragment) getSupportFragmentManager().findFragmentByTag("ROUTE");
                if(mRouteFragment == null)
                {
                    mRouteFragment = new RouteFragment(1);
                    switchFragment(mRouteFragment);
                }
                else
                {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .show(mRouteFragment)
                            .commit();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .hide(mVehicleFragment)
                            .commit();
                }
                break;
            }
            case 1:
            {
                mBinding.prevButton.setVisibility(View.VISIBLE);
                mBinding.nextButton.setText("Done");
                currentPosition = 1;
                mBinding.nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO handle done
                        // modify 3 relevant document (ID) and status
                        mViewModel.updateAssignment();
                    }
                });
                mBinding.prevButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handlePresentView(0);
                    }
                });
                if(mVehicleFragment == null)
                {
                    mVehicleFragment = new VehicleFragment(1);
                    switchFragment(mVehicleFragment);
                }
                else
                {
                    Log.e(TAG, "handlePresentView: here");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .show(mVehicleFragment)
                            .commit();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(mRouteFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                }
                break;
            }
        }
    }
    private void switchFragment(Fragment fragment)
    {
        if(fragment instanceof VehicleFragment)
        {
            currentPosition = 1;
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .add(mBinding.fragmentContainer.getId(), fragment, "VEHICLE")
                    .commit();
        }
        else
        {
            currentPosition = 0;
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(mBinding.fragmentContainer.getId(), fragment, "ROUTE")
                    .commit();
        }

    }

    @Override
    public void updateDone() {
        Log.e(TAG, "updateDone: " + Thread.currentThread());
        Toast.makeText(this, "Update done", Toast.LENGTH_SHORT).show();
        //TODO send signal to DriverDetailActivity
        AssignJobActivity.this.finishAndRemoveTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
    }
}