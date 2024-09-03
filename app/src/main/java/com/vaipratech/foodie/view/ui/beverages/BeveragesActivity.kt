package com.vaipratech.foodie.view.ui.beverages

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.vaipratech.foodie.R
import com.vaipratech.foodie.databinding.ActivityBeveragesBinding
import com.vaipratech.foodie.model.FoodItem
import com.vaipratech.foodie.view.base.BaseViewModel
import com.vaipratech.foodie.view.base.BasicActivity
import com.vaipratech.foodie.view.base.adapter.BeverageAdapter
import com.vaipratech.foodie.view.ui.home.MainActivityViewModel
import com.vaipratech.foodie.view.base.adapter.FoodAdapter
import com.vaipratech.foodie.view.ui.checkout.CheckOutActivity
import com.vaipratech.foodie.view.ui.home.MainActivity

import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "==>>BeveragesActivity"

@AndroidEntryPoint
class BeveragesActivity : BasicActivity<ActivityBeveragesBinding>(), MainActivity.Listener {

    private var viewBinding: ActivityBeveragesBinding? = null
    val viewModel by viewModels<MainActivityViewModel>()
    private val _sharedModel by viewModels<BaseViewModel>()
    override val sharedModel get() = _sharedModel
    lateinit var beverageAdapter: BeverageAdapter
    var listOfSelectedFoodItem = ArrayList<FoodItem>()

    var isRun = false


    override val bindingInflater: (LayoutInflater) -> ActivityBeveragesBinding
        get() = ActivityBeveragesBinding::inflate


    override fun setup(savedInstanceState: Bundle?) {
        viewBinding = binding
        MainActivity.listener = this
        try{
            listOfSelectedFoodItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(
                    "food_item_list_2",
                    ArrayList::class.java
                ) as ArrayList<FoodItem>
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("food_item_list_2") as ArrayList<FoodItem>?: arrayListOf()
            }
        }catch (e:Exception){
            listOfSelectedFoodItem = arrayListOf()
        }

        liveDataObserver()
        window.statusBarColor = ContextCompat.getColor(this, R.color.md_theme_tertiary)

        // Optionally, make the status bar icons dark or light
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            // To make icons dark, remove this flag to make icons light
        }
        viewBinding?.setupRecyclerView()
        viewBinding?.setClickListener()
        setupBackPressed()
    }

    private fun liveDataObserver() {

        Log.d(TAG, "liveDataObserver: data ===========>>>>${getListOfItem()}")
        sharedModel.addedFoodList.observe(this) {
            Log.d(TAG, "liveDataObserver: listOf added food =$it")
        }
        _sharedModel.addedFoodList.observe(this) {
            Log.d(TAG, "_liveDataObserver: listOf added food =$it")
        }
    }

    private fun setupBackPressed() {
        Log.d(TAG, "setupBackPressed: ")
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun ActivityBeveragesBinding.setupRecyclerView() {
        var beveragesList = emptyList<FoodItem>()
        if (listOfSelectedFoodItem.isEmpty()) {
            beveragesList = listOf(
                FoodItem(
                    image = R.drawable.baseline_emoji_food_beverage_24,
                    name = "Tea",
                    description = "A hot beverage made from tea leaves.",
                    rate = 10,
                    select = false,
                    color = R.color.md_theme_primaryContainer
                ),
                FoodItem(
                    image = R.drawable.baseline_emoji_food_beverage_24,
                    name = "Coffee",
                    description = "A strong and aromatic beverage made from coffee beans.",
                    rate = 15,
                    select = false,
                    color = R.color.md_theme_primaryContainer
                ),
                FoodItem(
                    image = R.drawable.baseline_emoji_food_beverage_24,
                    name = "Orange Juice",
                    description = "A refreshing citrus juice, rich in vitamin C.",
                    rate = 20,
                    select = false,
                    color = R.color.md_theme_primaryContainer
                ),
                FoodItem(
                    image = R.drawable.baseline_emoji_food_beverage_24,
                    name = "Milkshake",
                    description = "A creamy and sweet drink made with milk and ice cream.",
                    rate = 30,
                    select = false,
                    color = R.color.md_theme_primaryContainer
                ),
                FoodItem(
                    image = R.drawable.baseline_emoji_food_beverage_24,
                    name = "Smoothie",
                    description = "A thick and healthy beverage made with blended fruits.",
                    rate = 25,
                    select = false,
                    color = R.color.md_theme_primaryContainer
                ),
                FoodItem(
                    image = R.drawable.baseline_emoji_food_beverage_24,
                    name = "Mocktail",
                    description = "A non-alcoholic cocktail, perfect for any occasion.",
                    rate = 40,
                    select = false,
                    color = R.color.md_theme_primaryContainer
                )
            )
        } else {
            beveragesList = listOfSelectedFoodItem
        }

        foodieRV.layoutManager = LinearLayoutManager(this.root.context)
        beverageAdapter = BeverageAdapter(beveragesList) { fooItem, listOfFoodItem, pos, prevPos ->
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

          try {
              if (listOfSelectedFoodItem.contains(fooItem)) {
                  listOfSelectedFoodItem.removeAt(pos)
              } else {
                  listOfSelectedFoodItem.add(fooItem)
              }
          }catch (e:Exception){
              listOfSelectedFoodItem.clear()
          }

            beverageAdapter.updateFoodList(listOfFoodItem)
        }
        foodieRV.adapter = beverageAdapter
    }

    private fun ActivityBeveragesBinding.setClickListener() {

        proceedBtn.setOnClickListener {
            Log.d(TAG, "setClickListener: ")
            if (listOfSelectedFoodItem.isEmpty()) {
                Toast.makeText(this@BeveragesActivity, "item is empty...", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            FoodAdapter.selectedPosition = -1
            setListOfItem2(listOfSelectedFoodItem)
            startActivity(Intent(this@BeveragesActivity, CheckOutActivity::class.java))
        }

        toolbar.toolbarBack.setOnClickListener {
            setupBackPressed()
        }
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d(TAG, "handleOnBackPressed: ------>>>>")
            isEnabled = true
            sharedModel.addFoodItem(listOfSelectedFoodItem)
            startActivity(Intent(this@BeveragesActivity, MainActivity::class.java))

        }
    }

    override fun onDataShare(listOfSelectedItemList: MutableList<FoodItem>) {

    }
}


