package myfragment;

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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.transportmanagement.databinding.VehicleActivityDialogBinding;

import java.text.SimpleDateFormat;

import data.Driver;
import data.Route;
import myinterface.FinishCallback;
import viewmodel.VehicleDetailViewModel;

public class VehicleActivityDialog extends DialogFragment implements FinishCallback {
    private static final String TAG = "VehicleActivityDialog";
    VehicleActivityDialogBinding mBinding;
    VehicleDetailViewModel mViewModel;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(VehicleDetailViewModel.class);
        mViewModel.setCallbackForActivity(this);
        mViewModel.getCurrentRouteAndDriver();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = VehicleActivityDialogBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehicleActivityDialog.this.dismiss();
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

    @Override
    public void finish(boolean isSuccessful) {
        if(isSuccessful)
        {
            Route route = mViewModel.getCurrentRoute();
            Driver driver = mViewModel.getCurrentDriver();
            mBinding.driverId.setText(driver.getCitizenID());
            mBinding.driverName.setText(driver.getName());
            mBinding.driverLicense.setText(driver.getLicense());
            mBinding.driverPhone.setText(driver.getPhoneNumber());
            mBinding.driverExperience.setText(String.valueOf(driver.getYearOfExperience()));
            mBinding.departureTextview.setText(route.getDeparture());
            mBinding.scheArrive.setText(dateFormat.format(route.getScheArrivingDate().toDate()));
            mBinding.scheDepart.setSelected(true);
            mBinding.scheArrive.setSelected(true);
            mBinding.actualDepart.setSelected(true);
            mBinding.scheDepart.setText(dateFormat.format(route.getScheDepartureDate().toDate()));
            if(route.getActualDepartureDate() != null)
                mBinding.actualDepart.setText(dateFormat.format(route.getActualDepartureDate().toDate()));
            mBinding.actualArrive.setText("No data");
        }
        else
        {
            Log.e(TAG, "finish: false");
        }

    }
}
