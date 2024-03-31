package adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportmanagement.databinding.RouteItemHolderBinding;
import com.example.transportmanagement.databinding.RouteItemHolderForChoosingBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import data.Route;
import data.Vehicle;
import myinterface.OnRVItemClickListener;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {
    private static final String TAG = "RouteAdapter";
    private List<Route> filterRouteList;
    private List<Route> routeList;
    private List<Boolean> checkedList;
    private static List<String> statusListString;
    private int mViewType;
    OnRVItemClickListener mListener2;
    OnRouteDetailClickListener mListener1;
    public RouteAdapter(OnRouteDetailClickListener listener1, int viewType) {
        this.mViewType = viewType;
        mListener1 = listener1;
        statusListString = new ArrayList<>();
        statusListString.add("Not assigned");
        statusListString.add("Taken");
        statusListString.add("Finished");
    }
    public RouteAdapter(OnRVItemClickListener listener2, int viewType) {
        this.mViewType = viewType;
        mListener2 = listener2;
        statusListString = new ArrayList<>();
        statusListString.add("Not assigned");
        statusListString.add("Taken");
        statusListString.add("Finished");
    }
    public void setAdapterData(List<Route> list)
    {
        filterRouteList = list;
        routeList = list;
        notifyDataSetChanged();
    }
    public void setAdapterData(List<Route> list, List<Boolean> list2)
    {
        filterRouteList = list;
        checkedList = list2;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: viewType " + viewType);
        if(viewType == 1)
        {
            RouteItemHolderForChoosingBinding binding = RouteItemHolderForChoosingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new RouteViewHolder(binding, mListener2);
        }
        RouteItemHolderBinding binding = RouteItemHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RouteViewHolder(binding, mListener1);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: " );
        int realPosition = holder.getAdapterPosition();
        Route route = filterRouteList.get(realPosition);
        if(holder.getItemViewType() == 0)
        {
            holder.mBinding1.routeDeparture.setText(route.getDeparture());
            holder.mBinding1.routeCost.setText(route.getCost() + " VND");
            holder.mBinding1.routeRevenue.setText(route.getRevenue() + " VND");
            holder.mBinding1.routeDestination.setText(route.getDestination());
            SimpleDateFormat scheArrivingDate = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            holder.mBinding1.estimateTime.setText(scheArrivingDate.format(route.getScheArrivingDate().toDate()));
            holder.mBinding1.scheTime.setText(scheArrivingDate.format(route.getScheDepartureDate().toDate()));
            holder.mBinding1.routeStatus.setText(statusListString.get(route.getStatus()));

        }
        else
        {
            holder.mBinding2.checkbox.setChecked(checkedList.get(realPosition));
            holder.mBinding2.costTextview.setText(route.getCost() + " VND");
            holder.mBinding2.revenueTextview.setText(route.getRevenue() + " VND");
            holder.mBinding2.departureTextview.setText(route.getDeparture());
            holder.mBinding2.destinationTextview.setText(route.getDestination());
            SimpleDateFormat scheArrivingDate = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            holder.mBinding2.estimateInfo.setText(scheArrivingDate.format(route.getScheArrivingDate().toDate()));
            holder.mBinding2.scheduleInfo.setText(scheArrivingDate.format(route.getScheDepartureDate().toDate()));
        }
    }
//    public Filter getFilter(int type)
//    {
//        if(type == 0)
//        {
//            return new Filter() {
//                @Override
//                protected FilterResults performFiltering(CharSequence constraint) {
//                    String constraintStr = constraint.toString();
//                    if(constraintStr.isEmpty())
//                    {
//                        Log.e(TAG, "performFiltering: EMPTY" );
//                        filterRouteList = routeList;
//                    }
//                    else
//                    {
//                        List<Vehicle> list = new ArrayList<>();
//                        for(Route route : routeList)
//                        {
//                            if(route.getDeparture().contains(constraintStr))
//                            {
//                                list.add(route);
//                            }
//                        }
//                        filterVehicleList = list;
//                    }
//                    FilterResults filterResults = new FilterResults();
//                    filterResults.values = filterVehicleList;
//                    return filterResults;
//                }
//
//                @Override
//                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    notifyDataSetChanged();
//                }
//            };
//        }
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                String constraintStr = constraint.toString();
//                if(constraintStr.isEmpty() || constraintStr.equals("000"))
//                {
//                    Log.e(TAG, "performFiltering: EMPTY" );
//                    filterVehicleList = vehicleList;
//                }
//                else
//                {
//                    List<Vehicle> list = new ArrayList<>();
//                    if(constraintStr.charAt(0) == '1')
//                    {
//                        for(Vehicle vehicle : vehicleList)
//                        {
//                            if(vehicle.getStatus() == 0)
//                            {
//                                list.add(vehicle);
//                            }
//                        }
//                    }
//                    if(constraintStr.charAt(1) == '1')
//                    {
//                        for(Vehicle vehicle : vehicleList)
//                        {
//                            if(vehicle.getStatus() == 1)
//                            {
//                                list.add(vehicle);
//                            }
//                        }
//                    }
//                    if(constraintStr.charAt(2) == '1')
//                    {
//                        for(Vehicle vehicle : vehicleList)
//                        {
//                            if(vehicle.getStatus() == 2)
//                            {
//                                list.add(vehicle);
//                            }
//                        }
//                    }
//                    filterVehicleList = list;
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = filterVehicleList;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                notifyDataSetChanged();
//            }
//        };
//    }
    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }

    @Override
    public int getItemCount() {
        if(filterRouteList != null)
        {
            return filterRouteList.size();
        }
        return 0;
    }
    public interface OnRouteDetailClickListener
    {
        void onRouteDetailOpen(int position);
    }
    public static class RouteViewHolder extends RecyclerView.ViewHolder
    {
        private RouteItemHolderBinding mBinding1;
        private RouteItemHolderForChoosingBinding mBinding2;
        private OnRVItemClickListener mListener2;
        private OnRouteDetailClickListener mListener1;
        public RouteViewHolder(@NonNull RouteItemHolderBinding binding, OnRouteDetailClickListener listener) {
            super(binding.getRoot());
            this.mBinding1 = binding;
            this.mListener1 = listener;
            mBinding1.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener1.onRouteDetailOpen(getAdapterPosition());
                }
            });
        }

        public RouteViewHolder(@NonNull RouteItemHolderForChoosingBinding binding, OnRVItemClickListener listener) {
            super(binding.getRoot());
            this.mBinding2 = binding;
            this.mListener2 = listener;
            this.mBinding2.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener2.onRVItemClick(getAdapterPosition());
                }
            });
        }

    }
}
