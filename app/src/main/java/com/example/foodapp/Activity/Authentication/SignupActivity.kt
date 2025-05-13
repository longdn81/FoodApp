package com.example.foodapp.Activity.Authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.Activity.BaseActivity
import com.example.foodapp.Activity.Dashboard.MainActivity
import com.example.foodapp.Model.UserModel
import com.example.foodapp.R
import com.example.foodapp.ViewModel.AuthState
import com.example.foodapp.ViewModel.AuthViewModel

class SignupActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            SignupScreen(
                modifier = Modifier,
                authViewModel = AuthViewModel()
            )
        }
    }
}

@Composable
fun SignupScreen(modifier: Modifier = Modifier, authViewModel: AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }
    var showPassword by remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    // Kiểm tra trạng thái của DatePickerDialog
    val showDatePickerDialog = remember { mutableStateOf(false) }

    // DatePicker để lấy ngày sinh
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Cập nhật ngày sinh khi người dùng chọn
            birthDate = "$dayOfMonth/${month + 1}/$year"
        },
        2005, 1, 1  // Giá trị mặc định cho năm, tháng, ngày (có thể thay đổi)
    )

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated ->{
                Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)

                // Nếu bạn muốn đóng Activity hiện tại:
                if (context is Activity) {
                    context.finish()
                }
            }
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.user),
            contentDescription = "Signup Illustration",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Sign Up",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "Name Icon")
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Phone Field
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = "Phone Icon")
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Address Field
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            leadingIcon = {
                Icon(Icons.Default.Home, contentDescription = "Address Icon")
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Birthdate Field (Chọn ngày sinh)
        OutlinedTextField(
            value = birthDate,
            onValueChange = {},
            label = { Text("Date of Birth") },
            leadingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Birthdate Icon")
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePickerDialog.value = true }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Open DatePicker")
                }
            }
        )

        // Hiển thị DatePicker khi `showDatePickerDialog` là true
        if (showDatePickerDialog.value) {
            datePickerDialog.show()
            showDatePickerDialog.value = false  // Ẩn dialog sau khi chọn xong
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email Icon")
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password Icon")
            },
            trailingIcon = {
                // Thay đổi icon khi click
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password"
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val user = UserModel(
                    name = name,
                    phone = phone,
                    address = address,
                    birthDate = birthDate,
                    email = email
                )
                authViewModel.signupWithUser(user, password)
            },
            enabled = authState.value != AuthState.Loading,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
                )
        ) {
            Text(text = "Create account")
        }


        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Already have an account? Login",
                color = Color(0xFF03A9F4), // Màu xanh dương
                fontWeight = FontWeight.Medium)

        }

    }
}