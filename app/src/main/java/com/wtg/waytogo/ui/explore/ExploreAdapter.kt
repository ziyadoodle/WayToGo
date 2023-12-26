package com.wtg.waytogo.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wtg.waytogo.R
import com.wtg.waytogo.data.response.PlaceItem
import com.wtg.waytogo.databinding.ItemRowBinding
import com.wtg.waytogo.utils.DiffUtilCallback

class ExploreAdapter :
    PagingDataAdapter<PlaceItem, ExploreAdapter.ListViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        val place = getItem(position)
        place?.let { holder.bind(it) }
    }

    class ListViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(place: PlaceItem) {
            Glide.with(itemView)
                .load(place.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(binding.ivItem)
            binding.tvItemTitle.text = place.name
            binding.tvItemAddress.text = place.formattedAddress

//            itemView.setOnClickListener {
//                val detailStoryIntent = Intent(itemView.context, DetailActivity::class.java)
//                detailStoryIntent.putExtra(DetailActivity.EXTRA_STORY_DATA, story.id)
//
//                val optionsCompat: ActivityOptionsCompat =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        itemView.context as Activity,
//                        Pair(binding.imageView, "image"),
//                        Pair(binding.tvUsername, "title"),
//                        Pair(binding.tvDescription, "description")
//                    )
//
//                itemView.context.startActivity(detailStoryIntent, optionsCompat.toBundle())
//            }
        }

    }
}