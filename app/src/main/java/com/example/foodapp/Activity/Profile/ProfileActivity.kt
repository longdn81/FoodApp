package com.example.foodapp.Activity.Profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.foodapp.Activity.Authentication.LoginActivity
import com.example.foodapp.Activity.BaseActivity
import com.example.foodapp.Activity.Dashboard.MainActivity
import com.example.foodapp.Activity.Splash.SplashActivity
import com.example.foodapp.R
import com.example.foodapp.ViewModel.AuthState
import com.example.foodapp.ViewModel.AuthViewModel

class ProfileActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        authViewModel.loadUserProfile()
        setContent {
            PofileScreen(
                modifier = Modifier,
                authViewModel = authViewModel,
                onBackClick = { finish()  },
            )
        }
    }
}

@Composable
fun PofileScreen(modifier: Modifier = Modifier, authViewModel: AuthViewModel, onBackClick: () -> Unit,) {

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val user = (authState.value as? AuthState.Authenticated)?.user

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated ->{
                val intent = Intent(context, SplashActivity::class.java)
                context.startActivity(intent)

                // Nếu bạn muốn đóng Activity hiện tại:
                if (context is Activity) {
                    context.finish()
                }
            }
            else -> Unit
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            modifier = Modifier.padding(top = 36.dp, start = 16.dp, end = 16.dp)
        ) {
            val (backBtn, cartTxt) = createRefs()
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(cartTxt) { centerTo(parent) },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                text = "Profile "
            )
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onBackClick()
                    }
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Avatar
            Image(
                painter = painterResource(R.drawable.user), // dùng ảnh mặc định
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Name
            Text(
                text = user?.name ?: "llll",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A56AC)
            )

            // Title
            Text(
                text = "Your Profile",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileTextField(label = "Your Name", value = user?.name ?: "", icon = Icons.Default.AccountCircle)
            ProfileTextField(label = "Your Email", value = user?.email ?: "", icon = Icons.Default.Email)
            ProfileTextField(label = "Phone Number", value = user?.phone ?: "", icon = Icons.Default.Phone)
            ProfileTextField(label = "Address", value = user?.address ?: "", icon = Icons.Default.Home)
            ProfileTextField(label = "Birthday", value = user?.birthDate ?: "", icon = Icons.Default.DateRange)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { authViewModel.signout() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A56AC))
            ) {
                Text(text = "Sign out", color = Color(0xFF03A9F4), fontWeight = FontWeight.Medium)
            }
        }
    }


}

@Composable
fun ProfileTextField(label: String, value: String, icon: ImageVector) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            leadingIcon = { Icon(icon, contentDescription = null) },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledBorderColor = Color.LightGray,
                disabledLeadingIconColor = Color.Gray,
                disabledLabelColor = Color.Gray
            )
        )
    }
}
