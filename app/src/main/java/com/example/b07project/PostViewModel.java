package com.example.b07project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PostViewModel extends ViewModel{
    private MutableLiveData<Boolean> areRequirementsMet = new MutableLiveData<>();

    public PostViewModel() {
        // Initialize with false since the requirements are not met initially
        areRequirementsMet.setValue(false);
    }

    // Method to update the status of requirements being met
    public void setRequirementsMet(boolean met) {
        areRequirementsMet.setValue(met);
    }

    // Getter for the LiveData
    public LiveData<Boolean> getAreRequirementsMet() {
        return areRequirementsMet;
    }
}
