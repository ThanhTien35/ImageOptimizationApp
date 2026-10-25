# ImageOptimizationApp

Ứng dụng Android minh họa và đánh giá các kỹ thuật **tối ưu tải, lưu trữ đệm và hiển thị hình ảnh** trong ứng dụng Android.

Project được xây dựng bằng **Kotlin + Jetpack Compose**, sử dụng **Coil 3** để tải ảnh từ URL. Ứng dụng cung cấp hai chế độ `BASELINE` và `OPTIMIZED` để thực hiện các thử nghiệm và so sánh hiệu năng tải ảnh.

---

## 1. Thông tin project

- **Project:** ImageOptimizationApp
- **Package:** `com.example.imageoptimizationapp`
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Image Loading Library:** Coil 3.3.0
- **Minimum SDK:** API 24
- **Target SDK:** API 37
- **Thiết bị kiểm thử:** Pixel 5 Emulator
- **Android kiểm thử:** Android 11 - API 30
- **Số lượng sản phẩm mặc định:** 100
- **Nguồn ảnh:** Remote URL
- **Kiến trúc giao diện:** Product List + Product Detail

---

# 2. Mục tiêu

Project tập trung nghiên cứu và thực nghiệm các vấn đề liên quan đến việc tải và hiển thị nhiều hình ảnh trong ứng dụng Android.

Các mục tiêu chính:

- Tải hình ảnh bất đồng bộ từ Internet.
- Hiển thị danh sách lớn bằng `LazyColumn`.
- Sử dụng loading placeholder trong thời gian chờ ảnh.
- Xử lý URL lỗi bằng error placeholder.
- Hạn chế công việc nặng trên Main Thread.
- Nghiên cứu Memory Cache và Disk Cache.
- Resize/downsample ảnh theo kích thước hiển thị.
- So sánh chế độ Baseline và Optimized.
- Theo dõi CPU và Memory bằng Android Studio Profiler.
- Kiểm tra ứng dụng với 100, 500 và 1000 sản phẩm.
- Kiểm tra Fast Scroll, Offline, Bad URL và High-resolution Image.
- Đánh giá sự khác biệt trước và sau tối ưu.

---

# 3. Công nghệ sử dụng

Project sử dụng:

- Kotlin
- Android SDK
- Jetpack Compose
- Material 3
- LazyColumn
- Coil 3
- Coil Network OkHttp
- Android Studio Profiler
- Android Emulator

Các dependency Coil chính:

```kotlin
implementation(libs.coil.compose)
implementation(libs.coil.network.okhttp)
```

Ứng dụng cần quyền truy cập Internet:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

# 4. Cấu trúc project

Các file Kotlin chính:

```text
app/src/main/java/com/example/imageoptimizationapp/

├── MainActivity.kt
├── Product.kt
├── ProductListScreen.kt
├── ProductDetailScreen.kt
├── ImageConfig.kt
├── ImageRequestFactory.kt
└── ui.theme/
```

Vai trò của từng file:

### `MainActivity.kt`

Quản lý màn hình hiện tại của ứng dụng và state giữa Product List và Product Detail.

Ngoài ra, `LazyListState` được giữ ở cấp `MainActivity` để bảo toàn vị trí cuộn khi người dùng:

```text
Product List
→ Product Detail
→ Back
→ Product List
```

Ứng dụng sẽ quay lại vị trí trước đó thay vì trở về Product 1.

### `Product.kt`

Định nghĩa model `Product` và tạo danh sách sản phẩm phục vụ kiểm thử.

Mỗi Product gồm:

```text
id
name
imageUrl
description
```

Danh sách mặc định hiện có **100 sản phẩm**.

### `ProductListScreen.kt`

Hiển thị danh sách sản phẩm bằng:

```text
LazyColumn
```

Mỗi item gồm:

```text
Image
Product Name
Description
```

Ảnh được tải bất đồng bộ bằng Coil.

### `ProductDetailScreen.kt`

Hiển thị:

- Ảnh sản phẩm kích thước lớn hơn.
- Tên sản phẩm.
- Mô tả sản phẩm.
- Nút Back.

### `ImageConfig.kt`

Quản lý chế độ tải ảnh:

```kotlin
enum class ImageMode {
    BASELINE,
    OPTIMIZED
}
```

Có thể chuyển chế độ kiểm thử bằng:

```kotlin
var currentMode = ImageMode.BASELINE
```

hoặc:

```kotlin
var currentMode = ImageMode.OPTIMIZED
```

### `ImageRequestFactory.kt`

Tạo `ImageRequest` tương ứng với từng chế độ.

Luồng tổng quát:

```text
ImageConfig
     ↓
BASELINE / OPTIMIZED
     ↓
ImageRequestFactory
     ↓
Coil ImageRequest
     ↓
AsyncImage
```

---

# 5. Giai đoạn 1 - Basic Application

## 5.1. Mục tiêu

Xây dựng ứng dụng Android cơ bản gồm:

- Kotlin project.
- Product model.
- Danh sách sản phẩm.
- 100 URL ảnh.
- Product List.
- Product Detail.
- Điều hướng List → Detail → Back.

## 5.2. Product List

Danh sách được hiển thị bằng `LazyColumn`.

Ứng dụng tạo:

```text
Product 1
Product 2
...
Product 100
```

Mỗi sản phẩm có URL ảnh riêng.

Việc sử dụng `LazyColumn` giúp chỉ compose các item cần thiết trên màn hình thay vì render toàn bộ 100 item cùng lúc.

## 5.3. Product Detail

Khi người dùng chọn một sản phẩm:

```text
Product List
      ↓
Product Detail
```

Detail Screen hiển thị ảnh lớn hơn cùng thông tin của sản phẩm.

Người dùng có thể bấm Back để trở về danh sách.

## 5.4. Minh chứng

Ảnh của Stage 1 được lưu tại:

```text
pic/stage 1/
```

Ví dụ:

```text
Stage1_01_Project_Config.png
Stage1_02_100_Products_URL.png
```

---

# 6. Giai đoạn 2 - Coil và Async Image Loading

## 6.1. Coil 3

Project sử dụng Coil 3 để tải ảnh từ remote URL.

Hai dependency chính:

```text
coil-compose
coil-network-okhttp
```

Ảnh được hiển thị bằng:

```kotlin
AsyncImage(...)
```

## 6.2. Loading Placeholder

Trong thời gian ảnh đang được tải, ứng dụng hiển thị placeholder.

Mục đích:

- Tránh vùng ảnh bị trống.
- Cung cấp phản hồi trực quan cho người dùng.
- Cho biết request ảnh đang được xử lý.

Loading placeholder đã được kiểm thử bằng cách:

- Clear app data.
- Giảm tốc độ mạng emulator.
- Mở ứng dụng.
- Quan sát placeholder trước khi ảnh thật xuất hiện.

Kết quả:

```text
PASS
```

## 6.3. Error Placeholder

Ứng dụng có error placeholder khi Coil không thể tải ảnh.

Kiểm thử được thực hiện bằng URL không hợp lệ.

Kết quả:

```text
Product có URL lỗi → Error Placeholder
Product khác → tải ảnh bình thường
```

Kết quả:

```text
PASS
```

## 6.4. Async Loading

Coil xử lý việc tải ảnh bất đồng bộ.

Trong quá trình ảnh đang tải, giao diện vẫn có thể tương tác và cuộn mà không cần thực hiện network request trực tiếp trên Main Thread.

## 6.5. Minh chứng

Ảnh Stage 2:

```text
pic/stage 2/
```

Bao gồm các minh chứng như:

```text
Stage2_03_Coil_Dependencies.png
Stage2_04_AsyncImage_Code.png
Stage2_05_Detail_AsyncImage_Code.png
Stage2_Error_Placeholder.png
Stage2_Loading_Placeholder.png
```

---

# 7. Giai đoạn 3 - Baseline và Optimized

Giai đoạn 3 xây dựng hai chế độ tải ảnh để phục vụ thực nghiệm.

```text
BASELINE
vs
OPTIMIZED
```

---

## 7.1. Baseline

Baseline đại diện cho cách tải ảnh cơ bản.

Request:

```kotlin
ImageRequest.Builder(context)
    .data(imageUrl)
    .build()
```

Baseline không bổ sung các cấu hình tối ưu chủ động được sử dụng trong chế độ Optimized.

---

## 7.2. Optimized

Optimized bổ sung các cấu hình liên quan đến:

- Resize ảnh.
- Memory Cache.
- Disk Cache.

Ví dụ:

```kotlin
ImageRequest.Builder(context)
    .data(imageUrl)
    .size(width, height)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .diskCachePolicy(CachePolicy.ENABLED)
    .build()
```

Mục tiêu là kiểm tra ảnh theo kích thước phù hợp hơn với vùng hiển thị và chủ động cấu hình cache.

---

## 7.3. ImageRequestFactory

`ImageRequestFactory.kt` chịu trách nhiệm chọn request phù hợp.

```text
currentMode
    │
    ├── BASELINE
    │       ↓
    │   Baseline Request
    │
    └── OPTIMIZED
            ↓
        Optimized Request
```

Điều này giúp giữ nguyên giao diện và dữ liệu trong khi thay đổi chiến lược tải ảnh.

---

## 7.4. Chuyển đổi chế độ

Chế độ được cấu hình tại:

```text
ImageConfig.kt
```

Baseline:

```kotlin
var currentMode = ImageMode.BASELINE
```

Optimized:

```kotlin
var currentMode = ImageMode.OPTIMIZED
```

Trên giao diện, mode hiện tại được hiển thị:

```text
Image Mode: BASELINE
```

hoặc:

```text
Image Mode: OPTIMIZED
```

Điều này giúp xác định rõ mode trong ảnh minh chứng và quá trình benchmark.

---

## 7.5. Minh chứng

Ảnh Stage 3:

```text
pic/stage 3/
```

Bao gồm:

```text
Stage3_01_Baseline.png
Stage3_02_Optimized.png
Stage3_03_ImageConfig.png
Stage3_04_Request_Comparison.png
```

---

# 8. UI và State Fix

Trước khi thực hiện Performance Testing, project được sửa thêm hai vấn đề giao diện.

## 8.1. Edge-to-Edge System Bars

Nội dung trước đây có khả năng hiển thị sát hoặc đè lên vùng System Bars.

Các màn hình được bổ sung xử lý safe drawing area để nội dung nằm đúng trong vùng hiển thị.

Kết quả:

```text
Status Bar
------------------------
Image Mode
Product List
...
------------------------
Navigation Bar
```

Nội dung không còn đè lên System Bars.

## 8.2. Preserve Scroll Position

Ban đầu khi thực hiện:

```text
Product 70
→ Detail
→ Back
```

LazyColumn có thể quay trở lại đầu danh sách.

Project sử dụng:

```kotlin
rememberLazyListState()
```

và truyền `LazyListState` vào `ProductListScreen`.

Sau khi sửa:

```text
Product 70
→ Detail
→ Back
→ trở lại khu vực Product 70
```

Vị trí cuộn được bảo toàn.

---

# 9. Giai đoạn 4 - Performance Testing

Thiết bị và môi trường kiểm thử chính:

```text
Device       : Pixel 5 Emulator
Android      : Android 11
API          : 30
Products     : 100 mặc định
Network      : Full, trừ các test liên quan Offline/Slow Network
Profiler     : Android Studio Profiler
Image Source : Remote URL
```

Các test được thực hiện trên cùng thiết bị và môi trường nhằm giảm sai lệch giữa Baseline và Optimized.

---

# 10. TC01 - First Load

## 10.1. Mục tiêu

Kiểm tra hành vi của ứng dụng trong lần tải ảnh đầu tiên khi app bắt đầu từ trạng thái sạch.

Quy trình:

```text
Set Image Mode
→ Build
→ Clear App Data
→ Run
→ View Live Telemetry
→ Không scroll/click
→ Record khoảng 30 giây
→ Stop Recording
```

## 10.2. Baseline

Điều kiện:

```text
Mode        : BASELINE
Products    : 100
Network     : Full
Interaction : Không scroll/click
Duration    : khoảng 30 giây
```

Kết quả cuối lượt:

| Metric | Baseline |
|---|---:|
| Total Memory | 110.9 MB |
| Java | 11.4 MB |
| Native | 44.8 MB |
| Stack | 2.5 MB |
| Code | 39.4 MB |
| Others | 12.8 MB |
| Image Loading | PASS |

## 10.3. Optimized

Điều kiện kiểm thử được giữ tương đương với Baseline.

Kết quả cuối lượt:

| Metric | Optimized |
|---|---:|
| Total Memory | 152.8 MB |
| Java | 16.3 MB |
| Native | 79.3 MB |
| Stack | 2.3 MB |
| Code | 41.5 MB |
| Others | 13.4 MB |
| Image Loading | PASS |

## 10.4. So sánh TC01

| Metric | Baseline | Optimized |
|---|---:|---:|
| Total Memory | 110.9 MB | 152.8 MB |
| Java | 11.4 MB | 16.3 MB |
| Native | 44.8 MB | 79.3 MB |
| Stack | 2.5 MB | 2.3 MB |
| Code | 39.4 MB | 41.5 MB |
| Others | 12.8 MB | 13.4 MB |
| Result | PASS | PASS |

Trong lượt đo TC01 này, memory cuối lượt của Optimized cao hơn Baseline.

Kết quả này được giữ nguyên theo dữ liệu thực nghiệm và chưa được sử dụng riêng lẻ để kết luận Optimized tốt hơn hoặc kém hơn Baseline.

Việc đánh giá tổng thể cần kết hợp thêm Cache Hit, Fast Scroll, High-resolution Image, scalability và các test tiếp theo.

## 10.5. Minh chứng

```text
pic/TC/TC01_First_Load/
```

Bao gồm:

```text
TC01_Baseline_Profiler.png
TC01_Optimized_Profiler.png
```

---

# 11. TC02 - Cache Hit

## 11.1. Mục tiêu

Kiểm tra hành vi tải lại ảnh sau khi ảnh đã được tải trước đó.

Quy trình:

```text
Clear App Data
→ Run
→ Đợi ảnh tải hoàn tất
→ Không clear data/cache nữa
→ Start Profiler
→ Product 1
→ Detail
→ Back
→ Product 2
→ Detail
→ Back
→ Stop Recording
```

Việc không clear dữ liệu giữa initial load và reload là cần thiết để giữ trạng thái cache phục vụ kiểm thử.

---

## 11.2. Baseline

Kết quả cuối lượt:

| Metric | Baseline |
|---|---:|
| Total Memory | 114.3 MB |
| Java | 12.6 MB |
| Native | 46.3 MB |
| Stack | 2.3 MB |
| Code | 39.5 MB |
| Others | 13.6 MB |
| Error | No |
| Result | PASS |

---

## 11.3. Optimized

Kết quả cuối lượt:

| Metric | Optimized |
|---|---:|
| Total Memory | 131.6 MB |
| Java | 17.3 MB |
| Native | 56.4 MB |
| Stack | 2.2 MB |
| Code | 41.4 MB |
| Others | 14.4 MB |
| Error | No |
| Result | PASS |

---

## 11.4. So sánh TC02

| Metric | Baseline | Optimized |
|---|---:|---:|
| Total Memory | 114.3 MB | 131.6 MB |
| Java | 12.6 MB | 17.3 MB |
| Native | 46.3 MB | 56.4 MB |
| Stack | 2.3 MB | 2.2 MB |
| Code | 39.5 MB | 41.4 MB |
| Others | 13.6 MB | 14.4 MB |
| Error | No | No |
| Result | PASS | PASS |

TC02 xác nhận ứng dụng có thể tải và hiển thị lại ảnh ổn định trong quá trình List → Detail → Back.

Memory snapshot của Optimized trong lượt đo này vẫn cao hơn Baseline.

Android Studio Live Telemetry được sử dụng để theo dõi CPU và Memory. Các biểu đồ CPU được giữ làm minh chứng trực quan; không tự suy đoán giá trị CPU peak khi Profiler không cung cấp một số peak chính xác trong screenshot.

## 11.5. Minh chứng

```text
pic/TC/TC02_Cache_Hit/
```

Bao gồm:

```text
TC02_Baseline_CacheHit_Profiler.png
TC02_Optimized_CacheHit_Profiler.png
```

---

# 12. Danh sách Test Case

Tiến độ Giai đoạn 4:

- [x] **TC01 - First Load**
- [x] **TC02 - Cache Hit**
- [ ] **TC03 - Fast Scroll**
- [ ] **TC04 - High-resolution Image**
- [ ] **TC05 - Offline**
- [ ] **TC06 - Bad URL / Slow Server**
- [ ] **TC07 - 100 → 500 → 1000 Products**
- [ ] **TC08 - Baseline vs Optimized**

---

# 13. TC03 - Fast Scroll

**Status:** Pending

Mục tiêu:

- Cuộn nhanh qua danh sách sản phẩm.
- Kiểm tra UI responsiveness.
- Kiểm tra placeholder.
- Kiểm tra ảnh có bị hiển thị sai item hay không.
- Theo dõi CPU và Memory.
- Kiểm tra crash hoặc freeze.

---

# 14. TC04 - High-resolution Image

**Status:** Pending

Mục tiêu:

- Kiểm tra ảnh độ phân giải lớn.
- So sánh Baseline và Optimized.
- Đánh giá resize/downsampling.
- Theo dõi ảnh được decode và memory usage.

---

# 15. TC05 - Offline

**Status:** Pending

Mục tiêu:

- Tải ảnh trước khi ngắt mạng.
- Chuyển emulator sang Offline.
- Kiểm tra khả năng hiển thị ảnh đã có dữ liệu/cache.
- Kiểm tra error handling với ảnh chưa tải.

---

# 16. TC06 - Bad URL / Slow Server

**Status:** Pending

Mục tiêu:

- Kiểm tra URL không hợp lệ.
- Kiểm tra server phản hồi chậm.
- Quan sát loading placeholder.
- Quan sát error placeholder.
- Đảm bảo ứng dụng không crash.

Trong quá trình phát triển, Picsum từng trả về lỗi:

```text
Error 503 Maximum threads for service reached
```

Ảnh minh chứng lỗi server được lưu tại:

```text
pic/Server_503_Picsum.png
```

Đây là lỗi từ dịch vụ ảnh bên ngoài và không phải lỗi crash của ứng dụng.

---

# 17. TC07 - Scalability

**Status:** Pending

Kiểm thử ứng dụng với:

```text
100 Products
500 Products
1000 Products
```

Mục tiêu:

- Theo dõi Memory.
- Theo dõi CPU.
- Kiểm tra khả năng cuộn.
- Kiểm tra crash/OOM.
- Quan sát khả năng Lazy Loading khi dataset tăng.

---

# 18. TC08 - Baseline vs Optimized

**Status:** Pending

TC08 sẽ tổng hợp kết quả của các test trước để so sánh:

```text
BASELINE
vs
OPTIMIZED
```

Các tiêu chí dự kiến:

- First Load
- Cache Hit
- Memory Usage
- Fast Scroll
- High-resolution Image
- Offline behavior
- Error handling
- Scalability
- UI responsiveness

Không kết luận mode nào tốt hơn cho đến khi hoàn thành đầy đủ các phép thử.

---

# 19. Android Studio Profiler

Project sử dụng:

```text
View Live Telemetry
```

để theo dõi runtime.

Các dữ liệu chính:

- CPU activity
- Total Memory
- Java Memory
- Native Memory
- Stack
- Code
- Others

Quy trình benchmark cố gắng giữ các điều kiện giống nhau giữa Baseline và Optimized.

Ví dụ:

```text
Same Device
Same API
Same Product Dataset
Same Network Condition
Same Interaction Sequence
Same Test Duration
```

---

# 20. Thư mục minh chứng

Ảnh minh chứng được tổ chức trong:

```text
pic/
```

Cấu trúc hiện tại:

```text
pic/
│
├── stage 1/
│
├── stage 2/
│
├── stage 3/
│
├── TC/
│   ├── TC01_First_Load/
│   │   ├── TC01_Baseline_Profiler.png
│   │   └── TC01_Optimized_Profiler.png
│   │
│   └── TC02_Cache_Hit/
│       ├── TC02_Baseline_CacheHit_Profiler.png
│       └── TC02_Optimized_CacheHit_Profiler.png
│
└── Server_503_Picsum.png
```

Các test tiếp theo sẽ được bổ sung:

```text
pic/TC/
├── TC03_Fast_Scroll/
├── TC04_High_Resolution/
├── TC05_Offline/
├── TC06_Error_Slow_Server/
├── TC07_Scalability/
└── TC08_Baseline_vs_Optimized/
```

---

# 21. Trạng thái hiện tại

```text
Stage 1 - Basic Application                 COMPLETE
Stage 2 - Coil + Async Image Loading        COMPLETE
Stage 3 - Baseline + Optimized              COMPLETE
UI Fix - System Bars + Scroll State         COMPLETE
Stage 4 - Performance Testing               IN PROGRESS
```

Performance Testing:

```text
TC01 First Load                COMPLETE
TC02 Cache Hit                 COMPLETE
TC03 Fast Scroll               PENDING
TC04 High-resolution           PENDING
TC05 Offline                   PENDING
TC06 Bad URL / Slow Server     PENDING
TC07 100 / 500 / 1000          PENDING
TC08 Baseline vs Optimized     PENDING
```

---

# 22. Các bước tiếp theo

Các công việc tiếp theo:

1. Thực hiện TC03 - Fast Scroll.
2. Thực hiện TC04 - High-resolution Image.
3. Thực hiện TC05 - Offline.
4. Thực hiện TC06 - Bad URL / Slow Server.
5. Thực hiện TC07 với 100, 500 và 1000 sản phẩm.
6. Tổng hợp TC08 Baseline vs Optimized.
7. Tổng hợp số liệu thành bảng và biểu đồ.
8. Hoàn thiện báo cáo thực nghiệm.
9. Build APK.
10. Quay video demo.
11. Hoàn thiện README và tài liệu bàn giao.

---

# 23. Ghi chú

Kết quả benchmark trong project là kết quả thực nghiệm trên:

```text
Pixel 5 Emulator
Android 11
API 30
```

Các giá trị CPU và Memory có thể thay đổi giữa các lượt chạy do:

- Android Runtime.
- Garbage Collection.
- Network.
- Image server.
- Emulator.
- Background processes.
- Trạng thái cache.

Vì vậy, kết quả được đánh giá dựa trên cùng môi trường kiểm thử và cùng quy trình giữa Baseline và Optimized.

Các số liệu thực nghiệm được giữ nguyên, không điều chỉnh để buộc Optimized phải có kết quả tốt hơn Baseline.
