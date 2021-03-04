package com.remon.doordash.restaurantfinder.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.remon.doordash.restaurantfinder.R
import com.remon.doordash.restaurantfinder.data.Restaurant
import com.remon.doordash.restaurantfinder.viewmodel.FinderViewModel
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FinderFragmentTest {

    private val mockViewModel = mockk<FinderViewModel>(relaxUnitFun = true)
    private val testListRestaurant = MutableLiveData<List<Restaurant>>()
    private val testError = MutableLiveData<String?>()

    @Before
    fun setup() {
        justRun { mockViewModel.fetchRestaurantsAroundLocation(any(), any()) }
        every { mockViewModel.restaurants } returns testListRestaurant
        every { mockViewModel.error } returns testError

        launchFragmentInContainer {
            FinderFragment().apply {
                viewModel = mockViewModel
            }
        }
    }

    @Test
    fun launchWithProgressShown() {
        onView(withId(R.id.progress_indicator)).check(matches(isDisplayed()))
    }

    @Test
    fun launchWithRestaurantsShown() {
        val restaurantList = arrayListOf(Restaurant(1, "Bongos", "Tropical foods and fruits", ""),
            Restaurant(2, "Cocos", "Caribbean delights", ""))

        testListRestaurant.postValue(restaurantList)
        onView(withId(R.id.progress_indicator)).check(matches(not(isDisplayed())))

        // Ref: https://developer.android.com/training/testing/espresso/lists#recycler-view-list-items
        // to do operations on a RecyclerView like scrollTo, actionOnItem, we need the dependency 'espresso-contrib'
        // But since we are adding only a few items we don't need them yet
        onView(withId(R.id.restaurant_list_view)).check(matches(isDisplayed())).check(
            matches(hasDescendant(withText(restaurantList[0].name)))
        ).check(
            matches(hasDescendant(withText(restaurantList[1].name)))
        )
        onView(withText(restaurantList[0].description)).check(matches(isDisplayed()))
        onView(withText(restaurantList[1].description)).check(matches(isDisplayed()))
    }
}