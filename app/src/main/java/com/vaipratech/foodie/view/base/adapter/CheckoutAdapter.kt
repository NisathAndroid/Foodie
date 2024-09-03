package com.vaipratech.foodie.view.base.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vaipratech.foodie.R
import com.vaipratech.foodie.databinding.ItemFoods2Binding
import com.vaipratech.foodie.model.FoodItem

private const val TAG = "==>>CheckoutAdapter"
class CheckoutAdapter(
    private var foodList: List<FoodItem>,
    private val itemClickListener: (FoodItem, List<FoodItem>, Int, Int) -> Unit

) : RecyclerView.Adapter<CheckoutAdapter.FoodViewHolder>() {
    private var tempFoodList = foodList

    companion object {
        var selectedPosition = -1
        var count: Int = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
            val binding =
                ItemFoods2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FoodViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        val foodItem = tempFoodList[position]

        holder.bind(foodItem, itemClickListener, tempFoodList, position)
    }

    override fun getItemCount(): Int {
        return tempFoodList.size
    }

    var tempUnSelected = emptyList<FoodItem>()

    inner class FoodViewHolder(private val binding: ItemFoods2Binding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            foodItem: FoodItem,
            clickListener: (
                FoodItem, tempFoodList: List<FoodItem>,
                position: Int,
                selectedPosition: Int
            ) -> Unit,
            tempFoodList: List<FoodItem>,
            position: Int
        ) {
            binding.apply {
                mealsNameTV.text = foodItem.name
                mealsRateTV.text = foodItem.rate.toString()
                var color: Int = 0
                if (position == selectedPosition) {
                    color = ContextCompat.getColor(
                        itemView.context,
                        R.color.md_theme_primaryContainer
                    )
                    addItemBtn.isEnabled = true
                    foodItem.enable = true
                } else {
                    addItemBtn.isEnabled = false
                    foodItem.enable = false
                    if (selectedPosition == -1) {
                        addItemBtn.isEnabled = true
                        foodItem.enable = true
                    }
                    color = ContextCompat.getColor(itemView.context, R.color.white)

                }

                itemCard.setBackgroundColor(color)
                Log.d(
                    TAG,
                    "bind: name=${foodItem.name} ,enable =${foodItem.enable} ,select=${foodItem.select}"
                )
                addItemBtn.setOnClickListener {
                    count++
                    Log.d(
                        TAG,
                        "bind: OnClick = name=${foodItem.name} ,enable =${foodItem.enable} ,select=${foodItem.select}"
                    )
                    if (foodItem.select) {
                        foodItem.select = false
                        itemCard.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.md_theme_primaryContainer
                            )
                        )
                    } else {
                        foodItem.select = true
                        itemCard.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.white
                            )
                        )
                    }

                    if (selectedPosition == position) {
                        selectedPosition = -1 // deselect if already selected
                    } else {
                        selectedPosition = position
                    }

                    tempUnSelected = tempFoodList.filterIndexed { index, fItem ->

                        if (index != position) {
                            fItem.select = false
                            fItem.enable = false
                        } else {
                            fItem.select = true
                            fItem.enable = true

                        }
                        Log.d(TAG, "bind: select =${foodItem.select}")
                        true
                    }
                    clickListener(foodItem, tempUnSelected, position, selectedPosition)
                }
            }
        }
    }

    fun updateFoodList(tempFoodUpdateList: List<FoodItem>) {
        tempFoodList = tempFoodUpdateList
        notifyDataSetChanged()
        if (count % 2 == 0) count = 0
    }

    fun getPrevPos(): Int = selectedPosition
}