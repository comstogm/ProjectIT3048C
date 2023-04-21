package com.projectit3048c

import com.projectit3048c.service.FoodService
import com.projectit3048c.service.IFoodService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Module to provide Koin dependencies
 *
 * [MainViewModel]: View model for the main activity
 * [IFoodService]: Interface for fetching food data
 */
val appModule = module {
    viewModel { MainViewModel(get()) }
    single<IFoodService> { FoodService() }
}