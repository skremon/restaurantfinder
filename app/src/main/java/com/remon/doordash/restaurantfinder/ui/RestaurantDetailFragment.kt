package com.remon.doordash.restaurantfinder.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.remon.doordash.restaurantfinder.databinding.RestaurantDetailFragmentBinding
import com.remon.doordash.restaurantfinder.viewmodel.RestaurantViewModel

class RestaurantDetailFragment : Fragment() {

    companion object {
        const val EXTRA_RESTAURANT_ID = "restaurant_id"
        private val TAG: String? = this::class.simpleName
    }

    private var _viewBinding: RestaurantDetailFragmentBinding? = null
    private val viewBinding
        get() = _viewBinding!!

    lateinit var viewModel: RestaurantViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _viewBinding = RestaurantDetailFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.restaurantId.let {
            viewBinding.progressIndicator.visibility = View.VISIBLE
            viewModel.fetchDetails()
        }

        viewModel.restaurantDetail.observe(viewLifecycleOwner, Observer {
            it?.let { restaurant ->
                viewBinding.apply {
                    progressIndicator.visibility = View.GONE
                    averageRating.text = restaurant.averageRating.toString()
                    description.text = restaurant.description
                    status.text = restaurant.status
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.e(TAG, "Error: $it")
                viewBinding.progressIndicator.visibility = View.GONE
            }
        })
    }
}