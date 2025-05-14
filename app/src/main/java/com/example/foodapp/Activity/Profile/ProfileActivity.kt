package com.example.foodapp.Activity.Profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import coil.compose.rememberAsyncImagePainter
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

    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(user?.name.orEmpty()) }
    var email by remember { mutableStateOf(user?.email.orEmpty()) }
    var phone by remember { mutableStateOf(user?.phone.orEmpty()) }
    var address by remember { mutableStateOf(user?.address.orEmpty()) }
    var birthDate by remember { mutableStateOf(user?.birthDate.orEmpty()) }

    LaunchedEffect(user) {
        user?.let {
            name = it.name
            email = it.email
            phone = it.phone
            address = it.address
            birthDate = it.birthDate
        }
    }
    var pickedUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null && isEditing) {
            pickedUri = uri

        }
    }

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
            val painter = when {
                pickedUri != null -> rememberAsyncImagePainter(pickedUri)
                user?.avatarUrl.isNullOrBlank() -> painterResource(R.drawable.user)
                else -> rememberAsyncImagePainter(user!!.avatarUrl)
            }
            Box {
                Image(
                    painter = painter,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable { if (isEditing) imagePicker.launch("image/*") }
                )
                if (isEditing) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Avatar",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(24.dp)
                            .background(Color.White, CircleShape)
                            .padding(4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Name
            Text(
                text = user?.name ?: "llll",
                color = Color.Black,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,

                )

            // Title
            Text(
                text = "Your Profile",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileTextField("Your Name", name, Icons.Default.AccountCircle, isEditing) { name = it }
            ProfileTextField("Your Email", email, Icons.Default.Email, isEditing) { email = it }
            ProfileTextField("Phone Number", phone, Icons.Default.Phone, isEditing) { phone = it }
            ProfileTextField("Address", address, Icons.Default.Home, isEditing) { address = it }
            ProfileTextField("Birthday", birthDate, Icons.Default.DateRange, isEditing) { birthDate = it }
            ProfileTextField("Password", "********", Icons.Default.Lock, false) {}

            Spacer(Modifier.height(24.dp))

            // Buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Edit/Save button
                Button(
                    onClick = {
                        if (isEditing) {
                            val updated = user!!.copy(
                                name = name,
                                email = email,
                                phone = phone,
                                address = address,
                                birthDate = birthDate
                            )
                            authViewModel.updateUserProfile(updated, pickedUri)
                        }
                        isEditing = !isEditing
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8A56AC),
                        contentColor = Color.White
                    )
                ) {
                    Text(if (isEditing) "Save" else "Edit Profile")
                }

                // Sign out button
                OutlinedButton(
                    onClick = { authViewModel.signout() },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF00C2FF),
                        contentColor = Color.White
                    )
                ) {
                    Text("Sign out")
                }
            }
        }
    }


}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    icon: ImageVector,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(icon, contentDescription = null) }
        )
    }
}
