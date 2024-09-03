package com.vaipratech.foodie.view.base

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.vaipratech.foodie.model.FoodItem

private const val TAG = "BasicActivity"

abstract class BasicActivity<VB : ViewBinding> : AppCompatActivity() {
    @Suppress("UNCHECKED_CAST")
    protected val binding: VB get() = _binding as VB
    private var _binding: ViewBinding? = null
    private val _sharedModel by viewModels<BaseViewModel>()
    open val sharedModel get() = _sharedModel
    abstract val bindingInflater: (LayoutInflater) -> VB
    abstract fun setup(savedInstanceState: Bundle?)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        setup(savedInstanceState)
    }

    companion object {
        private var listOfSelectedFoodIte = arrayListOf<FoodItem>()
        private var listOfSelectedFoodItem2 = arrayListOf<FoodItem>()
    }

    fun getListOfItem(): ArrayList<FoodItem> = listOfSelectedFoodIte
    fun getListOfItem2(): ArrayList<FoodItem> = listOfSelectedFoodItem2
    fun clearListOfItem() = listOfSelectedFoodIte.clear()
    fun clearListOfItem2() = listOfSelectedFoodItem2.clear()
    fun removeListOfItem(index: Int): MutableList<FoodItem> {
        listOfSelectedFoodIte.removeAt(index)
        return listOfSelectedFoodIte
    }
    fun removeListOfItem2(index: Int): MutableList<FoodItem> {
        listOfSelectedFoodItem2.removeAt(index)
        return listOfSelectedFoodItem2
    }

    fun setListOfItem(listOfSelectedFoodItem: ArrayList<FoodItem>) {
        listOfSelectedFoodIte = listOfSelectedFoodItem
    }
    fun setListOfItem2(listOfSelectedFoodItem: ArrayList<FoodItem>) {
        listOfSelectedFoodItem2 = listOfSelectedFoodItem
    }
}