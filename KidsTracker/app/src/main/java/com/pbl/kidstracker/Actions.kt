package com.pbl.kidstracker

import androidx.navigation.NavHostController

class Action(navController: NavHostController) {
    val login: () -> Unit = { navController.navigate("login_screen")}
    val register: () -> Unit = { navController.navigate("register_screen")  }
    val maincontent: () -> Unit = { navController.navigate("main_content")  }
    val userscreen: () -> Unit = { navController.navigate("user_screen") }
    val update_info: () -> Unit = { navController.navigate("update_info") }
    val change_password: () -> Unit = { navController.navigate("change_password") }
    val change_image: () -> Unit = { navController.navigate("change_image") }
    val messagescreen: () -> Unit = { navController.navigate("message_screen") }
    val logout: () -> Unit = { navController.navigate("login_screen")}
}