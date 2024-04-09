package com.example.transportmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.transportmanagement.databinding.ActivityVehicleDetailBinding;

import data.Vehicle;
import myfragment.DriverActivityDialog;
import myfragment.DriverFragment;
import myfragment.MaintenanceDialog;
import viewmodel.VehicleDetailViewModel;

public class VehicleDetailActivity extends AppCompatActivity {
    ActivityVehicleDetailBinding mBinding;
    private int vehicleId;
    VehicleDetailViewModel mViewModel;
    private Vehicle mVehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityVehicleDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        getClickedVehicleID();
        mViewModel = new ViewModelProvider(this).get(VehicleDetailViewModel.class);
        mViewModel.getVehicleData(vehicleId);
        mViewModel.getVehicleLiveData().observe(this, new Observer<Vehicle>() {
            @Override
            public void onChanged(Vehicle vehicle) {
                mVehicle = vehicle;
                setVehicleInfo();
            }
        });
        setUpGeneralView();


    }
    private void setUpGeneralView()
    {
        mBinding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehicleDetailActivity.this.finish();
            }
        });
        mBinding.activityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VehicleDetailActivity.this, "Activity", Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VehicleDetailActivity.this, "History", Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.maintenanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VehicleDetailActivity.this, "Maintenance", Toast.LENGTH_SHORT).show();
            }
        });
        PopupMenu popupMenu = new PopupMenu(this, mBinding.menuButton, Gravity.BOTTOM, 0, R.style.PopupMenuStyleCustom);
        popupMenu.inflate(R.menu.vehicle_detail_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.maintenance)
                {
                    //TODO do something for maintenance
                    MaintenanceDialog dialog = new MaintenanceDialog();
                    dialog.show(getSupportFragmentManager(), null);
                    return true;
                }
                return false;
            }
        });
        mBinding.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();

            }
        });
    }

    private void getClickedVehicleID()
    {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            vehicleId = bundle.getInt(VehicleActivity.VEHICLE_ID_EXTRA);
        }
        else
        {
            Toast.makeText(this, "Driver info loading error!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setVehicleInfo() {
        if (mVehicle != null)
        {
            mBinding.fuel.setText(mVehicle.getTypeOfFuel());
            mBinding.type.setText(mVehicle.getType());
            mBinding.maxLoad.setText(String.valueOf(mVehicle.getMaximumLoad()));
            String sizeStr = String.join("x", String.valueOf(mVehicle.getLength()), String.valueOf(mVehicle.getWidth()), String.valueOf(mVehicle.getHeight()));
            mBinding.statusDescription.setText(mViewModel.getStatus(mVehicle.getStatus()));
            mBinding.size.setText(sizeStr);
            mBinding.licensePlate.setText(mVehicle.getNumberOfPlate());
            if(mVehicle.getStatus() == 0)
            {
                mBinding.statusIndicator.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
            }
            else if(mVehicle.getStatus() == 1)
            {
                mBinding.statusIndicator.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
                mViewModel.getCurrentRouteAndDriver();
//                mBinding.viewActivityButton.setVisibility(View.VISIBLE);
////                mBinding.viewActivityButton.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        if(mViewModel.getCurrentRoute() == null || mViewModel.getCurrentVehicle() == null)
////                        {
////                            Toast.makeText(DriverDetailActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
////                            return;
////                        }
////                        new DriverActivityDialog().show(getSupportFragmentManager(), null);
////                    }
////                });
            }
            else
            {
                mBinding.statusIndicator.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));
            }
        }

    }


}