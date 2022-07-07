package com.pbl.kidstracker.view

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.libraries.maps.model.PolylineOptions
import com.google.maps.android.ktx.awaitMap
import com.pbl.kidstracker.R
import com.pbl.kidstracker.model.Heartbeat
import com.pbl.kidstracker.model.User
import com.pbl.kidstracker.viewmodel.AddScreenViewModel
import com.pbl.kidstracker.viewmodel.HomeViewModel
import com.pbl.kidstracker.viewmodel.TopBarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MapsScreen() {
    MyMap()
}



@Composable
fun MyMap(topBarViewModel: TopBarViewModel = viewModel(), homeViewModel: HomeViewModel = viewModel(), addScreenViewModel: AddScreenViewModel = viewModel()) {
    val name: String by topBarViewModel.name.observeAsState("")
    val userlat: Double by homeViewModel.lat.observeAsState(16.0740959)
    val userlon: Double by homeViewModel.lon.observeAsState(108.1366866)
    val mapView = rememberMapViewWithLifeCycle()
    addScreenViewModel.getListContactUser()
    val contactUser: List<User> by addScreenViewModel.listContactUser.observeAsState(
        initial = emptyList<User>().toMutableList()
    )

    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        AndroidView(
            { mapView }
        ) { mapView ->
            CoroutineScope(Dispatchers.Main).launch {
                val map = mapView.awaitMap()
                if (userlon != 0.0 && userlat != 0.0){
                    map.uiSettings.isZoomControlsEnabled = false
                    val destination = LatLng(userlat, userlon)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 30f))
                    val markerOptionsDestination = MarkerOptions()
                        .position(destination)
                        .title(name)
                    map.addMarker(markerOptionsDestination)
                    map.addPolyline(
                        PolylineOptions().add(
                            destination,
                        )
                    ).color = R.color.red
                }
                for (user in contactUser){
                    Log.d("tag", user.getlocation().toString())
                    if (user.getlocation() != "0.0,0.0" && user.getlocation() != "0.000000,0.000000" && user.getlocation() != "") {
                        val latlon: List<String> = user.getlocation().toString().split(",")
                        val lat = latlon[0].toDouble()
                        val lon = latlon[1].toDouble()
                        val destination1 = LatLng(lat, lon)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination1, 30f))
                        val markerOptionsDestination1 = MarkerOptions()
                            .position(destination1)
                            .title(user.getname())
                            .alpha(8F)
                        map.addMarker(markerOptionsDestination1)
                        map.addPolyline(
                            PolylineOptions().add(
                                destination1
                            )
                        ).color = R.color.red
                    }
                }


                map.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifeCycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map_frame
        }
    }
    val lifeCycleObserver = rememberMapLifecycleObserver(mapView)
    val lifeCycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifeCycle) {
        lifeCycle.addObserver(lifeCycleObserver)
        onDispose {
            lifeCycle.removeObserver(lifeCycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }
