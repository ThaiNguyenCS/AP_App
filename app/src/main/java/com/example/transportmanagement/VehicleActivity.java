package com.example.transportmanagement;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.transportmanagement.databinding.ActivityVehicleBinding;

import java.util.List;

import adapter.VehicleAdapter;
import data.Vehicle;
import myinterface.RefreshCallback;
import myinterface.ViewBindCallback;
import viewmodel.VehicleViewModel;

public class VehicleActivity extends AppCompatActivity implements
        ViewBindCallback,
        VehicleAdapter.OnVehicleDetailClickListener,
        RefreshCallback
{
    private static final String TAG = "VehicleActivity";
    ActivityVehicleBinding mBinding;
    VehicleViewModel mViewModel;
    private VehicleAdapter adapter;
    private List<Vehicle> vehicleList;
    private List<Boolean> filterList;
    private String searchQuery;
    Animator slide_down_animator;
    Animation alpha_anim, slide_up_anim, scale_down_anim;
    Handler mHandler;
    public static String VEHICLE_ID_EXTRA = "VehicleID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityVehicleBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        searchQuery = "";
        mHandler = new Handler();
        mViewModel = new ViewModelProvider(this).get(VehicleViewModel.class);
        mViewModel.fetchVehicleData(false);
        mBinding.progressIndicator.setVisibility(View.VISIBLE);
        mBinding.filterOptionLayout.setVisibility(View.GONE);
        adapter = new VehicleAdapter(this, 0);
        adapter.setCallBack(this);
        setUpAnimation();
        setUpMenu();

        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mViewModel.getVehicleLiveList().observe(this, new Observer<List<Vehicle>>() {
            @Override
            public void onChanged(List<Vehicle> vehicles) {
                vehicleList = vehicles;
                adapter.setAdapterData(vehicleList);
                mBinding.progressIndicator.setVisibility(View.GONE);
            }
        });
        mViewModel.getFilterLiveList().observe(this, new Observer<List<Boolean>>() {
            @Override
            public void onChanged(List<Boolean> list) {
                filterList = list;
            }
        });
        mViewModel.setRefreshCallbackForVehicle(this);
        mBinding.getRoot().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.fetchVehicleData(true);
                mBinding.inUseFilter.setSelected(false);
                mBinding.maintenanceFilter.setSelected(false);
                mBinding.availableFilter.setSelected(false);
            }
        });
        mBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehicleActivity.this.finish();
            }
        });
        mBinding.cancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.filterOptionLayout.startAnimation(slide_up_anim);
                // clear filters
                adapter.setAdapterData(vehicleList);
                mBinding.inUseFilter.setSelected(false);
                mBinding.maintenanceFilter.setSelected(false);
                mBinding.availableFilter.setSelected(false);
                for(int i = 0; i < 3; i++)
                {
                    filterList.set(i, false);
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.filterOptionLayout.setVisibility(View.GONE);
                    }
                }, 300);
            }
        });
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                adapter.getFilter(mViewModel.getFilterConstraints()).filter(searchQuery);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                adapter.getFilter(mViewModel.getFilterConstraints()).filter(newText);
                return false;
            }
        });

        mBinding.availableFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected())
                {
                    filterList.set(0, false);
                    v.setSelected(false);
                }
                else
                {
                    filterList.set(0, true);
                    v.setSelected(true);
                }
                adapter.getFilter(mViewModel.getFilterConstraints()).filter(searchQuery);


            }
        });
        mBinding.maintenanceFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected())
                {
                    filterList.set(2, false);
                    v.setSelected(false);
                }
                else
                {
                    filterList.set(2, true);
                    v.setSelected(true);
                }
                adapter.getFilter(mViewModel.getFilterConstraints()).filter(searchQuery);
            }
        });
        mBinding.inUseFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected())
                {
                    filterList.set(1, false);
                    v.setSelected(false);
                }
                else
                {
                    filterList.set(1, true);
                    v.setSelected(true);
                }
                adapter.getFilter(mViewModel.getFilterConstraints()).filter(searchQuery);
            }
        });
    }

    private void setUpAnimation()
    {
        scale_down_anim = AnimationUtils.loadAnimation(this, R.anim.scale_down_60);
        alpha_anim = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        slide_up_anim = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim);
        slide_down_animator = ObjectAnimator.ofFloat(mBinding.filterOptionLayout, "translationY", -100, 0);
        slide_down_animator.setDuration(300);
        slide_down_animator.setInterpolator(new AccelerateDecelerateInterpolator());
    }
    private void setUpMenu()
    {
        PopupMenu popupMenu = new PopupMenu(VehicleActivity.this, mBinding.menuButton, Gravity.BOTTOM, 0, R.style.PopupMenuStyleCustom);
        popupMenu.inflate(R.menu.vehicle_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.filter)
                {
                    mBinding.filterOptionLayout.setVisibility(View.VISIBLE);
                    slide_down_animator.start();
                    mBinding.filterOptionLayout.startAnimation(alpha_anim);
                    return true;
                }
                return false;
            }
        });
        mBinding.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e(TAG, "onClick: " );
                v.startAnimation(scale_down_anim);
                popupMenu.show();
            }
        });
    }
    @Override
    public void onVehicleDetailOpen(int position) {
        Log.e(TAG, "onVehicleDetailOpen at " + position);
        Intent intent = new Intent(this, VehicleDetailActivity.class);
        intent.putExtra(VEHICLE_ID_EXTRA, adapter.getFilterVehicleList().get(position).getID());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.fetchVehicleData(true);
        searchQuery = "";
        for(int i = 0; i < 3; i++) {
            filterList.set(i, false);
        }
    }

    @Override
    public void refreshDone() {
        mBinding.getRoot().setRefreshing(false);
    }

    @Override
    public ColorDrawable callBackStatusDrawable(int statusId) {
        switch (statusId) {
            case 0: {
                return new ColorDrawable(getResources().getColor(R.color.green));
            }
            case 1:
            {
                return new ColorDrawable(getResources().getColor(R.color.red));
            }
            default:
            {
                return new ColorDrawable(getResources().getColor(R.color.yellow));
            }
        }
    }
}