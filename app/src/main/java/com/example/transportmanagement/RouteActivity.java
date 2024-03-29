package com.example.transportmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.transportmanagement.databinding.ActivityRouteBinding;
import com.example.transportmanagement.databinding.ActivityVehicleBinding;

import java.util.ArrayList;
import java.util.List;

import adapter.RouteAdapter;
import data.Route;
import myinterface.RefreshCallback;
import viewmodel.MainViewModel;

public class RouteActivity extends AppCompatActivity implements
        RouteAdapter.OnRouteDetailClickListener,
        RefreshCallback {
    private static final String TAG = "RouteActivity";
    ActivityRouteBinding mBinding;
    MainViewModel mViewModel;
    private RouteAdapter adapter;
    private List<Route> routeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRouteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.fetchRouteData(false);
        mViewModel.setRefreshCallbackForRoute(this);
        adapter = new RouteAdapter(this, 0);
        mBinding.progressIndicator.setVisibility(View.VISIBLE);
        mViewModel.getRouteLiveList().observe(this, new Observer<List<Route>>() {
            @Override
            public void onChanged(List<Route> routes) {
                routeList = routes;
                adapter.setAdapterData(routes);
                //TODO this setVisibility is not in the right place yet but acceptable
                mBinding.progressIndicator.setVisibility(View.GONE);
            }
        });

        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL, false));
        mBinding.getRoot().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.fetchRouteData(true);
            }
        });

    }

    @Override
    public void onRouteDetailOpen(int position) {
        Log.e(TAG, "onRouteDetailOpen at " + position);
    }

    @Override
    public void refreshDone() {
        mBinding.getRoot().setRefreshing(false);
    }
}