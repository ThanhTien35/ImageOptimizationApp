package com.example.imageoptimizationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.imageoptimizationapp.ui.theme.ImageOptimizationAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ImageOptimizationAppTheme {

                val products = remember {
                    getProducts()
                }

                var selectedProduct by remember {
                    mutableStateOf<Product?>(null)
                }

                if (selectedProduct == null) {

                    ProductListScreen(
                        products = products,
                        onProductClick = { product ->
                            selectedProduct = product
                        }
                    )

                } else {

                    ProductDetailScreen(
                        product = selectedProduct!!,
                        onBack = {
                            selectedProduct = null
                        }
                    )
                }
            }
        }
    }
}