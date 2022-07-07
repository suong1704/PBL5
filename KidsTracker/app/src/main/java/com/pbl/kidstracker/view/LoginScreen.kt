package com.pbl.kidstracker.view

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pbl.kidstracker.R
import com.pbl.kidstracker.viewmodel.LoginViewModel


@Composable
fun LoginScreen(maincontent: () -> Unit,register: () -> Unit, loginViewModel: LoginViewModel = viewModel()){

    val email: String by loginViewModel.email.observeAsState("")
    val password: String by loginViewModel.password.observeAsState("")
    val status: String by loginViewModel.status.observeAsState("")

    val scrollState = rememberScrollState()
    val passwordVisibility = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .verticalScroll(scrollState)
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp))
                .background(Color.White)
                .padding(10.dp)

        ){

            Image(
                painter = painterResource(id = R.drawable.images),
                contentDescription = "Login",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = {loginViewModel.updateEmail(it) },
                label = { Text("Email Address") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_email_24), contentDescription = "icon_email")
                },
            )
            OutlinedTextField(
                value = password,
                onValueChange = { loginViewModel.updatePassword(it)},
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .focusRequester(focusRequester = focusRequester),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_key_24), contentDescription = "icon_Password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_remove_red_eye_24),
                            contentDescription = "icon_iye"
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),

                )
            Spacer(modifier = Modifier.padding(10.dp))
            val context = LocalContext.current
            Button(
                onClick = {
                    if (email.trim().isEmpty() || password.trim().isEmpty() ){
                        Toast.makeText(context, "Please enter all field!", Toast.LENGTH_SHORT).show()
                    }
                    else if (status != ""){
                        Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        loginViewModel.login(maincontent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(text = "Sign In", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                text = "Create New Account",
                modifier = Modifier.clickable(onClick = {
                        loginViewModel.createRegister(register)
                })
            )
        }
    }
}