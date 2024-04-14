package myfragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.transportmanagement.RouteDetailActivity;
import com.example.transportmanagement.databinding.RouteActivityDialogBinding;

import data.Driver;
import data.Vehicle;
import viewmodel.RouteDetailViewModel;

public class RouteActivityDialog extends DialogFragment {
    private static final String TAG = "RouteActivityDialog";
    RouteDetailViewModel mViewModel;
    RouteActivityDialogBinding mBinding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        mBinding = RouteActivityDialogBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(requireActivity()).get(RouteDetailViewModel.class);
        mBinding.dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteActivityDialog.this.dismiss();
            }
        });
    }

    private void setVehicleData(Vehicle vehicle)
    {
        Log.e(TAG, "setVehicleData: " );

        mBinding.vehicleBrand.setText(vehicle.getBrand());
        mBinding.vehicleCap.setText(String.valueOf(vehicle.getMaximumLoad()));
        mBinding.vehicleFuel.setText(vehicle.getTypeOfFuel());
        mBinding.numberOfPlate.setText(vehicle.getNumberOfPlate());
        String size = String.join("x", String.valueOf(vehicle.getLength()),
                                                String.valueOf(vehicle.getWidth()),
                                                String.valueOf(vehicle.getHeight()));
        mBinding.vehicleSize.setText(size);
    }

    private void setDriverData(Driver driver)
    {
        mBinding.driverName.setText(driver.getName());
        mBinding.driverLicense.setText(driver.getLicense());
        mBinding.driverPhone.setText(driver.getPhoneNumber());
        mBinding.driverExperience.setText(String.valueOf(driver.getYearOfExperience()));
        mBinding.driverId.setText(driver.getCitizenID());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = RouteActivityDialogBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteActivityDialog.this.dismiss();
            }
        });
        mViewModel.getVehicleLiveData().observe(requireActivity(), new Observer<Vehicle>() {
            @Override
            public void onChanged(Vehicle vehicle) {
                setVehicleData(vehicle);
            }
        });
        mViewModel.getDriverLiveData().observe(requireActivity(), new Observer<Driver>() {
            @Override
            public void onChanged(Driver driver) {
                setDriverData(driver);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if(window != null)
        {
            Log.e(TAG, "window != null");
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setAttributes(params);
        }
    }


}
