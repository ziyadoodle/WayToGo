package com.wtg.waytogo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wtg.waytogo.R
import com.wtg.waytogo.data.response.PlaceResponseItem
import com.wtg.waytogo.databinding.ItemPopularBinding
import com.wtg.waytogo.utils.DiffUtilCallback

class MainAdapter : PagingDataAdapter<PlaceResponseItem, MainAdapter.ListViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val place = getItem(position)
        place?.let { holder.bind(it) }
    }

    class ListViewHolder(private val binding: ItemPopularBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(place: PlaceResponseItem) {
            Glide.with(itemView)
                .load(place.photoReference)
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(binding.ivPopular)
            binding.tvPopular.text = place.name
            binding.tvRating.text = place.rating.toString()

//            itemView.setOnClickListener {}
        }

    }

}