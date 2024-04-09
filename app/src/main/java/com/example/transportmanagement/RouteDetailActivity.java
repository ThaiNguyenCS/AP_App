package com.example.transportmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.transportmanagement.databinding.ActivityRouteDetailBinding;

import java.text.SimpleDateFormat;

import myfragment.DriverActivityDialog;
import data.Route;
import myfragment.DriverFragment;
import myinterface.FinishCallback;
import viewmodel.RouteDetailViewModel;

public class RouteDetailActivity extends AppCompatActivity implements FinishCallback {
    private static final String TAG = "RouteDetailActivity";
    ActivityRouteDetailBinding mBinding;
    RouteDetailViewModel mViewModel;
    private int routeID;
    private Route mRoute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRouteDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        getClickedRouteID();
        mViewModel = new ViewModelProvider(this).get(RouteDetailViewModel.class);
        mViewModel.setFinishCallback(this);
        observeLiveData();
        setUpGeneralView();
    }
    private void observeLiveData()
    {
        mViewModel.getRouteData(routeID);
        mViewModel.getRouteLiveData().observe(this, new Observer<Route>() {
            @Override
            public void onChanged(Route route) {
                mRoute = route;
                setRouteInfo();
                mBinding.progressIndicator.setVisibility(View.GONE);
            }
        });
    }
    private void setUpGeneralView()
    {
        mBinding.actualTimeLayout.setVisibility(View.GONE);
        mBinding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteDetailActivity.this.finish();
            }
        });
    }

    private void getClickedRouteID()
    {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            routeID = bundle.getInt(RouteActivity.ROUTE_ID_EXTRA);
        }
        else
        {
            Toast.makeText(this, "Route info loading error!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRouteInfo() {
        if (mRoute != null)
        {
            mBinding.routeCost.setText(String.valueOf(mRoute.getCost()));
            mBinding.routeDestination.setText(mRoute.getDestination());
            mBinding.routeDeparture.setText(mRoute.getDeparture());
            mBinding.routeDistance.setText(String.valueOf(mRoute.getDistance()));
            mBinding.routeStatus.setText(mViewModel.getStatus(mRoute.getStatus()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a EEE dd MMM yyyy");
            mBinding.routeDepartureDate.setText(dateFormat.format(mRoute.getScheDepartureDate().toDate()));
            mBinding.routeEstimatedTime.setText(dateFormat.format(mRoute.getScheArrivingDate().toDate()));
            mBinding.doneButton.setVisibility(View.GONE);
            if(mRoute.getStatus() == 1) // ongoing trip
            {
                mViewModel.getCurrentDriverAndVehicle();
                mBinding.doneButton.setVisibility(View.VISIBLE);
                mBinding.doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RouteDetailActivity.this);
                        alertDialog.setMessage("Mark this route as done?");
                        alertDialog.setNegativeButton("Cancel", null);
                        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mViewModel.markRouteAsDone();
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                });
            }
            else if(mRoute.getStatus() == 2)
            {
//                mViewModel.getCurrentDriverAndVehicle();
                mBinding.doneButton.setVisibility(View.GONE);
                mBinding.routeActualArrival.setText(dateFormat.format(mRoute.getActualArrivingDate().toDate()));
                mBinding.actualTimeLayout.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            Log.e(TAG, "route is null" );
        }
    }

    @Override
    public void finish(boolean isSuccessful) {
        if(isSuccessful)
        {
            Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show();
            mBinding.doneButton.setVisibility(View.GONE);
            mBinding.progressIndicator.setVisibility(View.VISIBLE);
            mViewModel.getRouteData(routeID);
        }
        else
        {
            Toast.makeText(this, "Failed! Please try again later", Toast.LENGTH_SHORT).show();
        }
    }
}