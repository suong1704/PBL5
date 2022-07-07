package com.pbl.kidstracker


import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pbl.kidstracker.viewmodel.RegisterViewModel


@Composable
fun RegisterScreen(maincontent: () -> Unit, loginscreen: () -> Unit, registerViewModel: RegisterViewModel = viewModel()) {

    val name: String by registerViewModel.name.observeAsState("")
    val email: String by registerViewModel.email.observeAsState("")
    val phone: String by registerViewModel.phone.observeAsState("")
    val password: String by registerViewModel.password.observeAsState("")
    val device: String by registerViewModel.device.observeAsState("")
    val status: String by registerViewModel.status.observeAsState("")

    val confirmpassword =  remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }
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
                .padding(5.dp)

        ){
            Image(
                painter = painterResource(id = R.drawable.sign_up),
                contentDescription = "Signup",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = {registerViewModel.updateName(it)},
                label = { Text("Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_person_outline_24), contentDescription = "icon_email")
                },
            )
            OutlinedTextField(
                value = email,
                onValueChange = {registerViewModel.updateEmail(it)},
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
                value = phone,
                onValueChange = {registerViewModel.updatePhone(it)},
                label = { Text("Phone number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_local_phone_24), contentDescription = "icon_email")
                },
            )
            OutlinedTextField(
                value = device,
                onValueChange = {registerViewModel.updateDevice(it)},
                label = { Text("Device code") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_watch_24), contentDescription = "icon_email")
                },
            )
            OutlinedTextField(
                value = password,
                onValueChange = { registerViewModel.updatePassword(it) },
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
                            contentDescription = "icon_eye"
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),

                )
            OutlinedTextField(
                value = confirmpassword.value,
                onValueChange = { confirmpassword.value = it },
                label = { Text("Confirm Password") },
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
                        confirmPasswordVisibility.value = !confirmPasswordVisibility.value
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_remove_red_eye_24),
                            contentDescription = "icon_eye_1"
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),

                )
            Spacer(modifier = Modifier.padding(10.dp))
            val context = LocalContext.current
            Button(
                onClick = {
                    if (email!!.trim().isEmpty() or password!!.trim().isEmpty() or name!!.trim().isEmpty() or confirmpassword.value!!.trim().isEmpty() or phone!!.trim().isEmpty() or device!!.trim().isEmpty()){
                        Toast.makeText(context, "Please enter all field!", Toast.LENGTH_SHORT).show()
                    }
                    else if (phone!!.trim().length != 10){
                        Toast.makeText(context, "invalid phone number!", Toast.LENGTH_SHORT).show()
                    }
                    else if (password!!.trim().length < 6){
                        Toast.makeText(context, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show()
                    }
                    else if (password!!.trim().equals(confirmpassword.value!!.trim()) == false){
                        Toast.makeText(context, "Password and Confirm Password must be same!", Toast.LENGTH_SHORT).show()
                    }
                    else if (status != ""){
                        Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        registerViewModel.register(maincontent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(text = "Sign Up", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                text = "You already have an Account?",
                modifier = Modifier.clickable(onClick = {
                    loginscreen()
                })
            )
        }
    }
}




