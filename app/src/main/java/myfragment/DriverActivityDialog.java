package myfragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.transportmanagement.databinding.DriverActivityDialogBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

import data.Route;
import data.Vehicle;
import viewmodel.DriverDetailViewModel;

public class DriverActivityDialog extends DialogFragment {
    private static final String TAG = "DriverActivityDialog";
    DriverDetailViewModel mViewModel;
    private Route route;
    private Vehicle vehicle;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(DriverDetailViewModel.class);
        route = mViewModel.getCurrentRoute();
        vehicle = mViewModel.getCurrentVehicle();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DriverActivityDialogBinding binding = DriverActivityDialogBinding.inflate(getLayoutInflater());
        binding.numberOfPlate.setText(vehicle.getNumberOfPlate());
        binding.vehicleType.setText(vehicle.getType());
        binding.distanceIndicator.setMax(100);
        binding.distanceIndicator.setProgress(100-(int)(route.getLeftDistance()/route.getDistance()));
        binding.distanceLeft.setText(String.join(" ",String.valueOf((int)route.getLeftDistance()), "km"));
        binding.distanceTraveled.setText(String.join(" ",String.valueOf((int)(route.getDistance()-route.getLeftDistance())), "km"));
        binding.departureTextview.setText(route.getDeparture());
        binding.destinationTextview.setText(route.getDestination());
        binding.scheDepart.setText(dateFormat.format(route.getScheDepartureDate().toDate()));
        binding.scheArrive.setText(dateFormat.format(route.getScheArrivingDate().toDate()));
        if(route.getActualDepartureDate() != null)
            binding.actualDepart.setText(dateFormat.format(route.getActualDepartureDate().toDate()));
        binding.actualArrive.setText("No data");
        binding.dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverActivityDialog.this.dismiss();
            }
        });
        return builder.setView(binding.getRoot()).create();
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
