package bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.transportmanagement.VehicleDetailActivity;
import com.example.transportmanagement.databinding.BottomSheetVehicleEditBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import data.Vehicle;
import myinterface.OnSendVehicleDataToActivity;
import viewmodel.VehicleDetailViewModel;

public class VehicleEditBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "VehicleEditBottomSheet";
    BottomSheetVehicleEditBinding mBinding;
    VehicleDetailViewModel mViewModel;
    OnSendVehicleDataToActivity mListener;

    public VehicleEditBottomSheet() {
    }
    public VehicleEditBottomSheet(OnSendVehicleDataToActivity listener)
    {
        this.mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(VehicleDetailViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = BottomSheetVehicleEditBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();
        mViewModel.getVehicleLiveData().observe(requireActivity(), new Observer<Vehicle>() {
            @Override
            public void onChanged(Vehicle vehicle) {
                setInitialVehicleData(vehicle);
            }
        });
    }

    private void setInitialVehicleData(Vehicle vehicle)
    {
        String brand = vehicle.getBrand();
        String type = vehicle.getType();
        SpinnerAdapter brandAdapter = mBinding.brandSpinner.getAdapter();
        for(int i = 0; i < brandAdapter.getCount(); i++)
        {
            String str = (String) brandAdapter.getItem(i);
            Log.e(TAG, str );
            if (str.equals(brand))
            {
                mBinding.brandSpinner.setSelection(i, true);
                break;
            }
        }
        SpinnerAdapter typeAdapter = mBinding.typeSpinner.getAdapter();
        for(int i = 0; i < typeAdapter.getCount(); i++)
        {
            String str = (String) typeAdapter.getItem(i);
            Log.e(TAG, str );
            if (str.equals(type))
            {
                mBinding.typeSpinner.setSelection(i, true);
                break;
            }
        }
        mBinding.fuel.setText(vehicle.getTypeOfFuel());
        mBinding.height.setText(String.valueOf(vehicle.getHeight()));
        mBinding.width.setText(String.valueOf(vehicle.getWidth()));
        mBinding.length.setText(String.valueOf(vehicle.getLength()));
        mBinding.licensePlate.setText(vehicle.getNumberOfPlate());
        mBinding.maxLoad.setText(String.valueOf(vehicle.getMaximumLoad()));
        mBinding.fuel.setText(vehicle.getTypeOfFuel());
    }

    private void setUpView()
    {
        mBinding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Discard all changes?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VehicleEditBottomSheet.this.dismiss();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

        mBinding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String license = mBinding.licensePlate.getText().toString().trim();
                if(license.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String height = mBinding.height.getText().toString().trim();
                if(height.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String width = mBinding.width.getText().toString().trim();
                if(width.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String length = mBinding.length.getText().toString().trim();
                if(length.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String maxLoad = mBinding.maxLoad.getText().toString().trim();
                if(maxLoad.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String fuel = mBinding.fuel.getText().toString().trim();
                if(fuel.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String brand = (String) mBinding.brandSpinner.getSelectedItem();
                String type = (String) mBinding.typeSpinner.getSelectedItem();
                mListener.sendVehicleData(license,
                        Double.parseDouble(height),
                        Double.parseDouble(width),
                        Double.parseDouble(length),
                        Double.parseDouble(maxLoad),
                        fuel,
                        brand,
                        type);
                VehicleEditBottomSheet.this.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if(window != null)
        {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }
}
