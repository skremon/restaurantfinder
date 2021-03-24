package com.remon.doordash.restaurantfinder.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.remon.doordash.restaurantfinder.data.ApiResult
import com.remon.doordash.restaurantfinder.data.Restaurant
import com.remon.doordash.restaurantfinder.data.StoreFeed
import com.remon.doordash.restaurantfinder.repository.RestaurantsRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FinderViewModelTest {

    // Using this executor rule so observeForever cannot be called from background thread. Tests do run in a background thread.
    // Note: used JvmField for ValidationError: The @Rule 'rule' must be public.
    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val repository = mockk<RestaurantsRepository>()
    private val viewModel = FinderViewModel(repository)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testFetchWithoutObservers() {
        MatcherAssert.assertThat(viewModel.restaurants, CoreMatchers.notNullValue())
        viewModel.fetchRestaurantsAroundLocation(0.0, 0.0)
        verify(exactly = 0) { repository.getRestaurantsAround(0.0, 0.0) }
    }

    @Test
    fun testFetchWithObservers() {
        MatcherAssert.assertThat(viewModel.restaurants, CoreMatchers.notNullValue())
        val mockObserver = mockk<Observer<List<Restaurant>?>>()
        viewModel.restaurants.observeForever(mockObserver)

        val storeFeed = MutableLiveData<ApiResult<StoreFeed>>()
        every { repository.getRestaurantsAround(0.0, 0.0) } returns storeFeed
        viewModel.fetchRestaurantsAroundLocation(0.0, 0.0)
        verify { repository.getRestaurantsAround(0.0, 0.0) }
    }

    @Test
    fun testFetchDataChanged() {
        MatcherAssert.assertThat(viewModel.restaurants, CoreMatchers.notNullValue())
        val mockObserver = mockk<Observer<List<Restaurant>?>>(relaxUnitFun = true)
        viewModel.restaurants.observeForever(mockObserver)

        val storeFeed = MutableLiveData<ApiResult<StoreFeed>>()
        every { repository.getRestaurantsAround(0.0, 0.0) } returns storeFeed

        viewModel.fetchRestaurantsAroundLocation(0.0, 0.0)
        verify { repository.getRestaurantsAround(0.0, 0.0) }

        val restaurantList = arrayListOf(Restaurant(1, "Bongos", "Tropical foods and fruits", ""),
            Restaurant(1, "Cocos", "Caribbean delights", ""))
        storeFeed.value = ApiResult.SUCCESS(StoreFeed(200, 50, restaurantList))

        verify { mockObserver.onChanged(restaurantList) }
    }
}