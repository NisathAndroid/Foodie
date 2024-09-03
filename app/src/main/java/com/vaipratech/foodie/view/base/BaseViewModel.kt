package com.vaipratech.foodie.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vaipratech.foodie.model.FoodItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor():ViewModel() {
    val _addedFoodList = MutableLiveData<MutableList<FoodItem>>()
    val addedFoodList: LiveData<MutableList<FoodItem>> get() = _addedFoodList
    fun addFoodItem(listOfSelectedFoodItem: MutableList<FoodItem>) {
        _addedFoodList.value =listOfSelectedFoodItem
    }
}