package com.ali1200.store.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Sprint 3: new user signs in and lands on the home screen with products. */
class SignInE2ETest : BaseE2ETest() {

    @Test
    fun signIn_opensHomeWithProducts() {
        launchApp()

        composeRule.onNodeWithText("Sign In").performClick()
        waitForText("Log In")
        field("Email").performTextInput(EMAIL)
        field("Password").performTextInput("12345678")
        composeRule.onNodeWithText("Log In").performClick()

        waitForText("Leather Backpack")
        composeRule.onNodeWithText("Duni Bazaar").assertIsDisplayed()
        assertTrue(FakeBackend.lastBody("/signIn").contains(EMAIL))
        assertTrue(FakeBackend.paths().contains("/getProducts"))
    }

    @Test
    fun signIn_wrongPassword_staysOnSignInScreen() {
        launchApp()

        composeRule.onNodeWithText("Sign In").performClick()
        waitForText("Log In")
        field("Email").performTextInput(EMAIL)
        field("Password").performTextInput("wrongpass")
        composeRule.onNodeWithText("Log In").performClick()

        waitForRequest("/signIn")
        composeRule.onNodeWithText("Log In").assertIsDisplayed()
        assertNull(prefs.getString("token", null))
    }
}
