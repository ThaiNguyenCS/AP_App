package com.example.transportmanagement;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.transportmanagement.databinding.ActivityVehicleBinding;

import java.util.List;

import adapter.VehicleAdapter;
import data.Vehicle;
import myinterface.OnRVItemClickListener;
import myinterface.RefreshCallback;
import myinterface.ViewBindCallback;
import viewmodel.MainViewModel;

public class VehicleActivity extends AppCompatActivity implements
        ViewBindCallback,
        VehicleAdapter.OnVehicleDetailClickListener,
        RefreshCallback
{
    private static final String TAG = "VehicleActivity";
    ActivityVehicleBinding mBinding;
    MainViewModel mViewModel;
    private VehicleAdapter adapter;
    private List<Vehicle> vehicleList;
    Animator slide_down_animator;
    Animation alpha_anim, slide_up_anim;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityVehicleBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.fetchVehicleData(false);
        mBinding.progressIndicator.setVisibility(View.VISIBLE);
        adapter = new VehicleAdapter(this, 0);
        adapter.setCallBack(this);
        setUpAnimation();
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
        mViewModel.setRefreshCallbackForVehicle(this);
        mBinding.getRoot().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.fetchVehicleData(true);
            }
        });
    }

    private void setUpAnimation()
    {
        alpha_anim = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        slide_up_anim = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim);
        slide_down_animator = ObjectAnimator.ofFloat(mBinding.filterOptionLayout, "translationY", -100, 0);
        slide_down_animator.setDuration(300);
        slide_down_animator.setInterpolator(new AccelerateDecelerateInterpolator());
    }
    @Override
    public void onVehicleDetailOpen(int position) {
        Log.e(TAG, "onVehicleDetailOpen at " + position);
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