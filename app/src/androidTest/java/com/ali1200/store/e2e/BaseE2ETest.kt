package com.ali1200.store.e2e

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.ali1200.store.model.net.ApiConfig
import com.ali1200.store.ui.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseE2ETest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    private var scenario: ActivityScenario<MainActivity>? = null

    protected val prefs: SharedPreferences
        get() = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("data", Context.MODE_PRIVATE)

    @Before
    fun resetApp() {
        FakeBackend.reset()
        ApiConfig.baseUrl = FakeBackend.baseUrl
        prefs.edit().clear().commit()
        // The Room cache is not deleted here: Room keeps it open across tests,
        // and the products are reloaded from FakeBackend on every launch.
    }

    @After
    fun closeApp() {
        scenario?.close()
    }

    protected fun launchApp(loggedIn: Boolean = false) {
        if (loggedIn) {
            prefs.edit()
                .putString("token", "test-token")
                .putString("username", EMAIL)
                .putString("login_time", System.currentTimeMillis().toString())
                .commit()
        }
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    protected fun waitForText(text: String) {
        composeRule.waitUntil(TIMEOUT) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    protected fun waitForRequest(path: String) {
        composeRule.waitUntil(TIMEOUT) { FakeBackend.paths().contains(path) }
    }

    protected fun field(label: String): SemanticsNodeInteraction =
        composeRule.onNode(hasSetTextAction() and hasText(label))

    companion object {
        const val EMAIL = "student@mail.com"
        private const val TIMEOUT = 10_000L
    }
}
