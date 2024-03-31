package com.example.transportmanagement;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
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
import viewmodel.RouteViewModel;

public class RouteActivity extends AppCompatActivity implements
        RouteAdapter.OnRouteDetailClickListener,
        RefreshCallback {
    private static final String TAG = "RouteActivity";
    ActivityRouteBinding mBinding;
    RouteViewModel mViewModel;
    private RouteAdapter adapter;
    private List<Route> routeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRouteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        closeSearchingView();
        mViewModel = new ViewModelProvider(this).get(RouteViewModel.class);
        mViewModel.fetchRouteData(false);
        mViewModel.setRefreshCallbackForRoute(this);
        adapter = new RouteAdapter(this, 0);
        mBinding.filterOptionLayout.setVisibility(View.GONE);
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
        setUpMenu();
        mBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteActivity.this.finish();
            }
        });
        mBinding.cancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.filterOptionLayout.setVisibility(View.GONE);
            }
        });
        mBinding.closeSearching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearchingView();
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
    private void closeSearchingView()
    {
        mBinding.searchView.setVisibility(View.GONE);
        mBinding.searchView2.setVisibility(View.GONE);
        mBinding.closeSearching.setVisibility(View.GONE);
    }
    private void setUpMenu()
    {
        PopupMenu popupMenu = new PopupMenu(RouteActivity.this, mBinding.menuButton, Gravity.BOTTOM, 0, R.style.PopupMenuStyleCustom);
        popupMenu.inflate(R.menu.route_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.search)
                {
                    mBinding.searchView.setVisibility(View.VISIBLE);
                    mBinding.searchView2.setVisibility(View.VISIBLE);
                    mBinding.closeSearching.setVisibility(View.VISIBLE);
                    return true;
                }
                else if(item.getItemId() == R.id.filter)
                {
                    mBinding.filterOptionLayout.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        mBinding.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " );
                popupMenu.show();
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