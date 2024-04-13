package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.DriverHistoryHolderBinding;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import data.Route;
import data.Vehicle;

public class DriverHistoryAdapter extends RecyclerView.Adapter<DriverHistoryAdapter.DriverHistoryViewHolder> {
    private List<Route> routeList;
    private Map<Integer, Vehicle> vehicleMap;
    @NonNull
    @Override
    public DriverHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DriverHistoryHolderBinding binding = DriverHistoryHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DriverHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverHistoryViewHolder holder, int position) {
        Route route = routeList.get(holder.getAdapterPosition());
        Vehicle vehicle = vehicleMap.get(route.getCurrentVehicleID());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        if(route.getActualArrivingDate() != null)
            holder.mBinding.arrivalTime.setText(dateFormat.format(route.getActualArrivingDate().toDate()));
        holder.mBinding.departureTime.setText(dateFormat.format(route.getActualDepartureDate().toDate()));
        holder.mBinding.departure.setText(route.getDeparture());
        holder.mBinding.destination.setText(route.getDestination());
        holder.mBinding.vehicleInfo.setText(vehicle.getType() + " " +vehicle.getNumberOfPlate());
    }
    public void setData(List<Route> list1, Map<Integer, Vehicle> map)
    {
        routeList = list1;
        vehicleMap = map;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(routeList != null)
            return routeList.size();
        return 0;
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
