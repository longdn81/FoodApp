package com.example.foodapp.Activity.Authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.foodapp.Activity.BaseActivity
import com.example.foodapp.Activity.Dashboard.MainActivity

import com.example.foodapp.Activity.Splash.SplashActivity
import com.example.foodapp.ViewModel.AuthState
import com.example.foodapp.ViewModel.AuthViewModel

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            LoginScreen(
                modifier = Modifier,
                authViewModel = AuthViewModel()
            )
        }
    }


}

@Composable
fun LoginScreen(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {


    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated ->{
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)

                // Nếu bạn muốn đóng Activity hiện tại:
                if (context is Activity) {
                    context.finish()
                }
            }
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login Page", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "Email")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Password")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.login(email,password)
        },
            enabled = authState.value != AuthState.Loading
        ) {
            Text(text = "Login")
        }


        Spacer(modifier = Modifier.height(8.dp))


        TextButton(onClick = {
            val intent = Intent(context, SignupActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Don't have an account? Sign up")
        }

    }

}