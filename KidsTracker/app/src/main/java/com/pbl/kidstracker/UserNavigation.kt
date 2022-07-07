package com.pbl.kidstracker.view

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.pbl.kidstracker.Action

@Composable
fun NavUserContent() {
    val navController = rememberNavController()
    val actions = remember(navController) { Action(navController) }
    NavHost(
        navController = navController,
        startDestination = "user_screen"
    ) {
        composable("user_screen"){
            UserScreen(actions.change_image,
                        actions.update_info,
                        actions.change_password,)
        }
        composable("update_info"){
            Update_Info(actions.userscreen)
        }
        composable("change_password"){
            Change_Password(actions.login)
        }
        composable("change_image"){
            Update_Image(actions.userscreen)
        }
    }

}
@Composable
fun AddNav() {
    val navController = rememberNavController()
    val actions = remember(navController) { Action(navController) }
    NavHost(
        navController = navController,
        startDestination = "add_screen"
    ) {
        composable("add_screen"){
            AddScreen(navController)
        }

        composable("message_screen/{contacUserID}"){
            it.arguments?.getString("contacUserID")?.let { it1 -> MainAddScreen(contacUserID = it1)}

        }
    }

}
