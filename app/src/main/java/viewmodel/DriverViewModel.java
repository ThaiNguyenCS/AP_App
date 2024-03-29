package viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import data.Driver;
import myinterface.RefreshCallback;

public class DriverViewModel extends ViewModel {
    private List<Driver> driverList;
    private MutableLiveData<List<Driver>> driverLiveList;
    private RefreshCallback mDriverRefreshCallback;
}
