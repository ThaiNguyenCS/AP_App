package bottomsheet;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.transportmanagement.R;
import com.example.transportmanagement.databinding.BottomSheetDriverBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;

import data.Driver;
import myinterface.OnSendDataToActivity;
import viewmodel.DriverDetailViewModel;

public class DriverEditBottomSheet extends BottomSheetDialogFragment {
    BottomSheetDriverBinding mBinding;
    OnSendDataToActivity mListener;
    DriverDetailViewModel mViewModel;
    private Driver currentDriver;
    private static List<String> statusList;

    public DriverEditBottomSheet(OnSendDataToActivity listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(DriverDetailViewModel.class);
        statusList = new ArrayList<>();
        statusList.add("Available");
        statusList.add("Driving");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = BottomSheetDriverBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = BottomSheetDriverBinding.bind(view);
        mViewModel.getDriverLiveData().observe(this, new Observer<Driver>() {
            @Override
            public void onChanged(Driver driver) {
                currentDriver = driver;
                setAvailableInfo();
            }
        });
        mBinding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverEditBottomSheet.this.dismiss();
            }
        });
        mBinding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO fix empty fields
                String name = mBinding.nameEdittext.getText().toString().trim();
                String id = mBinding.idEdittext.getText().toString().trim();
                String license = mBinding.licenseEdittext.getText().toString().trim();
                long YOE = Long.parseLong(mBinding.YOEEdittext.getText().toString());
                String phone = mBinding.phoneEdittext.getText().toString().trim();
                String address = mBinding.addressEdittext.getText().toString().trim();
                mListener.onSendData(name, id, license, phone, address,  YOE);
                DriverEditBottomSheet.this.dismiss();
            }
        });
    }
    private void setAvailableInfo()
    {
        mBinding.idEdittext.setText(currentDriver.getCitizenID());
        mBinding.addressEdittext.setText(currentDriver.getAddress());
        mBinding.licenseEdittext.setText(currentDriver.getLicense());
        mBinding.nameEdittext.setText(currentDriver.getName());
        mBinding.phoneEdittext.setText(currentDriver.getPhoneNumber());
        mBinding.YOEEdittext.setText(Long.toString(currentDriver.getYearOfExperience()));
        switch ((int) currentDriver.getStatus())
        {
            case 0:
            {
                mBinding.statusTextview.setText(statusList.get(0));
                mBinding.statusIndicator.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
                break;
            }
            case 1:
            {
                mBinding.statusTextview.setText(statusList.get(1));
                mBinding.statusIndicator.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
                break;
            }
        }

//        mBinding.statusSpinner.setSelection((int)currentDriver.getStatus());
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListener = null;
    }
}
