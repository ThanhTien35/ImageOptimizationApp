package com.example.imageoptimizationapp

enum class ImageMode {
    BASELINE,
    OPTIMIZED
}

object ImageConfig {
    var currentMode = ImageMode.OPTIMIZED
}