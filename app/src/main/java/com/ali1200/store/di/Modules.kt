package com.ali1200.store.di

import android.content.Context
import com.ali1200.store.model.net.createApiService
import com.ali1200.store.model.repository.user.UserRepository
import com.ali1200.store.model.repository.user.UserRepositoryImpl
import com.ali1200.store.ui.features.signIn.SignInViewModel
import com.ali1200.store.ui.features.signUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {

    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { createApiService() }


    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }

}