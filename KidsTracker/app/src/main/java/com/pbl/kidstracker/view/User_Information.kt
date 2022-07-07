package com.pbl.kidstracker.view

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.pbl.kidstracker.*
import com.pbl.kidstracker.R
import androidx.compose.ui.graphics.asImageBitmap
import coil.compose.rememberAsyncImagePainter
import com.pbl.kidstracker.viewmodel.*
import java.util.*

@Composable
fun Update_Info(userscreen: () -> Unit,updateInfoViewModel: UpdateInfoViewModel = viewModel() , loadImageViewModel: LoadImageViewModel= viewModel()){
    val name: String by updateInfoViewModel.name.observeAsState("")
    val phone: String by updateInfoViewModel.phone.observeAsState("")
    val age: String by updateInfoViewModel.age.observeAsState("")
    val gender: String by updateInfoViewModel.gender.observeAsState("")
    val status: String by updateInfoViewModel.status.observeAsState("")
    val image: String by loadImageViewModel.image.observeAsState("")

    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Male", "Female", "Other")
    var selectedText by remember { mutableStateOf("") }

    var textfieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp))
                .background(Color.White)
                .padding(10.dp, 50.dp, 10.dp, 10.dp)

        ){
            Surface(
                shape = CircleShape,
                border =  BorderStroke(0.5.dp , Color.DarkGray),
                elevation = 4.dp,
                color = MaterialTheme.colors.background
            ){
            Image(
                rememberAsyncImagePainter(image),
                contentDescription = "profile image",
                modifier = Modifier
                    .size(150.dp)
                    .border(2.dp, Color.Black, CircleShape),
                contentScale = ContentScale.Crop,

            )}
            OutlinedTextField(
                value = name,
                onValueChange = {updateInfoViewModel.updateName(it)},
                label = { Text("Full Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_person_outline_24), contentDescription = "icon_email")
                },
            )
            OutlinedTextField(
                value = phone,
                onValueChange = {updateInfoViewModel.updatePhone(it)},
                label = { Text("Phone number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_local_phone_24), contentDescription = "icon_email")
                },
            )
            OutlinedTextField(
                value = age,
                onValueChange = {updateInfoViewModel.updateAge(it)},
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_calendar_today_24), contentDescription = "icon_email")
                },
            )

            Column() {
                OutlinedTextField(
                    value = gender,
                    onValueChange = { selectedText = it; },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .focusRequester(focusRequester = focusRequester)
                        .onGloballyPositioned { coordinates ->
                            textfieldSize = coordinates.size.toSize()
                        },
                    label = { Text("Gender") },
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable { expanded = !expanded })
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_female_24), contentDescription = "icon_gender")
                    },
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
                ) {
                    suggestions.forEach { label ->
                        DropdownMenuItem(onClick = {
                            selectedText = label
                            updateInfoViewModel.updateGender(selectedText)
                            expanded = false
                        }) {
                            Text(text = label)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            val context = LocalContext.current
            Button(
                onClick = {
                    if (phone!!.trim().length != 10){
                    Toast.makeText(context, "Invalid phone number!", Toast.LENGTH_SHORT).show()
                    }else {
                        updateInfoViewModel.updateUserInfo(userscreen)
                    }
                    if (status.isNotEmpty()){
                        Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                    }

                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(text = "Save", fontSize = 20.sp)
            }

        }
    }

}

@Composable
fun Change_Password(userscreen: () -> Unit,  updatePasswordViewModel: UpdatePasswordViewModel = viewModel()){

    val oldpassword: String by updatePasswordViewModel.oldPassword.observeAsState("")
    val newpassword: String by updatePasswordViewModel.newPassword.observeAsState("")
    val status: String by updatePasswordViewModel.status.observeAsState("")

    val confirmNewPassword =  remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val oldPasswordVisibility = remember { mutableStateOf(false) }
    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp))
                .background(Color.White)
                .padding(10.dp, 50.dp, 10.dp, 10.dp)

        ){

            Image(
                painter = painterResource(id = R.drawable.change_password),
                contentDescription = "Change password",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            OutlinedTextField(
                value = oldpassword,
                onValueChange = { updatePasswordViewModel.updateOldPassword(it) },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .focusRequester(focusRequester = focusRequester),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_key_24), contentDescription = "icon_Password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        oldPasswordVisibility.value = !oldPasswordVisibility.value
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_remove_red_eye_24),
                            contentDescription = "icon_eye_1"
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (oldPasswordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),
            )
            OutlinedTextField(
                value = newpassword,
                onValueChange = {updatePasswordViewModel.updateNewPassword(it)},
                label = { Text("New Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .focusRequester(focusRequester = focusRequester),
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
                value = confirmNewPassword.value   ,
                onValueChange = {confirmNewPassword.value = it},
                label = { Text("Confirm New Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .focusRequester(focusRequester = focusRequester),
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
                    if (oldpassword!!.trim().isEmpty() or newpassword!!.trim().isEmpty() or confirmNewPassword!!.value.trim().isEmpty() ){
                        Toast.makeText(context, "Please enter all field!", Toast.LENGTH_SHORT).show()
                    }
                    else if (newpassword!!.trim().length < 6){
                        Toast.makeText(context, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show()
                    }
                    else if (newpassword!!.trim().equals(confirmNewPassword.value!!.trim()) == false){
                        Toast.makeText(context, "Password and Confirm Password must be same!", Toast.LENGTH_SHORT).show()
                    }
                    else if (status != ""){
                        Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        updatePasswordViewModel.changePassword(userscreen)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(text = "Save", fontSize = 20.sp)
            }

        }
    }
}
@Composable
fun Update_Image(change_image: () -> Unit,  changeImageViewModel: changeImageViewModel = viewModel()){

    val image: String by changeImageViewModel.image.observeAsState("")
    val status: String by changeImageViewModel.status.observeAsState("")


    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp)
                        .padding(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Pick Image")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            if (imageUri != null ){
                val fname = UUID.randomUUID().toString();
                val ref = FirebaseStorage.getInstance().getReference("/images/$fname")

                ref.putFile(imageUri!!).addOnSuccessListener {
                    Log.d("Done Upload","Load")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("img",it.toString())
                        val userid = FirebaseAuth.getInstance().uid
                        val ref1 = FirebaseDatabase.getInstance().getReference("/Users/$userid")
                        ref1.child("image").setValue(it.toString())
                    }
                }

            }
        }) {
            Text(text = "SAVE")
        }
    }
}


