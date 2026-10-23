# ImageOptimizationApp

Ứng dụng Android được xây dựng nhằm nghiên cứu quá trình **tải, hiển thị và tối ưu hình ảnh** trong ứng dụng Android.

Project sử dụng **Kotlin**, **Jetpack Compose** và thư viện **Coil 3** để tải hình ảnh từ URL. Phiên bản hiện tại là **Giai đoạn 1**, tập trung xây dựng các chức năng cơ bản trước khi tiến hành đo hiệu năng và tối ưu ở các giai đoạn tiếp theo.

---

## 1. Thông tin project

- **Tên project:** ImageOptimizationApp
- **Ngôn ngữ:** Kotlin
- **Giao diện:** Jetpack Compose
- **Thư viện tải ảnh:** Coil 3
- **Minimum SDK:** API 24
- **Target SDK:** API 37
- **Thiết bị kiểm thử:** Pixel 5 Emulator
- **Android kiểm thử:** Android 11 - API 30
- **IDE:** Android Studio

---

## 2. Mục tiêu Giai đoạn 1

Giai đoạn 1 tập trung xây dựng phiên bản cơ bản của ứng dụng để làm nền tảng cho quá trình tối ưu hình ảnh sau này.

Các chức năng đã thực hiện:

- Tạo danh sách 100 sản phẩm.
- Mỗi sản phẩm sử dụng một hình ảnh lấy từ URL.
- Hiển thị danh sách bằng `LazyColumn`.
- Tải ảnh bất đồng bộ bằng Coil.
- Hiển thị ảnh Placeholder trong quá trình tải.
- Hiển thị Error Image khi URL ảnh bị lỗi.
- Hiển thị tên và mô tả sản phẩm.
- Cho phép cuộn danh sách sản phẩm.
- Cho phép chọn một sản phẩm.
- Hiển thị màn hình chi tiết sản phẩm.
- Hiển thị hình ảnh kích thước lớn trong màn hình chi tiết.
- Cho phép quay lại danh sách sản phẩm.

---

## 3. Các bước thực hiện

### Bước 1 - Cấp quyền truy cập Internet

Ứng dụng cần truy cập Internet để tải hình ảnh từ URL.

Quyền Internet được thêm vào file:

`AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

### Bước 2 - Tích hợp Coil

Project sử dụng **Coil 3** để tải và hiển thị hình ảnh từ Internet trong Jetpack Compose.

Các thư viện được sử dụng:

```text
coil-compose
coil-network-okhttp
```

Trong đó:

- `coil-compose`: hỗ trợ hiển thị hình ảnh trong Jetpack Compose.
- `coil-network-okhttp`: hỗ trợ tải hình ảnh thông qua HTTP/HTTPS.

Hình ảnh được hiển thị bằng `AsyncImage`.

Ví dụ:

```kotlin
AsyncImage(
    model = product.imageUrl,
    contentDescription = product.name
)
```

---

### Bước 3 - Xây dựng Product Model

File `Product.kt` được tạo để định nghĩa cấu trúc của một sản phẩm.

```kotlin
data class Product(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val description: String
)
```

Mỗi sản phẩm gồm:

- `id`: mã sản phẩm.
- `name`: tên sản phẩm.
- `imageUrl`: địa chỉ URL của hình ảnh.
- `description`: mô tả sản phẩm.

Ứng dụng tự động tạo 100 sản phẩm bằng:

```kotlin
List(100)
```

Do đó không cần khai báo thủ công từng sản phẩm.

URL hình ảnh được tạo theo dạng:

```text
https://picsum.photos/seed/{id}/800/600
```

Mỗi sản phẩm sử dụng một giá trị `seed` khác nhau để nhận được hình ảnh khác nhau.

---

### Bước 4 - Xây dựng danh sách sản phẩm

File `ProductListScreen.kt` được sử dụng để hiển thị danh sách sản phẩm.

Danh sách được xây dựng bằng:

```kotlin
LazyColumn
```

Mỗi sản phẩm bao gồm:

```text
Product Card
├── Hình ảnh
├── Tên sản phẩm
└── Mô tả sản phẩm
```

Giao diện cơ bản:

```text
+--------------------------------+
| [ Hình ảnh ]  Product 1        |
|               Description...   |
+--------------------------------+

+--------------------------------+
| [ Hình ảnh ]  Product 2        |
|               Description...   |
+--------------------------------+

               ...

+--------------------------------+
| [ Hình ảnh ]  Product 100      |
|               Description...   |
+--------------------------------+
```

`LazyColumn` được sử dụng để phù hợp với danh sách có nhiều phần tử và hỗ trợ thao tác cuộn.

---

### Bước 5 - Tải hình ảnh từ URL

Coil `AsyncImage` được sử dụng để tải hình ảnh từ URL.

```kotlin
AsyncImage(
    model = product.imageUrl,
    contentDescription = product.name,
    modifier = Modifier.size(100.dp),
    contentScale = ContentScale.Crop
)
```

Trong đó:

- `model`: URL của hình ảnh.
- `contentDescription`: mô tả hình ảnh.
- `size(100.dp)`: kích thước vùng hiển thị ảnh trong danh sách.
- `ContentScale.Crop`: cắt ảnh phù hợp với vùng hiển thị.

Quá trình cơ bản:

```text
Image URL
    |
    v
   Coil
    |
    v
Tải hình ảnh
    |
    v
Hiển thị trên giao diện
```

---

### Bước 6 - Placeholder và Error Image

Ứng dụng sử dụng hai tài nguyên:

```text
placeholder_image.xml
error_image.xml
```

`placeholder_image` được hiển thị trong thời gian hình ảnh đang được tải.

`error_image` được hiển thị khi Coil không thể tải hình ảnh từ URL.

```kotlin
AsyncImage(
    model = product.imageUrl,
    placeholder = painterResource(R.drawable.placeholder_image),
    error = painterResource(R.drawable.error_image)
)
```

Quá trình:

```text
             Image URL
                 |
                 v
               Loading
                 |
        +--------+--------+
        |                 |
     Thành công          Thất bại
        |                 |
        v                 v
    Ảnh thật          Error Image
```

Trong lúc chờ tải, Placeholder được hiển thị.

---

### Bước 7 - Xây dựng màn hình chi tiết

File `ProductDetailScreen.kt` được tạo để hiển thị thông tin chi tiết của sản phẩm.

Màn hình bao gồm:

- Nút Back.
- Hình ảnh sản phẩm kích thước lớn.
- Tên sản phẩm.
- Mô tả sản phẩm.

Giao diện cơ bản:

```text
[ Back ]

+-----------------------------+
|                             |
|       Hình ảnh lớn          |
|                             |
+-----------------------------+

Product 10

Description for Product 10
```

---

### Bước 8 - Xử lý khi chọn sản phẩm

Trong `ProductListScreen.kt`, mỗi `Card` có thể được nhấn bằng `clickable`.

```kotlin
.clickable {
    onProductClick(product)
}
```

Khi người dùng chọn một sản phẩm:

```text
Danh sách sản phẩm
        |
        | Click
        v
Sản phẩm được chọn
        |
        v
Màn hình chi tiết
```

---

### Bước 9 - Quản lý màn hình trong MainActivity

`MainActivity.kt` quản lý sản phẩm đang được chọn bằng State của Jetpack Compose.

```kotlin
var selectedProduct by remember {
    mutableStateOf<Product?>(null)
}
```

Khi:

```text
selectedProduct == null
```

ứng dụng hiển thị:

```text
ProductListScreen
```

Khi người dùng chọn một sản phẩm:

```text
selectedProduct = product
```

ứng dụng chuyển sang:

```text
ProductDetailScreen
```

Khi nhấn Back:

```text
selectedProduct = null
```

ứng dụng quay lại danh sách sản phẩm.

---

## 4. Luồng hoạt động của ứng dụng

```text
Khởi động ứng dụng
        |
        v
Tạo 100 Product
        |
        v
ProductListScreen
        |
        v
LazyColumn
        |
        v
Coil tải ảnh từ URL
        |
        v
Hiển thị sản phẩm
        |
        | Người dùng click
        v
ProductDetailScreen
        |
        | Back
        v
ProductListScreen
```

---

## 5. Cấu trúc source code chính

```text
ImageOptimizationApp/
|
├── app/
|   └── src/main/
|       |
|       ├── java/com/example/imageoptimizationapp/
|       |   ├── MainActivity.kt
|       |   ├── Product.kt
|       |   ├── ProductListScreen.kt
|       |   └── ProductDetailScreen.kt
|       |
|       ├── res/
|       |   └── drawable/
|       |       ├── placeholder_image.xml
|       |       └── error_image.xml
|       |
|       └── AndroidManifest.xml
|
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

### Chức năng của từng file

**`MainActivity.kt`**

Quản lý giao diện chính và việc chuyển đổi giữa danh sách sản phẩm và màn hình chi tiết.

**`Product.kt`**

Định nghĩa Product Model và tạo danh sách 100 sản phẩm.

**`ProductListScreen.kt`**

Hiển thị danh sách sản phẩm bằng `LazyColumn` và tải hình ảnh bằng Coil.

**`ProductDetailScreen.kt`**

Hiển thị thông tin và hình ảnh lớn của sản phẩm được chọn.

**`AndroidManifest.xml`**

Khai báo cấu hình ứng dụng và quyền truy cập Internet.

---

## 6. Kiểm thử Giai đoạn 1

Các chức năng cơ bản đã được kiểm tra:

| Chức năng | Kết quả mong đợi |
|---|---|
| Khởi động ứng dụng | Ứng dụng chạy thành công |
| Tạo dữ liệu | Có 100 sản phẩm |
| Tải ảnh URL | Hình ảnh được tải từ Internet |
| LazyColumn | Có thể cuộn danh sách |
| Placeholder | Hiển thị khi ảnh đang tải |
| URL lỗi | Hiển thị Error Image |
| Chọn sản phẩm | Mở màn hình chi tiết |
| Detail | Hiển thị đúng sản phẩm được chọn |
| Back | Quay lại danh sách sản phẩm |

---

## 7. Trạng thái Giai đoạn 1

```text
Internet Permission       : Hoàn thành
Coil Integration          : Hoàn thành
100 Products              : Hoàn thành
Remote Image Loading      : Hoàn thành
LazyColumn                : Hoàn thành
Product Card              : Hoàn thành
Placeholder Image         : Hoàn thành
Error Image               : Hoàn thành
Product Detail Screen     : Hoàn thành
Product Selection         : Hoàn thành
Back Navigation           : Hoàn thành
```

**Giai đoạn 1 đã hoàn thành phần chức năng cơ bản của ứng dụng.**

---

## 8. Giai đoạn tiếp theo

Sau khi hoàn thành phiên bản cơ bản, project sẽ tiếp tục thực hiện các nội dung liên quan đến đo lường và tối ưu hiệu năng tải ảnh.

Các nội dung dự kiến:

- Xây dựng và đo phiên bản Baseline.
- Phân tích Memory Cache.
- Phân tích Disk Cache.
- Kiểm tra Cache Hit và Cache Miss.
- Tối ưu kích thước ảnh.
- Resize hình ảnh.
- Sử dụng Thumbnail cho danh sách.
- Hạn chế decode ảnh có kích thước quá lớn.
- Kiểm thử khi cuộn nhanh.
- Kiểm thử khi mất kết nối mạng.
- Kiểm thử với 100, 500 và 1000 hình ảnh.
- Theo dõi CPU và Memory bằng Android Studio Profiler.
- Phân tích Frame Rendering và Jank.
- So sánh hiệu năng trước và sau khi tối ưu.

Mục tiêu cuối cùng là đánh giá sự khác biệt giữa phiên bản **trước tối ưu (Baseline)** và **sau tối ưu (Optimized)**.
