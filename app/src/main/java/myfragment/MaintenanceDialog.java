package myfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.transportmanagement.databinding.MaintenanceDialogBinding;


public class MaintenanceDialog extends DialogFragment {
    MaintenanceDialogBinding mBinding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mBinding = MaintenanceDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
        dialog.setView(mBinding.getRoot());
        return dialog.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
