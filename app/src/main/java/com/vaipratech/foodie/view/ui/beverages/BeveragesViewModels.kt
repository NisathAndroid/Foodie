package com.vaipratech.foodie.view.ui.beverages

import com.vaipratech.foodie.model.FoodItem
import com.vaipratech.foodie.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class BeveragesViewModels @Inject constructor():BaseViewModel() {
    fun addFoodItems(listOfSelectedFoodItem: MutableList<FoodItem>) {
        _addedFoodList.value =listOfSelectedFoodItem
    }
}