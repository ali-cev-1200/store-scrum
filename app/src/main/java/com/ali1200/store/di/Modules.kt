package com.ali1200.store.di

import android.content.Context
import androidx.room.Room
import com.ali1200.store.model.db.AppDatabase
import com.ali1200.store.model.net.createApiService
import com.ali1200.store.model.repository.product.ProductRepository
import com.ali1200.store.model.repository.product.ProductRepositoryImpl
import com.ali1200.store.model.repository.user.UserRepository
import com.ali1200.store.model.repository.user.UserRepositoryImpl
import com.ali1200.store.ui.features.main.MainViewModel
import com.ali1200.store.ui.features.signIn.SignInViewModel
import com.ali1200.store.ui.features.signUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {

    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { createApiService() }

    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_dataBase.db").build() }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get<AppDatabase>().productDao()) }

    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { (isNetConnected: Boolean) -> MainViewModel(get(), isNetConnected) }

}