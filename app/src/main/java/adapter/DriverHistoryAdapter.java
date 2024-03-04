package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.DriverHistoryHolderBinding;

public class DriverHistoryAdapter extends RecyclerView.Adapter<DriverHistoryAdapter.DriverHistoryViewHolder> {
    @NonNull
    @Override
    public DriverHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DriverHistoryHolderBinding binding = DriverHistoryHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DriverHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverHistoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class DriverHistoryViewHolder extends RecyclerView.ViewHolder
    {
        private DriverHistoryHolderBinding mBinding;
        public DriverHistoryViewHolder(@NonNull DriverHistoryHolderBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }
}
