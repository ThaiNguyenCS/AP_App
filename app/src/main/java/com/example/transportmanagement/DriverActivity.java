package com.example.transportmanagement;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.transportmanagement.databinding.ActivityDriverBinding;

import java.util.List;

import adapter.DriverAdapter;
import data.Driver;
import myinterface.OnRVItemClickListener;
import myinterface.RefreshCallback;
import myinterface.ViewBindCallback;
import viewmodel.MainViewModel;

public class DriverActivity extends AppCompatActivity implements OnRVItemClickListener,
        ViewBindCallback,
        RefreshCallback {
    private static final String TAG = "DriverActivity";
    public static String DRIVER_ID_EXTRA = "DRIVER_ID";
    ActivityDriverBinding mBinding;
    MainViewModel mViewModel;
    private List<Driver> driverList;
    DriverAdapter adapter;
    Animator slide_down_animator;
    Animation alpha_anim, slide_up_anim;
    Handler mHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding =  ActivityDriverBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mHandler = new Handler();
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.fetchDriverData(false);
        mBinding.getRoot().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "onRefresh: ");
                mViewModel.fetchDriverData(true);
            }
        });
        setUpAnimation();
        adapter = new DriverAdapter(this);
        adapter.setViewBindCallback(this);
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mViewModel.setRefreshCallbackForDriver(this);
        mViewModel.getDriverLiveList().observe(this, new Observer<List<Driver>>() {
            @Override
            public void onChanged(List<Driver> drivers) {
                driverList = drivers;
                adapter.setInitialData(driverList);
                mBinding.progressCircular.setVisibility(View.GONE);
            }
        });
        mBinding.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(DriverActivity.this, v, Gravity.BOTTOM, 0, R.style.PopupMenuStyleCustom);
                popupMenu.inflate(R.menu.driver_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int clickedItemID = item.getItemId();
                        if (clickedItemID == R.id.filter)
                        {
                            mBinding.filterOptionLayout.setVisibility(View.VISIBLE);
                            mBinding.filterOptionLayout.startAnimation(alpha_anim);
                            slide_down_animator.start();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
        setUpSearchView();
        mBinding.cancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.filterOptionLayout.startAnimation(slide_up_anim);
                adapter.getFilter(1).filter("");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.filterOptionLayout.setVisibility(View.GONE);
                    }
                }, 300);
            }
        });
        mBinding.drivingFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected())
                {
                    adapter.getFilter(1).filter("");
                    v.setSelected(false);
                }
                else
                {
                    adapter.getFilter(1).filter("Driving");
                    v.setSelected(true);
                    mBinding.availableFilter.setSelected(false);
                }
            }
        });
        mBinding.availableFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected())
                {
                    adapter.getFilter(1).filter("");
                    v.setSelected(false);
                }
                else
                {
                    adapter.getFilter(1).filter("Available");
                    v.setSelected(true);
                    mBinding.drivingFilter.setSelected(false);
                }
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
    private void setUpSearchView()
    {
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter(0).filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // for real-time searching
                adapter.getFilter(0).filter(newText);
                return false;
            }
        });
    }


    @Override
    public void onRVItemClick(int position) {
        Intent intent = new Intent(this, DriverDetailActivity.class);
        intent.putExtra(DRIVER_ID_EXTRA, driverList.get(position).getID()); // send the clicked driver's ID to the activity
        startActivity(intent);
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
