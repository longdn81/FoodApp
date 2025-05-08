package com.example.foodapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.Model.SliderModel
import com.example.foodapp.Repository.MainRepository
import com.google.firebase.database.core.view.View

class MainViewModel():ViewModel() {
    private  val repository = MainRepository()

    fun loadBanner(): LiveData<MutableList<SliderModel>>{
        return repository.loadBanner()
    }
}