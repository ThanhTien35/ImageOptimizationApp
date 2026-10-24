# ImageOptimizationApp

## 1. Giới thiệu

**ImageOptimizationApp** là ứng dụng Android được xây dựng bằng **Kotlin và Jetpack Compose**, phục vụ đề tài:

> **Tối ưu tải, lưu trữ đệm và hiển thị hình ảnh trong ứng dụng Android**

Mục tiêu của project là nghiên cứu và thực nghiệm các kỹ thuật tải và hiển thị hình ảnh trong Android, tập trung vào:

- Tải ảnh từ URL.
- Tải ảnh bất đồng bộ.
- Hiển thị trạng thái Loading và Error.
- Hiển thị danh sách lớn bằng `LazyColumn`.
- Tối ưu kích thước ảnh theo vị trí hiển thị.
- Sử dụng Memory Cache và Disk Cache.
- Xây dựng hai chế độ **Baseline** và **Optimized** để phục vụ so sánh hiệu năng.
- Chuẩn bị cho các phép đo thời gian tải, bộ nhớ, CPU và độ mượt giao diện.

Project được phát triển theo từng giai đoạn để dễ kiểm thử, so sánh và thu thập minh chứng.

---

# 2. Công nghệ sử dụng

- **Language:** Kotlin
- **Platform:** Android
- **UI Framework:** Jetpack Compose
- **Image Loading Library:** Coil 3.3.0
- **Network support:** Coil Network OkHttp
- **List:** LazyColumn
- **IDE:** Android Studio
- **Version Control:** Git + GitHub
- **Testing Device:** Pixel 5 Emulator – API 30
- **Minimum SDK:** 24
- **Target SDK:** 37
- **Compile SDK:** 37
- **Java Compatibility:** Java 11

---

# 3. Cấu trúc project

Các file chính hiện tại:

```text
ImageOptimizationApp
│
├── app
│   └── src/main
│       ├── AndroidManifest.xml
│       │
│       ├── java/com/example/imageoptimizationapp
│       │   ├── MainActivity.kt
│       │   ├── Product.kt
│       │   ├── ProductListScreen.kt
│       │   ├── ProductDetailScreen.kt
│       │   ├── ImageConfig.kt
│       │   └── ImageRequestFactory.kt
│       │
│       └── res
│           └── drawable
│               ├── placeholder_image
│               └── error_image
│
├── gradle
│   └── libs.versions.toml
│
└── README.md
```

---

# 4. Giai đoạn 1 – Xây dựng ứng dụng cơ bản

## 4.1. Mục tiêu

Giai đoạn 1 tập trung xây dựng cấu trúc cơ bản của ứng dụng:

- Tạo Android Kotlin project.
- Sử dụng Jetpack Compose.
- Xây dựng `Product` model.
- Sinh danh sách 100 sản phẩm.
- Mỗi sản phẩm có URL hình ảnh riêng.
- Xây dựng Product List.
- Xây dựng Product Detail.
- Cho phép chuyển từ danh sách sang màn hình chi tiết.

---

## 4.2. Product Model

Mỗi sản phẩm gồm:

```kotlin
data class Product(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val description: String
)
```

Các thuộc tính:

| Thuộc tính | Ý nghĩa |
|---|---|
| `id` | ID của sản phẩm |
| `name` | Tên sản phẩm |
| `imageUrl` | URL hình ảnh |
| `description` | Mô tả sản phẩm |

---

## 4.3. Tạo 100 sản phẩm

Ứng dụng tạo tự động 100 sản phẩm bằng:

```kotlin
return List(100) { index ->
    Product(
        id = index + 1,
        name = "Product ${index + 1}",
        imageUrl = "https://picsum.photos/seed/${index + 1}/800/600",
        description = "Description for Product ${index + 1}"
    )
}
```

Mỗi sản phẩm sử dụng một `seed` khác nhau để nhận ảnh khác nhau.

Ví dụ:

```text
Product 1  → /seed/1/800/600
Product 2  → /seed/2/800/600
Product 3  → /seed/3/800/600
...
Product 100 → /seed/100/800/600
```

---

## 4.4. Product List

Danh sách sản phẩm được xây dựng bằng:

```text
LazyColumn
```

`LazyColumn` chỉ compose các item cần thiết trong vùng hiển thị và phù hợp với danh sách có nhiều phần tử.

Mỗi item hiển thị:

```text
[Image]  Product Name
         Description
```

Ảnh trên Product List được hiển thị ở kích thước:

```kotlin
Modifier.size(100.dp)
```

---

## 4.5. Product Detail

Khi người dùng nhấn vào một Product:

```text
Product List
      ↓
Click Product
      ↓
Product Detail
```

Màn hình Detail hiển thị:

- Nút Back.
- Hình ảnh lớn.
- Product Name.
- Product Description.

Ảnh Detail được hiển thị với:

```kotlin
Modifier
    .fillMaxWidth()
    .height(300.dp)
```

---

## 4.6. Quản lý Product được chọn

`MainActivity` sử dụng Compose State để xác định màn hình cần hiển thị.

```kotlin
var selectedProduct by remember {
    mutableStateOf<Product?>(null)
}
```

Nếu:

```text
selectedProduct == null
```

ứng dụng hiển thị:

```text
ProductListScreen
```

Nếu có Product được chọn:

```text
ProductDetailScreen
```

---

## 4.7. Kết quả Giai đoạn 1

Đã hoàn thành:

- Android Kotlin Project.
- Jetpack Compose.
- Product Model.
- 100 Products.
- 100 URL hình ảnh.
- LazyColumn.
- Product List Screen.
- Product Detail Screen.
- Điều hướng List → Detail → Back.

---

# 5. Giai đoạn 2 – Coil, Placeholder và Async Image Loading

## 5.1. Mục tiêu

Giai đoạn 2 tập trung vào việc tải hình ảnh từ Internet một cách bất đồng bộ.

Các nội dung triển khai:

- Coil 3.
- Coil Compose.
- Coil Network OkHttp.
- `AsyncImage`.
- `ImageRequest`.
- Loading Placeholder.
- Error Placeholder.
- Kiểm tra URL lỗi.
- Kiểm tra mạng chậm.
- Kiểm tra cuộn danh sách trong khi ảnh đang tải.

---

## 5.2. Coil dependencies

Project sử dụng:

```kotlin
implementation(libs.coil.compose)
implementation(libs.coil.network.okhttp)
```

Phiên bản Coil:

```text
Coil 3.3.0
```

`coil-compose` được sử dụng để tích hợp Coil với Jetpack Compose.

`coil-network-okhttp` cung cấp khả năng tải hình ảnh qua HTTP/HTTPS.

---

## 5.3. Internet Permission

Ứng dụng cần quyền Internet:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Quyền này được khai báo trong:

```text
AndroidManifest.xml
```

---

## 5.4. AsyncImage

Hình ảnh được tải bằng `AsyncImage`.

Cấu trúc ban đầu:

```kotlin
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(product.imageUrl)
        .build(),
    contentDescription = product.name,
    placeholder = painterResource(R.drawable.placeholder_image),
    error = painterResource(R.drawable.error_image),
    modifier = Modifier.size(100.dp),
    contentScale = ContentScale.Crop
)
```

Coil thực hiện quá trình tải ảnh mà không yêu cầu thực hiện công việc mạng nặng trực tiếp trên Main Thread.

---

## 5.5. Loading Placeholder

Trong thời gian ảnh chưa tải xong, ứng dụng hiển thị:

```kotlin
placeholder = painterResource(
    R.drawable.placeholder_image
)
```

Loading Placeholder được kiểm thử bằng cách:

1. Giảm tốc độ mạng của Android Emulator.
2. Xóa dữ liệu ứng dụng khi cần cho lượt kiểm thử.
3. Khởi động lại ứng dụng.
4. Quan sát placeholder trước khi ảnh thật xuất hiện.

Kết quả:

```text
Loading Placeholder → PASS
```

---

## 5.6. Error Placeholder

Nếu URL không hợp lệ hoặc server không trả được hình ảnh:

```kotlin
error = painterResource(
    R.drawable.error_image
)
```

Trong quá trình kiểm thử, URL của Product 1 được tạm thời thay bằng một URL không hợp lệ.

Kết quả:

```text
Product 1 → Error Image
Product 2+ → Normal Image
```

Sau khi kiểm thử, URL Product 1 được khôi phục về URL bình thường.

Kết quả:

```text
Error Placeholder → PASS
```

---

## 5.7. Async Loading và Fast Scroll

Ứng dụng được kiểm tra bằng cách cuộn nhanh `LazyColumn` trong khi hình ảnh vẫn đang tải.

Mục tiêu:

- UI vẫn phản hồi.
- Không crash.
- Không đóng băng giao diện.
- Các item phía dưới tiếp tục hiển thị placeholder trong khi chờ ảnh.
- Hình ảnh được cập nhật sau khi tải hoàn tất.

Kết quả kiểm thử ban đầu:

```text
Async Loading → PASS
Fast Scroll during loading → PASS
```

Việc đo jank/frame rendering chính thức sẽ được thực hiện trong giai đoạn kiểm thử hiệu năng.

---

# 6. Vấn đề thực tế – HTTP 503 từ Picsum

Trong quá trình phát triển, dịch vụ hình ảnh Picsum từng trả về:

```text
Error 503
Maximum threads for service reached
```

Lỗi được xác nhận bằng cách truy cập trực tiếp URL ảnh trong Chrome của Android Emulator.

Điều này cho thấy lỗi tại thời điểm đó đến từ image server chứ không phải Android UI hoặc Coil configuration.

Khi server trả lỗi:

```text
Picsum
   ↓
HTTP 503
   ↓
Coil request fails
   ↓
error_image
```

Khi server hoạt động trở lại, hình ảnh được tải bình thường.

Vấn đề này cũng cho thấy khi thực hiện benchmark cần kiểm soát điều kiện mạng và trạng thái server để hạn chế sai lệch kết quả.

---

# 7. Giai đoạn 3 – Baseline và Optimized Image Loading

## 7.1. Mục tiêu

Giai đoạn 3 xây dựng hai chế độ:

```text
BASELINE
```

và:

```text
OPTIMIZED
```

Mục tiêu là chuẩn bị hai cấu hình tải ảnh để thực hiện so sánh hiệu năng trong giai đoạn tiếp theo.

---

# 8. ImageMode

Hai chế độ được định nghĩa trong:

```text
ImageConfig.kt
```

```kotlin
enum class ImageMode {
    BASELINE,
    OPTIMIZED
}
```

Chế độ hiện tại được chọn bằng:

```kotlin
object ImageConfig {
    var currentMode = ImageMode.BASELINE
}
```

Có thể chuyển sang Optimized bằng:

```kotlin
var currentMode = ImageMode.OPTIMIZED
```

---

# 9. ImageRequestFactory

Logic tạo `ImageRequest` được tách khỏi UI và đưa vào:

```text
ImageRequestFactory.kt
```

Mục đích:

- Tránh lặp code.
- Tách UI khỏi cấu hình tải ảnh.
- Dễ chuyển Baseline/Optimized.
- Dễ thay đổi chính sách cache.
- Dễ thay đổi kích thước request.
- Thuận tiện cho benchmark.

Luồng xử lý:

```text
ProductListScreen / ProductDetailScreen
                 ↓
        createImageRequest()
                 ↓
        ImageConfig.currentMode
                 ↓
       ┌─────────┴──────────┐
       ↓                    ↓
   BASELINE             OPTIMIZED
       ↓                    ↓
ImageRequest         Optimized Request
```

---

# 10. Baseline Mode

Baseline sử dụng request cơ bản:

```kotlin
fun createBaselineRequest(
    context: Context,
    imageUrl: String
): ImageRequest {
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .build()
}
```

Baseline:

- Sử dụng URL ảnh gốc.
- Không chủ động chỉ định kích thước decode bằng `.size()`.
- Không thêm chính sách tối ưu kích thước riêng.
- Giữ hành vi mặc định của Coil đối với các chính sách không được cấu hình rõ.

Baseline đại diện cho phiên bản **trước khi áp dụng các tối ưu chủ động của project**.

> Lưu ý: Baseline không được mô tả là "không có cache", vì request hiện tại không tắt cache mặc định của Coil.

---

# 11. Optimized Mode

Optimized Request:

```kotlin
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
```

Optimized Mode áp dụng:

- Resize request.
- Memory Cache được cấu hình `ENABLED`.
- Disk Cache được cấu hình `ENABLED`.
- Thumbnail strategy cho Product List.
- Kích thước lớn hơn cho Detail.

---

# 12. Resize Strategy

Một trong các vấn đề cần tránh khi hiển thị ảnh là decode ảnh lớn hơn nhiều so với kích thước thực tế cần hiển thị.

Nguồn ảnh hiện tại:

```text
800 × 600
```

Trong khi Product List chỉ hiển thị thumbnail nhỏ.

Do đó Optimized Mode yêu cầu kích thước phù hợp hơn.

---

## 12.1. Product List

UI:

```kotlin
Modifier.size(100.dp)
```

Optimized request:

```kotlin
createImageRequest(
    context = LocalContext.current,
    imageUrl = product.imageUrl,
    width = 200,
    height = 200
)
```

Khi chạy `OPTIMIZED`, request sử dụng:

```kotlin
.size(200, 200)
```

Mục tiêu là tránh decode ảnh nguồn lớn không cần thiết cho thumbnail trong danh sách.

---

## 12.2. Product Detail

Detail cần hình ảnh lớn hơn.

Request:

```kotlin
createImageRequest(
    context = LocalContext.current,
    imageUrl = product.imageUrl,
    width = 800,
    height = 600
)
```

Do đó:

```text
Product List → 200 × 200
Product Detail → 800 × 600
```

---

# 13. Memory Cache

Optimized Mode cấu hình:

```kotlin
.memoryCachePolicy(CachePolicy.ENABLED)
```

Mục tiêu:

- Cho phép sử dụng ảnh đã được xử lý trong bộ nhớ.
- Hạn chế công việc tải/decode lặp lại trong các tình huống phù hợp.
- Chuẩn bị cho kiểm thử Cache Hit.

---

# 14. Disk Cache

Optimized Mode cấu hình:

```kotlin
.diskCachePolicy(CachePolicy.ENABLED)
```

Mục tiêu:

- Cho phép sử dụng disk cache của Coil.
- Giảm nhu cầu tải lại dữ liệu ảnh trong các tình huống phù hợp.
- Hỗ trợ kiểm thử reload/cache trong giai đoạn tiếp theo.

---

# 15. Chuyển đổi Baseline và Optimized

`createImageRequest()` kiểm tra mode hiện tại:

```kotlin
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
```

Nhờ đó UI không cần biết chi tiết cấu hình bên trong.

---

# 16. Hiển thị Mode trên giao diện

Để tránh nhầm lẫn trong quá trình benchmark, Product List hiển thị mode hiện tại:

```kotlin
Text(
    text = "Image Mode: ${ImageConfig.currentMode}"
)
```

Khi chạy Baseline:

```text
Image Mode: BASELINE
```

Khi chạy Optimized:

```text
Image Mode: OPTIMIZED
```

Điều này giúp screenshot và dữ liệu benchmark có thể được phân biệt rõ ràng.

---

# 17. So sánh thiết kế Baseline và Optimized

| Thành phần | Baseline | Optimized |
|---|---|---|
| Remote Image | Có | Có |
| Coil | Có | Có |
| AsyncImage | Có | Có |
| Placeholder | Có | Có |
| Error Image | Có | Có |
| LazyColumn | Có | Có |
| Explicit Resize | Không | Có |
| List Request Size | Không chỉ định | 200 × 200 |
| Detail Request Size | Không chỉ định | 800 × 600 |
| Explicit Memory Cache Policy | Không | ENABLED |
| Explicit Disk Cache Policy | Không | ENABLED |
| Mode Indicator | BASELINE | OPTIMIZED |

---

# 18. Quy trình reset phục vụ benchmark

Khi cần thực hiện một lượt kiểm thử First Load với trạng thái ứng dụng sạch, có thể sử dụng:

```powershell
adb shell pm clear com.example.imageoptimizationapp
```

Trên môi trường phát triển hiện tại:

```powershell
& "C:\Users\THANH TIEN\AppData\Local\Android\Sdk\platform-tools\adb.exe" shell pm clear com.example.imageoptimizationapp
```

Kết quả mong đợi:

```text
Success
```

Không sử dụng thao tác clear giữa hai lần tải khi đang kiểm tra Cache Hit, vì cache cần được giữ lại để đánh giá hành vi tải lại.

---

# 19. Trạng thái sau 3 giai đoạn

## Giai đoạn 1

```text
Android Kotlin Project       DONE
Jetpack Compose              DONE
Product Model                DONE
100 Products                 DONE
100 Image URLs               DONE
LazyColumn                   DONE
Product List                 DONE
Product Detail               DONE
List → Detail                DONE
```

## Giai đoạn 2

```text
Coil 3                       DONE
Network Image Loading        DONE
AsyncImage                   DONE
ImageRequest                 DONE
Loading Placeholder          DONE
Error Placeholder            DONE
Slow Network Test            DONE
Invalid URL Test             DONE
Fast Scroll Test             DONE
```

## Giai đoạn 3

```text
ImageMode                    DONE
ImageConfig                  DONE
ImageRequestFactory          DONE
Baseline Mode                DONE
Optimized Mode               DONE
Memory Cache Configuration   DONE
Disk Cache Configuration     DONE
List Resize                  DONE
Detail Resize                DONE
Thumbnail Strategy           DONE
Mode Indicator               DONE
```

---

# 20. Minh chứng đã thu thập

## Giai đoạn 1

```text
Stage1_01_Project_Config.png
Stage1_02_100_Products_URL.png
Stage1_03_Product_List.png
Stage1_04_Product_List_Scroll.png
Stage1_05_Product_Detail.png
```

## Giai đoạn 2

```text
Stage2_01_Loading_Placeholder.png
Stage2_02_Error_Placeholder.png
Stage2_03_Coil_Dependencies.png
Stage2_04_AsyncImage_Code.png
Stage2_05_Detail_AsyncImage_Code.png
```

## Giai đoạn 3

```text
Stage3_01_Baseline.png
Stage3_02_Optimized.png
Stage3_03_ImageConfig.png
Stage3_04_Request_Comparison.png
```

---

# 21. Giai đoạn tiếp theo – Performance Testing

Giai đoạn 4 sẽ thực hiện các test case:

| Test Case | Nội dung |
|---|---|
| TC01 | First Load |
| TC02 | Cache Hit |
| TC03 | Fast Scroll |
| TC04 | High-resolution Image |
| TC05 | Offline |
| TC06 | Bad URL / Slow Server |
| TC07 | Scale Test: 100 → 500 → 1000 images |
| TC08 | Before vs After Optimization |

Các chỉ số dự kiến thu thập:

- Image loading time.
- Reload behavior.
- Memory usage.
- CPU usage.
- Frame rendering / jank.
- Scrolling smoothness.
- Cache behavior.
- Baseline vs Optimized comparison.

Công cụ chính:

```text
Android Studio Profiler
```

Môi trường benchmark sẽ được giữ nhất quán:

```text
Device: Pixel 5 Emulator
API: 30
Same image source
Same network conditions
Same application version
Same test procedure
```

---

# 22. Roadmap

```text
Stage 1
Basic Android Application
Product List + Detail + 100 URLs
            │
            ▼
Stage 2
Coil + AsyncImage
Loading/Error Placeholder
            │
            ▼
Stage 3
Baseline vs Optimized
Cache + Resize + Thumbnail
            │
            ▼
Stage 4
TC01 → TC08
Profiler + Performance Data
            │
            ▼
Stage 5
Report + Charts + Evidence
APK + Demo Video + README
```

---

# 23. Current Status

**Stage 1:** Completed  
**Stage 2:** Completed  
**Stage 3:** Completed  
**Stage 4:** Not started  
**Stage 5:** Not started
