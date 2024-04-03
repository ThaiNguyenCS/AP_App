package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.DriverItemHolderBinding;

import java.util.ArrayList;
import java.util.List;

import myinterface.OnRVItemClickListener;
import data.Driver;
import myinterface.ViewBindCallback;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {
    private static final String TAG = "DriverAdapter";
    private OnRVItemClickListener mListener;
    private List<Driver> driverList;
    private List<Driver> filterDriverList;
    private List<String> statusList;
    private ViewBindCallback mCallback;

    public DriverAdapter(OnRVItemClickListener listener) {
        this.mListener = listener;
        statusList = new ArrayList<>();
        statusList.add("Available");
        statusList.add("Driving");
    }

    public void setViewBindCallback(ViewBindCallback callback)
    {
        mCallback = callback;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DriverItemHolderBinding binding = DriverItemHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DriverViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = filterDriverList.get(position);
        holder.mBinding.driverId.setText(driver.getCitizenID());
        holder.mBinding.driverLicense.setText(driver.getLicense());
        holder.mBinding.driverName.setText(driver.getName());
        holder.mBinding.driverName.setSelected(true);
        holder.mBinding.driverStatus.setText(statusList.get((int)driver.getStatus()));
        holder.mBinding.statusIndicator.setImageDrawable(mCallback.callBackStatusDrawable((int)driver.getStatus()));
    }

    @Override
    public int getItemCount() {
        if(filterDriverList != null)
        {
            return filterDriverList.size();
        }
        return 0;
    }
    public void setInitialData(List<Driver> list)
    {
        this.driverList = list;
        this.filterDriverList = list;
        notifyDataSetChanged();
    }
    public List<Driver> getFilterDriverList()
    {
        return filterDriverList;
    }

    public Filter getFilter(String name) {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    List<Driver> firstList = new ArrayList<>();
                    if(name.isEmpty())
                    {
                        firstList = driverList;
                    }
                    else
                    {
                        for(Driver driver : driverList)
                        {
                            if(driver.getName().toLowerCase().contains(name.toLowerCase()))
                            {
                                firstList.add(driver);
                            }
                        }
                    }
                    List<Driver> secList = new ArrayList<>();
                    String keyword = constraint.toString();
                    if(keyword.equals("00")) {
                        secList = firstList;
                    }
                    else {
                        if(keyword.charAt(0) == '1')
                        {
                            for(Driver driver : firstList) {
                                if(driver.getStatus() == 0) {
                                    secList.add(driver);
                                }
                            }
                        }
                        if(keyword.charAt(1) == '1')
                        {
                            for(Driver driver : firstList) {
                                if(driver.getStatus() == 1) {
                                    secList.add(driver);
                                }
                            }
                        }
                    }
                    filterDriverList = secList;
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterDriverList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
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

