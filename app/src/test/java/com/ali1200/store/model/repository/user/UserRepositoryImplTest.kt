package com.ali1200.store.model.repository.user

import com.ali1200.store.fakes.FakeSharedPreferences
import com.ali1200.store.model.data.LoginResponse
import com.ali1200.store.model.net.ApiService
import com.ali1200.store.model.repository.TokenInMemory
import com.ali1200.store.util.VALUE_SUCCESS
import com.google.gson.JsonObject
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var sharedPref: FakeSharedPreferences
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        sharedPref = FakeSharedPreferences()
        repository = UserRepositoryImpl(apiService, sharedPref)
    }

    @After
    fun tearDown() {
        TokenInMemory.refreshToken(null, null)
    }

    @Test
    fun signIn_success_savesTokenAndUsername() = runBlocking {
        coEvery { apiService.signIn(any()) } returns LoginResponse(0, "ok", true, "token-1")

        val result = repository.signIn("user@mail.com", "12345678")

        assertEquals(VALUE_SUCCESS, result)
        assertEquals("token-1", repository.getToken())
        assertEquals("user@mail.com", repository.getUserName())
        assertEquals("token-1", TokenInMemory.token)
    }

    @Test
    fun signIn_failure_returnsServerMessageAndSavesNothing() = runBlocking {
        coEvery { apiService.signIn(any()) } returns LoginResponse(0, "wrong password", false, "")

        val result = repository.signIn("user@mail.com", "bad")

        assertEquals("wrong password", result)
        assertNull(repository.getToken())
        assertNull(TokenInMemory.token)
    }

    @Test
    fun signIn_sendsEmailAndPasswordToServer() = runBlocking {
        val body = slot<JsonObject>()
        coEvery { apiService.signIn(capture(body)) } returns LoginResponse(0, "ok", true, "t")

        repository.signIn("user@mail.com", "12345678")

        assertEquals("user@mail.com", body.captured.get("email").asString)
        assertEquals("12345678", body.captured.get("password").asString)
    }

    @Test
    fun signUp_success_savesToken() = runBlocking {
        coEvery { apiService.signUp(any()) } returns LoginResponse(0, "ok", true, "token-2")

        val result = repository.signUp("Ali", "user@mail.com", "12345678")

        assertEquals(VALUE_SUCCESS, result)
        assertEquals("token-2", repository.getToken())
    }

    @Test
    fun loadToken_restoresSessionFromStorage() {
        repository.saveToken("saved-token")
        repository.saveUserName("user@mail.com")

        repository.loadToken()

        assertEquals("saved-token", TokenInMemory.token)
        assertEquals("user@mail.com", TokenInMemory.username)
    }
}
