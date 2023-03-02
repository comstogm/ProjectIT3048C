package com.projectit3048c

import com.projectit3048c.service.FoodService
import com.projectit3048c.service.IFoodService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    single<IFoodService> { FoodService() }
}