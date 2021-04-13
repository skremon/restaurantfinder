package com.remon.doordash.restaurantfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.remon.doordash.restaurantfinder.R
import com.remon.doordash.restaurantfinder.data.RestaurantClient
import com.remon.doordash.restaurantfinder.databinding.RestaurantListItemBinding
import com.squareup.picasso.Picasso

class RestaurantListAdapter(val itemClickListener: RestaurantItemListener, val favoriteItemClickListener: FavoriteItemListener)
    : RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>() {

    // Ref: https://developer.android.com/reference/androidx/recyclerview/widget/AsyncListDiffer
    private val listDiffer = AsyncListDiffer<RestaurantClient>(this, object : DiffUtil.ItemCallback<RestaurantClient>() {
        override fun areItemsTheSame(oldItem: RestaurantClient, newItem: RestaurantClient): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RestaurantClient, newItem: RestaurantClient): Boolean {
            return oldItem == newItem
        }
    })

    class RestaurantViewHolder(private val binding: RestaurantListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: RestaurantClient, itemClickListener: RestaurantItemListener, favoriteItemClickListener: FavoriteItemListener) {
            binding.name.text = restaurant.name
            binding.description.text = restaurant.description
            binding.isFavorite.text = if (restaurant.favorite) context.getString(R.string.is_favorite) else itemView.resources.getString(R.string.mark_favorite)
            binding.isFavorite.setOnClickListener {
                favoriteItemClickListener.onClick(restaurant)
            }
            binding.root.setOnClickListener {
                itemClickListener.onClick(restaurant)
            }
            if (restaurant.coverImageUrl.isNotEmpty()) {
                Picasso.get().load(restaurant.coverImageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(binding.logo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(RestaurantListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(listDiffer.currentList[position], itemClickListener, favoriteItemClickListener)
    }

    fun submitData(listRestaurants: List<RestaurantClient>) {
        listDiffer.submitList(listRestaurants)
    }
}

class RestaurantItemListener(val clickListener: (restaurantId: Int) -> Unit) {
    fun onClick(restaurant: RestaurantClient) = clickListener(restaurant.id)
}
class FavoriteItemListener(val clickListener: (restaurantId: Int) -> Unit) {
    fun onClick(restaurant: RestaurantClient) = clickListener(restaurant.id)
}