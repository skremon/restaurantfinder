package com.remon.doordash.restaurantfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.remon.doordash.restaurantfinder.data.Restaurant
import com.remon.doordash.restaurantfinder.databinding.RestaurantListItemBinding
import com.squareup.picasso.Picasso

class RestaurantListAdapter : RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>() {

    // Ref: https://developer.android.com/reference/androidx/recyclerview/widget/AsyncListDiffer
    private val listDiffer = AsyncListDiffer<Restaurant>(this, object : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem
        }
    })

    class RestaurantViewHolder(private val binding: RestaurantListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant) {
            binding.name.text = restaurant.name
            binding.description.text = restaurant.description
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
        holder.bind(listDiffer.currentList[position])
    }

    fun submitData(listRestaurants: List<Restaurant>) {
        listDiffer.submitList(listRestaurants)
    }
}