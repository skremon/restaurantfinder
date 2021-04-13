package com.remon.doordash.restaurantfinder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.remon.doordash.restaurantfinder.databinding.RestaurantDetailActivityBinding
import com.remon.doordash.restaurantfinder.services.ServiceLocator
import com.remon.doordash.restaurantfinder.viewmodel.RestaurantViewModel
import com.squareup.picasso.Picasso

class RestaurantDetailActivity : AppCompatActivity() {

    // Ref: https://developer.android.com/guide/fragments/fragmentmanager?hl=en#dependencies
    class RestaurantDetailFragmentFactory(val restaurantViewModel: RestaurantViewModel) : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
            when (loadFragmentClass(classLoader, className)) {
                RestaurantDetailFragment::class.java -> RestaurantDetailFragment().apply {
                    viewModel = restaurantViewModel
                }
                else -> super.instantiate(classLoader, className)
            }
    }

    lateinit var viewModel: RestaurantViewModel
    private var _viewBinding: RestaurantDetailActivityBinding? = null
    private val viewBinding
        get() = _viewBinding!!

    private fun createViewModel(restaurantId: Int) = ViewModelProvider(this, ServiceLocator.provideRestaurantViewModelFactory(restaurantId, this)).get(
        RestaurantViewModel::class.java).also {
            viewModel = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val restaurantId = intent.getIntExtra(RestaurantDetailFragment.EXTRA_RESTAURANT_ID, 0)
        supportFragmentManager.fragmentFactory = RestaurantDetailFragmentFactory(createViewModel(restaurantId))
        super.onCreate(savedInstanceState)
        _viewBinding = RestaurantDetailActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Note: if restaurantId 0 handle as error

        viewBinding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        viewModel.restaurantDetail.observe(this, Observer {
            it?.let { restaurant ->
                viewBinding.topAppBar.title = restaurant.name
                if (restaurant.coverImageUrl.isNotEmpty()) {
                    Picasso.get().load(restaurant.coverImageUrl)
                            .placeholder(android.R.drawable.ic_menu_gallery)
                            .into(viewBinding.appbarCoverImage)
                }

            }
        })
    }
}