package com.pbl.kidstracker.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pbl.kidstracker.R
import com.pbl.kidstracker.model.Heartbeat
import com.pbl.kidstracker.viewmodel.HomeViewModel

@Composable
fun HomeScreen( homeViewModel: HomeViewModel = viewModel()) {
    val heartBeat: String by homeViewModel.heartbeat.observeAsState("")
    val listHeartBeat: List<Heartbeat> by homeViewModel.listHeartBeat.observeAsState(
        initial = emptyList<Heartbeat>().toMutableList()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.green))
            .wrapContentSize(Alignment.Center)

    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.green)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Surface (
                modifier = Modifier
                    .clickable(
                        onClick = {
                            homeViewModel.sendCurrentHeartBeat()
                        }
                    ),
                color = colorResource(id = R.color.green)
            ){
                InfiniteAnimation()
            }
            Text(
                text = heartBeat,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 50.sp
            )
            LazyColumn{
                items(listHeartBeat){
                        item ->
                    Card(
                        modifier = Modifier
                            .padding(13.dp)
                            .fillMaxWidth(),
                        elevation = 4.dp
                    ){
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .background(MaterialTheme.colors.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                item.getHeartbeat()
                                    ?.let { Text(text = it, fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Start,
                                        fontStyle = FontStyle.Italic,
                                    ) }
                                item.getTime()
                                    ?.let {
                                        Text(text = it,
                                            style = MaterialTheme.typography.subtitle1,
                                            textAlign = TextAlign.End,
                                            color =  Color.Black,
                                            fontStyle = FontStyle.Italic
                                        )
                                    }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Filled.Close,
                                    contentDescription = "remove",
                                    modifier = Modifier.clickable {
                                        homeViewModel.deleteIteminListHeartbeat(item.getTime().toString())
                                    })
                            }
                        }
                    }
                }
            }

        }
    }
}
@Composable
fun InfiniteAnimation() {
    val infiniteTransition = rememberInfiniteTransition()

    val heartSize by infiniteTransition.animateFloat(
        initialValue = 50.0f,
        targetValue = 100.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, delayMillis = 100,easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Image(
        painter = painterResource(R.drawable.ic_heart),
        contentDescription = "heart",
        colorFilter = ColorFilter.tint(color = Color.Red),
        modifier = Modifier
            .size(heartSize.dp)


    )
}