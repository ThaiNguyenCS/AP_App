package adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.DriverItemHolderBinding;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {
    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class DriverViewHolder extends RecyclerView.ViewHolder {

        private DriverItemHolderBinding mBinding;
        public DriverViewHolder(@NonNull DriverItemHolderBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }
}
