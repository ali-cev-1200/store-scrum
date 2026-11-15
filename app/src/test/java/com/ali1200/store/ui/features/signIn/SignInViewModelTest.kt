package com.ali1200.store.ui.features.signIn

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ali1200.store.fakes.MainDispatcherRule
import com.ali1200.store.model.repository.user.UserRepository
import com.ali1200.store.util.VALUE_SUCCESS
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SignInViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun signInUser_passesFieldsToRepositoryAndReturnsResult() {
        val repository: UserRepository = mockk()
        coEvery { repository.signIn("user@mail.com", "12345678") } returns VALUE_SUCCESS
        val viewModel = SignInViewModel(repository)
        viewModel.email.value = "user@mail.com"
        viewModel.password.value = "12345678"

        var result = ""
        viewModel.signInUser { result = it }

        assertEquals(VALUE_SUCCESS, result)
        coVerify { repository.signIn("user@mail.com", "12345678") }
    }
}
