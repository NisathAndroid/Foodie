package com.vaipratech.foodie.view.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.vaipratech.foodie.R
import com.vaipratech.foodie.databinding.ActivityMainBinding
import com.vaipratech.foodie.model.FoodItem
import com.vaipratech.foodie.view.base.BaseViewModel
import com.vaipratech.foodie.view.base.BasicActivity
import com.vaipratech.foodie.view.base.adapter.FoodAdapter
import com.vaipratech.foodie.view.ui.beverages.BeveragesActivity
import com.vaipratech.foodie.view.util.TypeOfFood
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

private const val TAG = "==>>MainActivity"

@AndroidEntryPoint
class MainActivity() : BasicActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate
    private var viewBinding: ActivityMainBinding? = null
    val viewModel by viewModels<MainActivityViewModel>()
    private val _sharedModel by viewModels<BaseViewModel>()
    override val sharedModel get() = _sharedModel
    lateinit var foodAdapter: FoodAdapter
    var listOfSelectedFoodItem = ArrayList<FoodItem>()

    companion object {
        var listener: Listener? = null
    }


    interface Listener {
        fun onDataShare(listOfSelectedItemList: MutableList<FoodItem>)
    }

    override fun setup(savedInstanceState: Bundle?) {
        viewBinding = binding
      try {
          listOfSelectedFoodItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
              intent.getSerializableExtra(
                  "food_item_list_1",
                  ArrayList::class.java
              ) as ArrayList<FoodItem>
          } else {
              @Suppress("DEPRECATION")
              intent.getSerializableExtra("food_item_list_1") as? ArrayList<FoodItem> ?: arrayListOf()
          }
      } catch (e:Exception){
          listOfSelectedFoodItem = arrayListOf()
      }
        Log.d(TAG, "setup: listOfSelectedFoodItem =$listOfSelectedFoodItem")
        liveDataObserver()
        setSupportActionBar(viewBinding?.toolbar?.customToolbar)
        viewBinding?.setupRecyclerView()
        viewBinding?.setClickListener()
    }

    private fun liveDataObserver() {
        sharedModel.addedFoodList.observe(this) {
            Log.d(TAG, "liveDataObserver: listOf added food =$it")
        }
        _sharedModel.addedFoodList.observe(this) {
            Log.d(TAG, "_liveDataObserver: listOf added food =$it")
        }
    }

    private fun ActivityMainBinding.setClickListener() {
        proceedBtn.setOnClickListener {
            Log.d(TAG, "setClickListener: ")
            if (listOfSelectedFoodItem.isEmpty()) {
                Toast.makeText(this@MainActivity, "item is empty...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FoodAdapter.selectedPosition = -1
            setListOfItem(listOfSelectedFoodItem)
            lifecycleScope.launch { sharedModel.addFoodItem(listOfSelectedFoodItem) }
            startActivity(Intent(this@MainActivity, BeveragesActivity::class.java))
        }
    }

    private fun ActivityMainBinding.setupRecyclerView() {
        var foodList = emptyList<FoodItem>()
        if (listOfSelectedFoodItem.isEmpty())
            foodList = mutableListOf(
                FoodItem(
                    R.drawable.baseline_food_bank_24,
                    "Pizza",
                    "Delicious cheese pizza with tomato sauce",
                    250,
                    select = false,
                    color = R.color.black
                ),
                FoodItem(
                    R.drawable.baseline_food_bank_24,
                    "Burger",
                    "Juicy beef burger with lettuce and tomato",
                    250,
                    select = false,
                    color = R.color.black
                ),
                FoodItem(
                    R.drawable.baseline_food_bank_24,
                    "Pasta",
                    "Italian pasta with marinara sauce",
                    250,
                    select = false,
                    color = R.color.black
                ),
                FoodItem(
                    R.drawable.baseline_food_bank_24,
                    "Sushi",
                    "Fresh sushi rolls with wasabi and soy sauce",
                    250,
                    select = false,
                    color = R.color.black
                )
            )
        else
            foodList = listOfSelectedFoodItem
        foodieRV.layoutManager = LinearLayoutManager(this.root.context)
        foodAdapter = FoodAdapter(foodList) { fooItem, listOfFoodItem, pos, prevPos ->
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


}


