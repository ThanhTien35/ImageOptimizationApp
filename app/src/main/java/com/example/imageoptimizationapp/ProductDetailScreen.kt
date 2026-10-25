package com.example.imageoptimizationapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import androidx.compose.foundation.layout.safeDrawingPadding

@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {

        Button(
            onClick = onBack
        ) {
            Text("Back")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        AsyncImage(
            model = createImageRequest(
                context = LocalContext.current,
                imageUrl = product.imageUrl,
                width = 800,
                height = 600
            ),
            contentDescription = product.name,
            placeholder = painterResource(R.drawable.placeholder_image),
            error = painterResource(R.drawable.error_image),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = product.name,
            fontSize = 26.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = product.description,
            fontSize = 16.sp
        )
    }
}