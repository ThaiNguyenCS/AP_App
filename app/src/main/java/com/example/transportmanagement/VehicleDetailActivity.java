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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import bottomsheet.MaintenanceBottomSheet;
import bottomsheet.VehicleEditBottomSheet;
import bottomsheet.VehicleHistoryBottomSheet;
import data.Vehicle;
import myfragment.DriverActivityDialog;
import myfragment.DriverFragment;
import myfragment.MaintenanceDialog;
import myfragment.VehicleActivityDialog;
import myinterface.FinishCallback;
import myinterface.OnSendVehicleDataToActivity;
import viewmodel.VehicleDetailViewModel;

public class VehicleDetailActivity extends AppCompatActivity implements
    OnSendVehicleDataToActivity, FinishCallback
{
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
        mViewModel.setCallbackForEditVehicle(this);
        mViewModel.getVehicleLiveData().observe(this, new Observer<Vehicle>() {
            @Override
            public void onChanged(Vehicle vehicle) {
                mVehicle = vehicle;
                setVehicleInfo();
                mBinding.progressIndicator.setVisibility(View.GONE);
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
                if (mVehicle.getStatus() == 1)
                {
                    VehicleActivityDialog dialog = new VehicleActivityDialog();
                    dialog.show(getSupportFragmentManager(), null);
                }
                else
                {
                    Toast.makeText(VehicleDetailActivity.this, "There's no activity now", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mBinding.historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehicleHistoryBottomSheet historyBottomSheet = new VehicleHistoryBottomSheet();
                historyBottomSheet.show(getSupportFragmentManager(), null);
            }
        });
        mBinding.maintenanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getVehicleMaintenanceHistory();
                MaintenanceBottomSheet maintenanceBottomSheet = new MaintenanceBottomSheet();
                maintenanceBottomSheet.show(getSupportFragmentManager(), null);

            }
        });
        PopupMenu popupMenu = new PopupMenu(this, mBinding.menuButton, Gravity.BOTTOM, 0, R.style.PopupMenuStyleCustom);
        popupMenu.inflate(R.menu.vehicle_detail_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.maintenance)
                {
                    MaintenanceDialog dialog = new MaintenanceDialog();
                    dialog.show(getSupportFragmentManager(), null);
                    return true;
                }
                return false;
            }
        });
        mBinding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehicleEditBottomSheet bottomSheet = new VehicleEditBottomSheet(VehicleDetailActivity.this);
                bottomSheet.show(getSupportFragmentManager(), null);

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
            mBinding.vehicleBrand.setText(mVehicle.getBrand());
            if(mVehicle.getStatus() == 0)
            {
                mBinding.statusIndicator.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
            }
            else if(mVehicle.getStatus() == 1)
            {
                mBinding.statusIndicator.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
            }
            else
            {
                mBinding.statusIndicator.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));
            }
        }

    }

    @Override
    public void sendVehicleData(String license, double height, double width, double length, double maxLoad, String fuel, String brand, String type) {
        mBinding.progressIndicator.setVisibility(View.VISIBLE);
        mViewModel.updateVehicle(license, height, width, length, maxLoad, fuel, brand, type);
    }

    @Override
    public void finish(boolean isSuccessful) { // finish edit vehicle
        if(isSuccessful)
        {
            Toast.makeText(this, "Update vehicle successfully", Toast.LENGTH_SHORT).show();
            mViewModel.getVehicleData(vehicleId);
        }
        else
        {
            mBinding.progressIndicator.setVisibility(View.GONE);
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}