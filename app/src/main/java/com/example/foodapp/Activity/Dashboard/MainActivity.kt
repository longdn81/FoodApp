package com.example.foodapp.Activity.Dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.foodapp.Activity.BaseActivity
import com.example.foodapp.Activity.Cart.CartActivity
import com.example.foodapp.Activity.Profile.ProfileActivity
import com.example.foodapp.Activity.Search.SearchActivity
import com.example.foodapp.Model.CategoryModel
import com.example.foodapp.Model.ItemsModel
import com.example.foodapp.Model.SliderModel
import com.example.foodapp.R
import com.example.foodapp.ViewModel.AuthState
import com.example.foodapp.ViewModel.AuthViewModel
import com.example.foodapp.ViewModel.MainViewModel

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel: AuthViewModel by viewModels()
        authViewModel.loadUserProfile()
        setContent {
            DashboardScreen(authViewModel = authViewModel,
                onNavigate = { menu ->
                    when (menu) {
                        "Cart"    -> startActivity(Intent(this, CartActivity::class.java))
                        "Profile" -> startActivity(Intent(this, ProfileActivity::class.java))
                        "Explorer" -> startActivity(Intent(this, SearchActivity::class.java).apply {
                            putExtra("query", "")
                            putExtra("title", "Explorer")
                        })

                    }
                },
                onSearch = { query ->
                    startActivity(Intent(this, SearchActivity::class.java).apply {
                        putExtra("query", query)
                    })
                })

            }
        }
    }



@Composable

fun DashboardScreen(authViewModel: AuthViewModel, onNavigate: (String) -> Unit , onSearch: (String) -> Unit ) {
    val viewModel = MainViewModel()

    val authState = authViewModel.authState.observeAsState()
    val user = (authState.value as? AuthState.Authenticated)?.user

//    search
    var searchQuery by remember { mutableStateOf("") }

//    load ui
    val banners = remember { mutableStateListOf<SliderModel>() }
    val categories = remember { mutableStateListOf<CategoryModel>() }
    val bestseller = remember { mutableStateListOf<ItemsModel>() }


    var showBannerLoading by remember { mutableStateOf(value = true) }
    var showCategoryLoading by remember { mutableStateOf(value = true) }
    var showBestSellerLoading by remember { mutableStateOf(value = true) }

    // banner
    LaunchedEffect(Unit) {
        viewModel.loadBanner().observeForever {
            banners.clear()
            banners.addAll(it)
            showBannerLoading = false
        }
    }

    // categories
    LaunchedEffect(Unit) {
        viewModel.loadCategory().observeForever {
            categories.clear()
            categories.addAll(it)
            showCategoryLoading = false
        }
    }
    // best seller
    LaunchedEffect(Unit) {
        viewModel.loadBestSeller().observeForever {
            bestseller.clear()
            bestseller.addAll(it)
            showBestSellerLoading = false
        }
    }

    ConstraintLayout(modifier = Modifier.background(Color.White)) {
        val (scrollList, bottomMenu) = createRefs()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .padding(bottom = 70.dp)

        ) {
            item{
                Column(modifier = Modifier.padding(16.dp)) {
                    // 1. Greeting
                    Row(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column() {
                            Text("Welcome back,", color = Color.Black, fontSize = 15.sp,)
                            Text(
                                text = user?.name ?: "User",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(onClick = { onNavigate("Profile") }) {
                            Image(
                                painter = painterResource(R.drawable.bell_icon),
                                contentDescription = "Notifications",
                                modifier = Modifier.size(47.dp) // chỉnh theo nhu cầu
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // 2. Search bar
                    Row(
                        modifier = Modifier
                            .height(56.dp)   // 56.dp
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color.White)
                            .padding(2.dp),         // Khoảng cách giữa TextField và nút
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Text input
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search...", color = Color.Gray) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 28.dp,
                                        bottomStart = 28.dp,
                                        topEnd = 0.dp,
                                        bottomEnd = 0.dp
                                    )
                                )
                                .background(Color.White),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor   = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor      = Color.Transparent,
                                unfocusedBorderColor    = Color.Transparent,
                                disabledBorderColor     = Color.Transparent
                            ),

                        )

                        // Search button
                        Box(
                            modifier = Modifier
                                .size(56.dp)   // 56.dp
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 0.dp,
                                        bottomStart = 0.dp,
                                        topEnd = 28.dp,
                                        bottomEnd = 28.dp
                                    )
                                )
                                .background(Color(0xFF00C2FF))
                                .clickable { onSearch(searchQuery.trim()) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
//            load banner
            item {
                if (showBannerLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }else{
                   Banners(banners)
                }
            }
            item {
                Text(
                    text = "Categories",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(horizontal = 16.dp)
                )
            }

            item {
                if (showCategoryLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    CategoryList(categories = categories)
                }
            }
            item{
                Row (modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        text = "Best Seller Product",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,

                    )
                    Text(
                        text = "See All",
                        color = colorResource(R.color.midBrown),

                        )
                }
            }
            item {
                if (showBestSellerLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    ListItems(bestseller)
                }
            }
        }
        BottomMenu(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomMenu) {
                    bottom.linkTo(parent.bottom)
                },
            onItemClick = { menu ->
                when (menu) {
                    "Cart" -> onNavigate("Cart")
                    "Profile" -> onNavigate("Profile")
                    "Explorer" -> onNavigate("Explorer")

                }
            }
        )
    }
}

