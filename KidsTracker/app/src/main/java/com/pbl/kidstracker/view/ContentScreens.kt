package com.pbl.kidstracker


import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.pbl.kidstracker.view.*
import com.pbl.kidstracker.viewmodel.LoadImageViewModel
import com.pbl.kidstracker.viewmodel.TopBarViewModel

@Composable
fun MainContent(login: () -> Unit) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { User_Info(login) },
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Navigation(navController = navController)
    }
}

@Composable
fun MainContentPreview() {
}

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen()
        }
        composable(NavigationItem.Add.route) {
            AddNav()
        }
        composable(NavigationItem.Maps.route) {
            MapsScreen()
        }
        composable(NavigationItem.User.route) {
            NavUserContent()
        }
    }
}
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Add,
        NavigationItem.Maps,
        NavigationItem.User,
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.green),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = colorResource(id = R.color.darkgreen),
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
@Composable
fun User_Info(login: () -> Unit, topBarViewModel: TopBarViewModel = viewModel() ,loadImageViewModel: LoadImageViewModel = viewModel()  ){
    val name: String by topBarViewModel.name.observeAsState("")
    val phone: String by topBarViewModel.phone.observeAsState("")
    val image: String by loadImageViewModel.image.observeAsState("")
    Row (
        modifier = Modifier
            .background(colorResource(id = R.color.darkgreen))
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Surface(
            modifier = Modifier
                .padding(10.dp),
            shape = CircleShape,
            border =  BorderStroke(0.5.dp , Color.DarkGray),
            elevation = 4.dp,
            color = MaterialTheme.colors.background
        ) {
            Image(
                rememberAsyncImagePainter(image),
                contentDescription = "profile image",
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier.width(250.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.subtitle1,
                color = Color.White,
            )
            Text(text = phone, modifier = Modifier.padding(3.dp), color = Color.White)
        }
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_logout_24),
            contentDescription = "profile image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = {
                    topBarViewModel.Logout(login)
                }),
        )
    }
}





