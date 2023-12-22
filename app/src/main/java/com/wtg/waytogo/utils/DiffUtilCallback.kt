package com.wtg.waytogo.utils

import androidx.recyclerview.widget.DiffUtil
import com.wtg.waytogo.data.response.PlaceResponseItem

class DiffUtilCallback : DiffUtil.ItemCallback<PlaceResponseItem>() {
    override fun areItemsTheSame(oldItem: PlaceResponseItem, newItem: PlaceResponseItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PlaceResponseItem,
        newItem: PlaceResponseItem
    ): Boolean {
        return oldItem == newItem
    }

}