package com.example.imageoptimizationapp

import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun ProductListScreen(
    products: List<Product>,
    onProductClick: (Product) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        items(products) { product ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
                    .clickable {
                        onProductClick(product)
                    }
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        placeholder = painterResource(R.drawable.placeholder_image),
                        error = painterResource(R.drawable.error_image),
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = product.name,
                            fontSize = 20.sp
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = product.description,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}