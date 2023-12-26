package com.wtg.waytogo.utils

import androidx.recyclerview.widget.DiffUtil
import com.wtg.waytogo.data.response.PlaceItem

class DiffUtilCallback : DiffUtil.ItemCallback<PlaceItem>() {
    override fun areItemsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PlaceItem,
        newItem: PlaceItem
    ): Boolean {
        return oldItem == newItem
    }

}