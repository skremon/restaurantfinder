package com.remon.doordash.restaurantfinder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.remon.doordash.restaurantfinder.R
import com.remon.doordash.restaurantfinder.services.ServiceLocator
import com.remon.doordash.restaurantfinder.viewmodel.FinderViewModel

open class FinderActivity : AppCompatActivity(R.layout.finder_activity) {

    // Ref: https://developer.android.com/guide/fragments/fragmentmanager?hl=en#dependencies
    class FinderFragmentFactory(val finderViewModel: FinderViewModel) : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
            when (loadFragmentClass(classLoader, className)) {
                FinderFragment::class.java -> FinderFragment().apply { viewModel = finderViewModel }
                else -> super.instantiate(classLoader, className)
            }
    }

    private fun createViewModel() = ViewModelProvider(this, ServiceLocator.provideFinderViewModelFactory()).get(FinderViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = FinderFragmentFactory(createViewModel())
        super.onCreate(savedInstanceState)
    }
}
