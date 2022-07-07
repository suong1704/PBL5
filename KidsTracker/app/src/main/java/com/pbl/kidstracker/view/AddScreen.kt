package com.pbl.kidstracker.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.pbl.kidstracker.R
import com.pbl.kidstracker.model.Message
import com.pbl.kidstracker.model.Notification
import com.pbl.kidstracker.model.User
import com.pbl.kidstracker.viewmodel.AddScreenViewModel
import com.pbl.kidstracker.viewmodel.MessageViewModel
import com.pbl.kidstracker.viewmodel.NotifyViewModel
import java.text.SimpleDateFormat


@Composable
fun MainAddScreen(contacUserID: String,addScreenViewModel: AddScreenViewModel = viewModel()){
    val isChat: Boolean by addScreenViewModel.isChat.observeAsState(false)
    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ){
            Button(
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green)),
                onClick = {
                    addScreenViewModel.updateIsChat(false)
                },
            ){
                Text(text = "Notify", fontSize = 10.sp)
            }
            Button(
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green)),
                onClick = {
                    addScreenViewModel.updateIsChat(true)
                },
            ){
                Text(text = "Chat", fontSize = 10.sp)
            }
        }
        if (isChat == true){
            ChatScreen(contacUserID)
        }
        else{
            NotifyScreen(contacUserID)
        }
    }

}
@Composable
fun Portfolio(navController: NavHostController,message: () -> Unit,addScreenViewModel: AddScreenViewModel = viewModel()){
    val listHeartBeat: List<User> by addScreenViewModel.listContactUser.observeAsState(
        initial = emptyList<User>().toMutableList()
    )
    fun navigateToUser(contactUserID: String) {
        val id = Gson().toJson(contactUserID)
        navController.navigate("userDetailsView/$id")
    }
    if (listHeartBeat.isNotEmpty()){
        LazyColumn{
            items(listHeartBeat){
                    item ->
                Card(
                    modifier = Modifier
                        .padding(13.dp)
                        .fillMaxWidth()
                        .clickable(onClick = {

                            item
                                .getuserid()
                                ?.let { navigateToUser(it) }
//                            message()
//                            MainAddScreen()
                        }),
                    elevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(MaterialTheme.colors.surface),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start

                    ) {
                        CreateImageProfile(item.getimage().toString())
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(CenterVertically)
                        ) {
                            item.getname()?.let { Text(text = it, fontWeight = FontWeight.Bold) }
                            item.getemail()
                                ?.let { Text(text = it,style = MaterialTheme.typography.body2 ) }
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .align(CenterVertically),

                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_heart),"",
                                tint = Color.Red,
                            )
                            item.getheartbeat()
                                ?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.body2,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.align(Alignment.Center)
                                    )}

                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Top) {
                        Icon(imageVector = Icons.Filled.Close,
                            contentDescription = "remove",
                            modifier = Modifier.clickable {
                                        addScreenViewModel.deleteContactUser(item.getuserid().toString())
                            })
                    }
                }
            }
        }
    }
}


@Composable
fun CreateImageProfile(image: String?){
    Surface(
        modifier = Modifier
            .padding(5.dp),
        color = colorResource(id = R.color.purple_200),
        shape = CircleShape,
        border =  BorderStroke(0.5.dp , Color.DarkGray),
        elevation = 4.dp,
    ) {
        Image(
            rememberAsyncImagePainter(image),
            contentDescription = "image",
            modifier = Modifier.size(70.dp),
            contentScale = ContentScale.Crop)
    }
}
@Composable
fun AddScreen(navController: NavHostController, addScreenViewModel: AddScreenViewModel = viewModel()) {
    val email: String by addScreenViewModel.email.observeAsState("")
    val isHide: Boolean by addScreenViewModel.isHide.observeAsState(true)
    val status: String by addScreenViewModel.status.observeAsState("")
    val focusRequester = remember { FocusRequester() }
    val listHeartBeat: List<User> by addScreenViewModel.listContactUser.observeAsState(
        initial = emptyList<User>().toMutableList()
    )
    fun navigateToUser(contactUserID: String) {
        navController.navigate("message_screen/$contactUserID")
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.green))

    ){
        if (listHeartBeat.isNotEmpty()){
            LazyColumn{
                items(listHeartBeat){
                        item ->
                    Card(
                        modifier = Modifier
                            .padding(13.dp)
                            .fillMaxWidth()
                            .clickable(onClick = {
                                item.getuserid()
                                    ?.let { navigateToUser(it) }
                            }),
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .background(MaterialTheme.colors.surface),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start

                        ) {
                            CreateImageProfile(item.getimage().toString())
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(CenterVertically)
                            ) {
                                item.getname()?.let { Text(text = it, fontWeight = FontWeight.Bold) }
                                item.getemail()
                                    ?.let { Text(text = it,style = MaterialTheme.typography.body2 ) }
                            }
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(CenterVertically),

                                ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_heart),"",
                                    tint = Color.Red,
                                )
                                item.getheartbeat()
                                    ?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.body2,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.align(Alignment.Center)
                                        )}
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.Top) {
                            Icon(imageVector = Icons.Filled.Close,
                                contentDescription = "remove",
                                modifier = Modifier.clickable {
                                    addScreenViewModel.deleteContactUser(item.getuserid().toString())
                                })
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(15.dp, 70.dp)
                .background(
                    colorResource(id = R.color.green)

                ),
            shape = RoundedCornerShape(
                topStart = 30.dp, topEnd = 30.dp, bottomStart = 30.dp, bottomEnd = 2.dp
            ),
            contentColor = Color(0xFFFEDBD0),
            onClick = {
                addScreenViewModel.updateIsHide(!isHide)

            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
    if(isHide){
        Surface(
            modifier = Modifier.padding(5.dp,200.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(0.dp, 5.dp)
                    .wrapContentWidth()
                    .height(120.dp)
                    .background(Color.White)
                    .border(2.dp, Color.Black, RoundedCornerShape(2.dp))

            ){
                OutlinedTextField(
                    value = email,
                    onValueChange = { addScreenViewModel.updateEmail(it) },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester = focusRequester)
                        .align(Alignment.TopCenter)
                        .padding(5.dp, 5.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
                val context = LocalContext.current
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green)),
                    onClick = {
                        addScreenViewModel.AddContactUSer()
                        addScreenViewModel.updateEmail("")
                        if (status.isNotEmpty()){
                            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.BottomCenter)
                        .padding(5.dp),
                ) {
                    Text(text = "Save", fontSize = 10.sp)
                }

            }
        }
    }


}

@Composable
fun ChatScreen(contacUserID: String,messageViewModel: MessageViewModel = viewModel()){
    val messages: List<Message> by messageViewModel.messages.observeAsState(
        initial = emptyList<Message>().toMutableList()
    )
    messageViewModel.getMessagesByID(contacUserID)
    val id : String by messageViewModel.contactUserID.observeAsState(contacUserID)
    val message: String by messageViewModel.message.observeAsState("")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 0.85f, fill = true),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                val isCurrentUser = message.getIsCurrentUser()
                if (isCurrentUser != null) {
                    ChatMessage(
                        message = message.getmessage().toString(),
                        isCurrentUser = isCurrentUser
                    )
                }
            }
        }
        OutlinedTextField(
            value = message,
            onValueChange = {messageViewModel.updateMessage(it)},
            label = {
                Text(
                    "Type Your Message"
                )
            },
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 1.dp)
                .fillMaxWidth()
                .padding(1.dp, 1.dp, 1.dp, 60.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        messageViewModel.SendMessage(contacUserID)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Button"
                    )
                }
            }
        )
    }
}
@Composable
fun NotifyScreen(contacUserID: String,notifyViewModel: NotifyViewModel = viewModel()){
    val notifications: List<Notification> by notifyViewModel.notifications.observeAsState(
        initial = emptyList<Notification>().toMutableList()
    )
    notifyViewModel.getNotificationByID(contacUserID)
    val id : String by notifyViewModel.contactUserID.observeAsState(contacUserID)



    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        reverseLayout = true
    ) {
        items(notifications) {
            message -> SingleMessage(message)
        }
    }
}
@Composable
fun SingleMessage(notification: Notification) {
    Card(
    ) {
        Row() {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { /* Nothing yet */ }
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                notification.getnotify()?.let {
                    Text(
                        text = it,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        color =  Color.Black,
                        fontWeight = FontWeight.Bold,
                    )
                }
                notification.getheartbeat()?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        text = it,
                        textAlign = TextAlign.Start,
                        fontStyle = FontStyle.Italic,

                        color =  Color.Black
                    )
                }
                Row {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(CenterVertically),

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_heart), "",
                            tint = Color.Red,
                        )
                    }
                    notification.gettime()?.let {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            text = it,
                            style = MaterialTheme.typography.subtitle1,
                            textAlign = TextAlign.End,
                            color =  Color.Black,
                            fontStyle = FontStyle.Italic,
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ChatMessage(message: String, isCurrentUser: Boolean) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (isCurrentUser) colorResource(id = R.color.green) else colorResource(id = R.color.primary)
    ) {
        Text(
            text = message,
            textAlign =
            if (isCurrentUser)
                TextAlign.End
            else
                TextAlign.Start,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color.Black
        )
    }
}


