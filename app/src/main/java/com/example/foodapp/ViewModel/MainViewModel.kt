package com.example.foodapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.Model.CategoryModel
import com.example.foodapp.Model.ItemsModel
import com.example.foodapp.Model.SliderModel
import com.example.foodapp.Repository.MainRepository

class MainViewModel():ViewModel() {
    private  val repository = MainRepository()

    fun loadBanner(): LiveData<MutableList<SliderModel>>{
        return repository.loadBanner()
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>>{
        return repository.loadCategory()
    }

    fun loadBestSeller(): LiveData<MutableList<ItemsModel>>{
        return repository.loadBestSeller()
    }

    fun loadFiltered(id:String): LiveData<MutableList<ItemsModel>>{
        return repository.loadFiltered(id)
    }

}