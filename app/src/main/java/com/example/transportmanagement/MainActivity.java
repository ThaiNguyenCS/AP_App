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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import adapter.ViewPagerAdapter;
import data.Driver;
import data.Route;
import data.Vehicle;
import myfragment.DriverFragment;
import myfragment.RouteFragment;
import myfragment.VehicleFragment;
import viewmodel.DriverDetailViewModel;
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
        mBinding.viewPager.setOffscreenPageLimit(1);
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
    private void RESETDATA()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Vehicle")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    QuerySnapshot snapshots = task.getResult();
                    Map<String, Object> map = new HashMap<>();
                    map.put(Vehicle.VEHICLE_STATUS, 0);
                    map.put(Vehicle.VEHICLE_DRIVER_ID, null);
                    map.put(Vehicle.VEHICLE_ROUTE_ID, null);

                    Log.e(TAG, "start update");
                    for(QueryDocumentSnapshot snapshot : snapshots)
                    {
                        snapshot.getReference().update(map);
                    }
                    Log.e(TAG, "end update");
                }
            }
        });
    }

}