package com.example.transportmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.transportmanagement.databinding.ActivityRouteDetailBinding;

import java.text.SimpleDateFormat;

import myfragment.DriverActivityDialog;
import data.Route;
import myfragment.DriverFragment;
import viewmodel.RouteDetailViewModel;

public class RouteDetailActivity extends AppCompatActivity {
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
            mBinding.routeEstimatedArrival.setText(dateFormat.format(mRoute.getScheArrivingDate().toDate()));
        }
        else
        {
            Log.e(TAG, "route is null" );
        }
    }
}