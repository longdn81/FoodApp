package com.example.foodapp.Activity.Search

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import com.example.foodapp.Activity.BaseActivity
import com.example.foodapp.Activity.Detail.DetailActivity
import com.example.foodapp.Model.ItemsModel
import com.example.foodapp.R
import com.example.foodapp.ViewModel.MainViewModel


class SearchActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var query: String
    private lateinit var searchResults: LiveData<List<ItemsModel>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val query = intent.getStringExtra("query") ?: ""
        val title = intent.getStringExtra("title") ?: "Search Result"

        searchResults = viewModel.searchItems(query)

        setContent {
            SearchScreen(results = searchResults ,onBackClick = {finish()} ,title = title)

        }
    }

}


@Composable
fun SearchScreen(results: LiveData<List<ItemsModel>>, onBackClick: () -> Unit, title: String) {
    val items by results.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Tiêu đề và nút back
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            val (backBtn, searchTxt) = createRefs()

            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(searchTxt) {
                        centerTo(parent)
                    }
            )

            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .clickable { onBackClick() }
            )
        }

        // Danh sách sản phẩm dạng grid với scroll
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                ProductCard(item)
            }
        }
    }
}



@Composable
fun ProductCard(item: ItemsModel) {
    val context = LocalContext.current

    Column(modifier = Modifier
            .padding(4.dp)
            .wrapContentHeight()
        ) {
            AsyncImage(
                model = item.picUrl.firstOrNull(),
                contentDescription = null,
                modifier = Modifier
                    .width(180.dp)
                    .background(colorResource(R.color.LightGrey), shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .height(180.dp)
                    .clickable {
                        val intent = Intent(context, DetailActivity::class.java).apply {
                        putExtra("object", item)
                        }
                        context.startActivity(intent)
                    },
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.title,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
            Row(
                modifier = Modifier
                    .width(175.dp)
                    .padding(top = 4.dp)
            ) {
                Row {
                    Image(painter = painterResource(R.drawable.star), contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer( modifier = Modifier.width(8.dp))
                    Text(item.rating.toString(), color = Color.Black , fontSize = 15.sp)
                }
                Text(text = "${item.price}K VND",
                    color = colorResource(R.color.darkBrown),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp ,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }






