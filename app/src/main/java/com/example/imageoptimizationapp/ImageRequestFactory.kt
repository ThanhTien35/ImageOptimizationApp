package com.example.imageoptimizationapp

import android.content.Context
import coil3.request.CachePolicy
import coil3.request.ImageRequest

fun createBaselineRequest(
    context: Context,
    imageUrl: String
): ImageRequest {
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .build()
}

fun createOptimizedRequest(
    context: Context,
    imageUrl: String,
    width: Int,
    height: Int
): ImageRequest {
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .size(width, height)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build()
}

fun createImageRequest(
    context: Context,
    imageUrl: String,
    width: Int,
    height: Int
): ImageRequest {
    return when (ImageConfig.currentMode) {
        ImageMode.BASELINE -> {
            createBaselineRequest(
                context = context,
                imageUrl = imageUrl
            )
        }

        ImageMode.OPTIMIZED -> {
            createOptimizedRequest(
                context = context,
                imageUrl = imageUrl,
                width = width,
                height = height
            )
        }
    }
}