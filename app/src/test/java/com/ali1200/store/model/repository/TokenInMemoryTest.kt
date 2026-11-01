package com.ali1200.store.model.repository

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TokenInMemoryTest {

    @After
    fun tearDown() {
        TokenInMemory.refreshToken(null, null)
    }

    @Test
    fun refreshToken_storesUsernameAndToken() {
        TokenInMemory.refreshToken("user@mail.com", "abc123")

        assertEquals("user@mail.com", TokenInMemory.username)
        assertEquals("abc123", TokenInMemory.token)
    }

    @Test
    fun refreshToken_withNull_clearsSession() {
        TokenInMemory.refreshToken("user@mail.com", "abc123")
        TokenInMemory.refreshToken(null, null)

        assertNull(TokenInMemory.username)
        assertNull(TokenInMemory.token)
    }
}
