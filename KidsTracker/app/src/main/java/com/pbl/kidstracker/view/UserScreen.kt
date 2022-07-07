package com.pbl.kidstracker.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.pbl.kidstracker.viewmodel.LoadImageViewModel

@Composable
fun UserScreen(change_image: () -> Unit,
               update_info: () -> Unit,
               change_password: () -> Unit,
               loadImageViewModel: LoadImageViewModel = viewModel()
) {
    val image: String by loadImageViewModel.image.observeAsState("")
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.White)
            .padding(0.dp, 50.dp, 0.dp, 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            border =  BorderStroke(0.5.dp , Color.DarkGray),
            elevation = 4.dp,
            color = MaterialTheme.colors.background
        ) {
            Image(
                rememberAsyncImagePainter(image),
                contentDescription = "profile image",
                modifier = Modifier
                    .size(150.dp)
                    .border(2.dp, Color.Black, CircleShape),
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable(onClick = {
                    change_image()
                })
        ) {
            Text(
                "Đổi ảnh đại diện",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable(onClick = {
                    update_info()
                })
        ) {
            Text(
                "Chỉnh sửa thông tin",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable(onClick = {
                    change_password()
                })
        ) {
            Text(
                "Đổi mật khẩu",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
            )
        }

    }
}