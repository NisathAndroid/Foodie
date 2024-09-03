package com.vaipratech.foodie.model

import java.io.Serializable

data class FoodItem(
    var image: Int,
    var name: String,
    var description: String,
    var rate: Int,
    var select: Boolean,
    var color: Int,
    var enable: Boolean = false
) : Serializable