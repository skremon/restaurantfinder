package com.remon.doordash.restaurantfinder.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.remon.doordash.restaurantfinder.adapters.RestaurantListAdapter
import com.remon.doordash.restaurantfinder.databinding.FragmentFinderBinding
import com.remon.doordash.restaurantfinder.viewmodel.FinderViewModel

class FinderFragment() : Fragment() {

    private var _viewBinding: FragmentFinderBinding? = null
    private val viewBinding
        get() = _viewBinding!!

    private val restaurantsAdapter = RestaurantListAdapter()
    lateinit var viewModel: FinderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentFinderBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.restaurantListView.apply {
            adapter = restaurantsAdapter
        }

        viewBinding.progressIndicator.visibility = View.VISIBLE
        viewModel.fetchRestaurantsAroundLocation(doorDashHQLocation.first, doorDashHQLocation.second)

        viewModel.restaurants.observe(viewLifecycleOwner, Observer { listRestaurants ->
            listRestaurants?.let {
                Log.d(TAG, "Restaurants result = ${it.size}")

                restaurantsAdapter.submitData(listRestaurants)
                viewBinding.progressIndicator.visibility = View.GONE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.e(TAG, "Error: $it")
                viewBinding.progressIndicator.visibility = View.GONE
            }
        })
    }

    companion object {
        private val TAG: String? = this::class.simpleName
        private val doorDashHQLocation = Pair(37.422740, -122.139956)
    }
}
