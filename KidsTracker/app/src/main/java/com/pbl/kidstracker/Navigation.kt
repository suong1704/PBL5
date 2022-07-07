import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pbl.kidstracker.Action
import com.pbl.kidstracker.MainContent
import com.pbl.kidstracker.RegisterScreen
import com.pbl.kidstracker.ui.theme.KidsTrackerTheme
import com.pbl.kidstracker.view.Change_Password
import com.pbl.kidstracker.view.LoginScreen
import com.pbl.kidstracker.view.Update_Info

@Composable
fun NavComposeApp() {
    val navController = rememberNavController()
    val actions = remember(navController) { Action(navController) }
    KidsTrackerTheme {
        NavHost(
            navController = navController,
            startDestination = "login_screen"
        ){
            composable("register_screen"){
                RegisterScreen(actions.maincontent, actions.login)
            }
            composable("login_screen"){
                LoginScreen(actions.maincontent,actions.register)
            }
            composable("main_content"){
                MainContent(actions.logout)
            }
//            composable("logout_screen"){
//                LoginScreen(actions.maincontent,actions.register)
//            }

        }
    }
}



