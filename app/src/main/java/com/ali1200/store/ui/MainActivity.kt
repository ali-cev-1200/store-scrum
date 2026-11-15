package com.ali1200.store.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ali1200.store.ui.features.IntroScreen
import com.ali1200.store.ui.theme.BackgroundMain
import com.ali1200.store.ui.theme.MainAppTheme
import com.ali1200.store.util.MyScreens
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import com.ali1200.store.di.myModules
import com.ali1200.store.model.repository.TokenInMemory
import com.ali1200.store.model.repository.user.UserRepository
import org.koin.android.ext.koin.androidContext
import com.ali1200.store.ui.features.main.MainScreen
import com.ali1200.store.ui.features.signUp.SignUpScreen
import com.ali1200.store.ui.features.signIn.SignInScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContent {

            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {
                MainAppTheme {
                    Surface(color = BackgroundMain, modifier = Modifier.fillMaxSize()) {

                        val userRepository :UserRepository = get()
                        userRepository.loadToken()

                        DuniBazaarUi()

                    }
                }
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            DuniBazaarUi()
        }
    }
}

@Composable
fun DuniBazaarUi() {
    val navController = rememberNavController()
    KoinNavHost(navController = navController, startDestination = MyScreens.MainScreen.route) {

        composable(MyScreens.MainScreen.route) {

            if(TokenInMemory.token != null) {
                MainScreen()
            } else {
                IntroScreen()
            }

        }

        composable(MyScreens.ProductScreen.route + "/{productId}") {
            NotReadyScreen("Product")
        }

        composable(MyScreens.CategoryScreen.route + "/{categoryName}") {
            NotReadyScreen("Category")
        }

        composable(MyScreens.ProfileScreen.route) {
            NotReadyScreen("Profile")
        }

        composable(MyScreens.CartScreen.route) {
            NotReadyScreen("Cart")
        }

        composable(MyScreens.SignUpScreen.route) {
            SignUpScreen()
        }

        composable(MyScreens.SignInScreen.route) {
            SignInScreen()
        }

    }

}

/** Placeholder for screens planned in later sprints (see docs/roadmap.md). */
@Composable
fun NotReadyScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "$name screen is coming in a later sprint")
    }
}
