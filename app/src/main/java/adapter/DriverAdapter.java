package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.DriverItemHolderBinding;

import myinterface.OnRVItemClickListener;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {
    private OnRVItemClickListener mListener;
    public DriverAdapter(OnRVItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DriverItemHolderBinding binding = DriverItemHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DriverViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class DriverViewHolder extends RecyclerView.ViewHolder {

        private DriverItemHolderBinding mBinding;
        private final OnRVItemClickListener mListener;
        public DriverViewHolder(@NonNull DriverItemHolderBinding binding, OnRVItemClickListener listener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.mListener = listener;
            this.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DriverViewHolder.this.mListener.onRVItemClick(getAdapterPosition());
                }
            });
        }
    }
}
