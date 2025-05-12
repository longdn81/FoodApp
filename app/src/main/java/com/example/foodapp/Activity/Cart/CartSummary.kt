package com.example.foodapp.Activity.Cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.R

@Composable
fun CartSummary(itemTotal: Double, tax: Double, delivery: Double) {
    val total = itemTotal + tax + delivery

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Item Total:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
            Text(
                text = "$$itemTotal",
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Tax:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
            Text(
                text = "$$tax",
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Delivery:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
            Text(
                text = "$$delivery",
                fontWeight = FontWeight.Bold
            )
        }
        Box(modifier = Modifier
            .padding(top = 16.dp)
            .height(1.dp)
            .fillMaxWidth()
            .background(colorResource(R.color.grey))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Total:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
            Text(
                text = "$$total",
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.green)
            ),
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Proceed to Checkout",
                fontSize = 18.sp,
                color = Color.White
            )
        }

    }
}
