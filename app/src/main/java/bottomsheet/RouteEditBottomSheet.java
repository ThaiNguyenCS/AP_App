package bottomsheet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.transportmanagement.AddRouteActivity;
import com.example.transportmanagement.databinding.BottomSheetRouteEditBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import data.Route;
import myinterface.OnSendRouteDataToActivity;
import viewmodel.RouteDetailViewModel;

public class RouteEditBottomSheet extends BottomSheetDialogFragment {
    RouteDetailViewModel mViewModel;
    BottomSheetRouteEditBinding mBinding;
    OnSendRouteDataToActivity mListener;
    Calendar c = Calendar.getInstance();
    private Calendar arrivingDate = Calendar.getInstance();
    private Calendar departureDate = Calendar.getInstance();
    final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    public RouteEditBottomSheet(OnSendRouteDataToActivity mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(RouteDetailViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = BottomSheetRouteEditBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.getRouteLiveData().observe(requireActivity(), new Observer<Route>() {
            @Override
            public void onChanged(Route route) {
                presentInitialRouteData(route);
            }
        });
        mBinding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Discard all changes?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RouteEditBottomSheet.this.dismiss();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        mBinding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departure = mBinding.departure.getText().toString().trim();
                if(departure.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String destination = mBinding.destination.getText().toString().trim();
                if(destination.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String schedule = mBinding.schedule.getText().toString();
                if(schedule.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String estimate = mBinding.estimate.getText().toString();
                if(estimate.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String cost = mBinding.cost.getText().toString().trim();
                if(cost.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                String revenue = mBinding.revenue.getText().toString().trim();
                if(revenue.isEmpty())
                {
                    Toast.makeText(requireActivity(), "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                mListener.sendRouteDataToActivity(departure,
                        destination,
                        new Timestamp(departureDate.getTime()),
                        new Timestamp(arrivingDate.getTime()),
                        Double.parseDouble(cost),
                        Double.parseDouble(revenue));
                RouteEditBottomSheet.this.dismiss();
            }
        });
        mBinding.schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int initYear = c.get(Calendar.YEAR);
                int initMonth = c.get(Calendar.MONTH);
                int initDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    }
                }, initYear, initMonth, initDay);
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                departureDate.set(Calendar.MINUTE, minute);
                                departureDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                Date date = departureDate.getTime();
                                mBinding.schedule.setText(dateFormat.format(date));
                            }
                        }, 12, 0, true);
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        departureDate.set(Calendar.DAY_OF_MONTH, datePickerDialog.getDatePicker().getDayOfMonth());
                        departureDate.set(Calendar.MONTH, datePickerDialog.getDatePicker().getMonth());
                        departureDate.set(Calendar.YEAR, datePickerDialog.getDatePicker().getYear());
                        timePickerDialog.show();
                    }
                });
                datePickerDialog.show();
            }
        });
        mBinding.estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int initYear = c.get(Calendar.YEAR);
                int initMonth = c.get(Calendar.MONTH);
                int initDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    }
                }, initYear, initMonth, initDay);
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                arrivingDate.set(Calendar.MINUTE, minute);
                                arrivingDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                Date date = arrivingDate.getTime();
                                mBinding.estimate.setText(dateFormat.format(date));
                            }
                        }, 12, 0, true);
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrivingDate.set(Calendar.DAY_OF_MONTH, datePickerDialog.getDatePicker().getDayOfMonth());
                        arrivingDate.set(Calendar.MONTH, datePickerDialog.getDatePicker().getMonth());
                        arrivingDate.set(Calendar.YEAR, datePickerDialog.getDatePicker().getYear());
                        timePickerDialog.show();
                    }
                });
                datePickerDialog.show();
            }
        });
    }

    private void presentInitialRouteData(Route route)
    {
        mBinding.departure.setText(route.getDeparture());
        mBinding.destination.setText(route.getDestination());
        mBinding.cost.setText(String.valueOf(route.getCost()));
        Date departDate = route.getScheDepartureDate().toDate();
        Date arriveDate = route.getScheArrivingDate().toDate();
        departureDate.setTime(departDate);
        arrivingDate.setTime(arriveDate);
        mBinding.schedule.setText(dateFormat.format(departDate));
        mBinding.estimate.setText(dateFormat.format(arriveDate));
        mBinding.revenue.setText(String.valueOf(route.getRevenue()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }
}
