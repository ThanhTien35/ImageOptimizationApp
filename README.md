# ImageOptimizationApp

Android application for studying and comparing image loading, caching, and display optimization techniques using **Kotlin, Jetpack Compose, and Coil**.

The project provides two configurations:

- **Baseline:** basic image loading configuration.
- **Optimized:** image loading with explicit image sizing, Memory Cache, and Disk Cache.

Both configurations use the same UI, functionality, and dataset for performance comparison.

---

## Features

- Product List using `LazyColumn`
- Product Detail screen
- Asynchronous image loading with Coil
- Loading placeholder
- Error placeholder
- Memory Cache and Disk Cache
- Baseline / Optimized comparison
- Dataset testing with 100, 500, and 1000 products
- Performance monitoring using Android Studio Profiler

---

## Tech Stack

- Kotlin
- Jetpack Compose
- Coil 3
- Android Studio
- Android Studio Profiler
- Minimum SDK: 24
- Target / Compile SDK: 37

The experiments in this project were mainly performed using a **Pixel 5 emulator with Android API 30**.

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/ThanhTien35/ImageOptimizationApp.git
cd ImageOptimizationApp
```

Alternatively, use **Get from VCS** in Android Studio and enter the repository URL.

### 2. Open the project

1. Open `ImageOptimizationApp` in Android Studio.
2. Wait for Gradle Sync to complete.
3. Allow Android Studio to download missing dependencies if required.
4. Select an Android emulator or physical device.

For reproducing the experiments, a **Pixel 5 – API 30** emulator is recommended.

### 3. Run the application

1. Select the `app` configuration.
2. Select the Android device.
3. Click **Run**.
4. Wait for the application to build and install.

An Internet connection is required to load images that are not already available in cache.

---

## Using the Application

The main screen displays a scrollable Product List.

- Scroll vertically to browse products.
- Select a product to open the Product Detail screen.
- A loading placeholder is displayed while an image is loading.
- An error placeholder is displayed if the image request fails.
- Use the Back button to return to the Product List.

---

## Baseline and Optimized Modes

The project keeps both configurations in the same source code.

Open:

```text
app/src/main/java/com/example/imageoptimizationapp/ImageConfig.kt
```

Select:

```kotlin
ImageMode.BASELINE
```

or:

```kotlin
ImageMode.OPTIMIZED
```

Then rebuild and run the application.

The Optimized configuration uses:

```kotlin
ImageRequest.Builder(context)
    .data(imageUrl)
    .size(width, height)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .diskCachePolicy(CachePolicy.ENABLED)
    .build()
```

When comparing Baseline and Optimized, use the same device, dataset, and testing procedure.

---

## Changing Dataset Size

Open:

```text
app/src/main/java/com/example/imageoptimizationapp/Product.kt
```

Change:

```kotlin
List(size = 100)
```

to the required dataset size:

```text
100
500
1000
```

Rebuild the application after changing the dataset.

These dataset sizes are used for performance and Dataset Scaling testing.

---

## Performance Testing

The project includes the following test cases:

| Test Case | Description |
|---|---|
| TC01 | First Load |
| TC02 | Cache Hit |
| TC03 | Fast Scroll |
| TC04 | High Resolution |
| TC05 | Offline |
| TC06 | Bad URL / Slow Server |
| TC07 | Dataset Scaling |
| TC08 | Baseline vs Optimized Comparison |

To reproduce a test:

1. Select Baseline or Optimized mode.
2. Select the required dataset size.
3. Run the application on the same device or emulator.
4. Open **Android Studio Profiler**.
5. Perform the required test.
6. Record the result.
7. Switch to the other mode and repeat under similar conditions.

Profiler values may vary depending on cache state, garbage collection, network conditions, and measurement time.

---

## Project Documentation

The repository separates the final documentation into three main parts:

```text
ImageOptimizationApp/
├── app/            # Application source code
├── evidence/       # Development and testing evidence
├── PROGRESS.md     # Development progress
└── README.md       # Setup and usage instructions
```

### Evidence

The `evidence/` directory contains screenshots and results collected during development and testing.

```text
evidence/
├── stage 1/
├── stage 2/
├── stage 3/
├── stage 4/        # TC01 – TC07
└── stage 5/
    ├── APK/
    ├── Final_Configuration/
    ├── Report/
    └── TC08_Before_After_Comparison/
```

### Development Progress

See [`PROGRESS.md`](PROGRESS.md) for the development process and completed work for each stage.

### APK

The final APK is available at:

```text
evidence/stage 5/APK/ImageOptimizationApp.apk
```

It can be installed directly on a compatible Android device for application verification.

---

## Demo Video

A complete project demonstration will be available on YouTube.

**YouTube Demo:** `To be updated`

The final link will be added after the demo video is completed.

---

## Repository

**ImageOptimizationApp**  
GitHub: `ThanhTien35/ImageOptimizationApp`
