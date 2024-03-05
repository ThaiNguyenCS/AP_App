package com.example.transportmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.transportmanagement.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import adapter.ViewPagerAdapter;
import myfragment.DriverFragment;
import myfragment.RouteFragment;
import myfragment.VehicleFragment;
import viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ActivityMainBinding mBinding;
    MainViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mBinding.viewPager.setAdapter(new ViewPagerAdapter(this));
        setBottomNavBehave();
        setPageChangeCallbackForViewpager();
    }
    private void setPageChangeCallbackForViewpager()
    {
        mBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e(TAG, "onPageSelected: " + position);
                switch (position)
                {
                    case 1:
                    {
                        mBinding.bottomNav.setSelectedItemId(R.id.driver_menuitem);
                        break;
                    }
                    case 2:
                    {
                        mBinding.bottomNav.setSelectedItemId(R.id.route_menuitem);
                        break;
                    }
                    default:
                    {
                        mBinding.bottomNav.setSelectedItemId(R.id.vehicle_menuitem);
                        break;
                    }
                }
            }
        });
    }
    private void setBottomNavBehave()
    {
        mBinding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.driver_menuitem)
                {
                    mBinding.viewPager.setCurrentItem(1);
                    return true;
                }
                else if(item.getItemId() == R.id.route_menuitem)
                {
                    mBinding.viewPager.setCurrentItem(2);
                    return true;
                }
                else
                {
                    mBinding.viewPager.setCurrentItem(0);
                    return true;
                }
            }
        });
    }

}