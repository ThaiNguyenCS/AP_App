package bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.transportmanagement.databinding.BottomSheetDriverBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import myinterface.OnSendDataToActivity;

public class DriverEditBottomSheet extends BottomSheetDialogFragment {
    BottomSheetDriverBinding mBinding;
    OnSendDataToActivity mListener;

    public DriverEditBottomSheet(OnSendDataToActivity listener) {
        this.mListener = listener;
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
//        mBinding = BottomSheetDriverBinding.bind(view);
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
                String YOE = mBinding.YOEEdittext.getText().toString().trim();
                String phone = mBinding.phoneEdittext.getText().toString().trim();
                String address = mBinding.addressEdittext.getText().toString().trim();
                mListener.onSendData(name, id, license, phone, address,  YOE);
                DriverEditBottomSheet.this.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListener = null;
    }
}
