package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.MaintenanceHolderBinding;

import java.text.SimpleDateFormat;
import java.util.List;

import data.MaintenanceReport;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.MaintenanceViewHolder> {
    private List<MaintenanceReport> reportList;
    @NonNull
    @Override
    public MaintenanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaintenanceHolderBinding binding = MaintenanceHolderBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new MaintenanceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceViewHolder holder, int position) {
        MaintenanceReport report = reportList.get(holder.getAdapterPosition());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        holder.mBinding.beginDate.setText(dateFormat.format(report.getBeginDate().toDate()));
        if(report.getFinishDate() != null)
            holder.mBinding.finishDate.setText(dateFormat.format(report.getFinishDate().toDate()));
        if(report.getCost() != null)
            holder.mBinding.cost.setText(String.valueOf(report.getCost()));
        holder.mBinding.description.setText(report.getDescription());
    }

    @Override
    public int getItemCount() {
        if(reportList != null)
            return reportList.size();
        return 0;
    }

    public void setData(List<MaintenanceReport> list)
    {
        this.reportList = list;
        notifyDataSetChanged();
    }

    public static class MaintenanceViewHolder extends RecyclerView.ViewHolder
    {
        private MaintenanceHolderBinding mBinding;
        public MaintenanceViewHolder(@NonNull MaintenanceHolderBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
