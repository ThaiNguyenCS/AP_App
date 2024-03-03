package com.example.transportmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.transportmanagement.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import myfragment.DriverFragment;
import myfragment.RouteFragment;
import myfragment.VehicleFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.driver_menuitem)
                {
                    switchToFragment(new DriverFragment());
                    return true;
                }
                else if(item.getItemId() == R.id.route_menuitem)
                {
                    switchToFragment(new RouteFragment());
                    return true;
                }
                else
                {
                    switchToFragment(new VehicleFragment());
                    return true;
                }
            }
        });
    }

    private void switchToFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(mBinding.fragmentContainer.getId(), fragment, null)
                .commit();
    }
}