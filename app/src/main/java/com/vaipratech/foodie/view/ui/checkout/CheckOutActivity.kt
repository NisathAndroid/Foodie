package com.vaipratech.foodie.view.ui.checkout

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vaipratech.foodie.databinding.ActivityCheckOutBinding
import com.vaipratech.foodie.model.FoodItem
import com.vaipratech.foodie.view.base.BaseViewModel
import com.vaipratech.foodie.view.base.BasicActivity
import com.vaipratech.foodie.view.base.adapter.BeverageAdapter
import com.vaipratech.foodie.view.base.adapter.FoodAdapter
import com.vaipratech.foodie.view.ui.beverages.BeveragesActivity
import com.vaipratech.foodie.view.ui.home.MainActivity

import com.vaipratech.foodie.view.ui.home.MainActivityViewModel

private const val TAG = "==>>CheckOutActivity"

class CheckOutActivity : BasicActivity<ActivityCheckOutBinding>() {

    private var viewBinding: ActivityCheckOutBinding? = null
    val viewModel by viewModels<MainActivityViewModel>()
    private val _sharedModel by viewModels<BaseViewModel>()
    override val sharedModel get() = _sharedModel
    lateinit var beverageAdapter: BeverageAdapter
    var listOfSelectedFoodItem1 = ArrayList<FoodItem>()
    var listOfSelectedFoodItem2 = ArrayList<FoodItem>()
    lateinit var foodAdapter: FoodAdapter
    var listOfSelectedFoodItem = ArrayList<FoodItem>()

    override val bindingInflater: (LayoutInflater) -> ActivityCheckOutBinding
        get() = ActivityCheckOutBinding::inflate

    override fun setup(savedInstanceState: Bundle?) {
        viewBinding = binding
        try {
            listOfSelectedFoodItem1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(
                    "food_item_list_1",
                    ArrayList::class.java
                ) as ArrayList<FoodItem>
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("food_item_list_1") as ArrayList<FoodItem>
                    ?: arrayListOf()
            }
        } catch (e: Exception) {
            listOfSelectedFoodItem1 = arrayListOf()
        }
        try {
            listOfSelectedFoodItem2 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(
                    "food_item_list_2",
                    ArrayList::class.java
                ) as ArrayList<FoodItem>
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("food_item_list_2") as ArrayList<FoodItem>
                    ?: arrayListOf()
            }
        } catch (e: Exception) {
            listOfSelectedFoodItem2 = arrayListOf()
        }
        if (listOfSelectedFoodItem1.isEmpty())
            listOfSelectedFoodItem1 = getListOfItem()
        if (listOfSelectedFoodItem2.isEmpty())
            listOfSelectedFoodItem2 = getListOfItem2()
        listOfSelectedFoodItem.addAll(listOfSelectedFoodItem1)
        listOfSelectedFoodItem.addAll(listOfSelectedFoodItem2)

        viewBinding?.setupRecyclerView()
        viewBinding?.setClickListener()
        liveDataObserver()
        setupBackPressed()
    }

    private fun setupBackPressed() {
        onBackPressedDispatcher.addCallback(this, callback)

    }

    private fun liveDataObserver() {
//        listOfSelectedFoodItem=getListOfItem()
        Log.d(TAG, "liveDataObserver: data ===========>>>>${getListOfItem()}")
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d(TAG, "handleOnBackPressed: ------>>>>")
            isEnabled = true
            sharedModel.addFoodItem(listOfSelectedFoodItem)
            val intent = Intent(this@CheckOutActivity, BeveragesActivity::class.java)
            intent.putExtra("food_item_list_1", listOfSelectedFoodItem1)
            intent.putExtra("food_item_list_2", listOfSelectedFoodItem2)
            startActivity(intent)

        }
    }

    private fun ActivityCheckOutBinding.setupRecyclerView() {
        foodieRV.layoutManager = LinearLayoutManager(this.root.context)
        foodAdapter = FoodAdapter(listOfSelectedFoodItem) { fooItem, listOfFoodItem, pos, prevPos ->
            Log.d(
                TAG,
                "setupRecyclerView: currentPos =$pos ,isEnable=${fooItem.enable} ,List = ${fooItem.name} |${fooItem.select}"
            )
            listOfFoodItem.filter {
                Log.d(
                    TAG,
                    "setupRecyclerView: name:${it.name}|enable:${it.enable}|select:${it.select}"
                )
                true
            }

            listOfSelectedFoodItem.clear()
            listOfSelectedFoodItem.add(fooItem)

            foodAdapter.updateFoodList(listOfFoodItem)
        }
        foodieRV.adapter = foodAdapter
    }

    private fun ActivityCheckOutBinding.setClickListener() {

    }


}