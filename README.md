# ImageOptimizationApp

Ứng dụng Android được xây dựng nhằm nghiên cứu và thực nghiệm các kỹ thuật **tối ưu tải, lưu trữ đệm và hiển thị hình ảnh trong ứng dụng Android**.

Project tập trung so sánh hai chế độ:

- **BASELINE:** phiên bản cơ sở trước khi áp dụng các kỹ thuật tối ưu.
- **OPTIMIZED:** phiên bản áp dụng các kỹ thuật tối ưu tải và hiển thị hình ảnh.

Ứng dụng sử dụng danh sách sản phẩm với hình ảnh được tải từ URL và thực hiện các testcase trên cùng môi trường để đánh giá sự khác biệt giữa hai phiên bản.

---

# 1. Thông tin project

- **Project:** ImageOptimizationApp
- **Platform:** Android
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Image Loading Library:** Coil 3
- **Minimum SDK:** API 24
- **Test Device:** Pixel 5 Emulator
- **Test Android Version:** Android 11 - API 30
- **Image Source:** Picsum Photos
- **Main Test Dataset:** 100 images
- **Scaling Test:** 100 / 500 / 1000 products

---

# 2. Mục tiêu

Project được xây dựng với các mục tiêu chính:

- Tải hình ảnh từ URL trong ứng dụng Android.
- Thực hiện tải ảnh bất đồng bộ để không chặn Main Thread.
- Hiển thị placeholder trong quá trình tải ảnh.
- Hiển thị error placeholder khi ảnh không thể tải.
- Hiển thị danh sách lớn bằng `LazyColumn`.
- Xây dựng màn hình danh sách và màn hình chi tiết.
- Nghiên cứu ảnh hưởng của kích thước ảnh đến bộ nhớ.
- Áp dụng resize/downsampling phù hợp với kích thước hiển thị.
- Sử dụng Memory Cache và Disk Cache.
- Kiểm tra khả năng sử dụng ảnh đã cache khi thiết bị offline.
- So sánh hiệu năng giữa BASELINE và OPTIMIZED.
- Theo dõi CPU và Memory bằng Android Studio Profiler.
- Kiểm tra khả năng mở rộng với 100, 500 và 1000 sản phẩm.

---

# 3. Công nghệ sử dụng

## Kotlin

Toàn bộ phần logic của ứng dụng được viết bằng Kotlin.

## Jetpack Compose

Jetpack Compose được sử dụng để xây dựng giao diện.

Các thành phần chính gồm:

- `LazyColumn`
- `Card`
- `Row`
- `Column`
- `AsyncImage`
- `Button`

`LazyColumn` giúp ứng dụng chỉ compose các item cần thiết trong vùng hiển thị, phù hợp với danh sách có số lượng lớn.

## Coil 3

Project sử dụng Coil để tải và hiển thị ảnh từ URL.

Các dependency chính:

```kotlin
implementation(libs.coil.compose)
implementation(libs.coil.network.okhttp)
```

Coil được sử dụng cho:

- Async image loading.
- Placeholder.
- Error image.
- Memory caching.
- Disk caching.
- Image request configuration.
- Resize/downsampling.

---

# 4. Cấu trúc chính của project

```text
ImageOptimizationApp/
│
├── app/
│   └── src/main/
│       ├── java/com/example/imageoptimizationapp/
│       │   ├── MainActivity.kt
│       │   ├── Product.kt
│       │   ├── ProductListScreen.kt
│       │   ├── ProductDetailScreen.kt
│       │   ├── ImageConfig.kt
│       │   └── ImageRequestFactory.kt
│       │
│       ├── res/
│       │   └── drawable/
│       │       ├── placeholder_image
│       │       └── error_image
│       │
│       └── AndroidManifest.xml
│
└── pic/
    ├── stage 1/
    ├── stage 2/
    ├── stage 3/
    └── stage 4/
```

---

# 5. Product Model

Model `Product` chứa thông tin cơ bản của một sản phẩm:

```kotlin
data class Product(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val description: String
)
```

Danh sách sản phẩm được tạo tự động.

URL ảnh sử dụng Picsum với seed riêng cho từng sản phẩm:

```text
https://picsum.photos/seed/{id}/800/600
```

Nhờ sử dụng `seed`, mỗi sản phẩm có thể sử dụng một ảnh xác định thay vì thay đổi ngẫu nhiên sau mỗi request.

---

# 6. Application Flow

Luồng hoạt động cơ bản:

```text
Start Application
       ↓
Generate Product List
       ↓
ProductListScreen
       ↓
LazyColumn
       ↓
Create ImageRequest
       ↓
Coil loads image asynchronously
       ↓
Display image
       ↓
User selects product
       ↓
ProductDetailScreen
       ↓
Display larger image
       ↓
Back
       ↓
ProductListScreen
```

---

# 7. Stage 1 - Basic Application

## Mục tiêu

Xây dựng ứng dụng Android cơ bản có khả năng:

- Hiển thị danh sách sản phẩm.
- Tạo ít nhất 100 sản phẩm.
- Tải ảnh từ URL.
- Hiển thị danh sách bằng `LazyColumn`.
- Mở màn hình chi tiết sản phẩm.
- Quay trở lại danh sách.

## Product List

Ứng dụng tạo danh sách mặc định gồm:

```text
100 products
```

Mỗi sản phẩm có:

- ID.
- Name.
- Image URL.
- Description.

Danh sách được hiển thị bằng:

```kotlin
LazyColumn
```

Điều này cho phép ứng dụng xử lý danh sách dài hiệu quả hơn so với việc render toàn bộ item cùng lúc.

## Detail Screen

Khi người dùng nhấn vào một sản phẩm:

```text
Product List
      ↓
Select Product
      ↓
Product Detail
```

Màn hình chi tiết hiển thị:

- Ảnh sản phẩm kích thước lớn.
- Tên sản phẩm.
- Mô tả.
- Nút Back.

---

# 8. Stage 2 - Coil & Async Image Loading

## Mục tiêu

Stage 2 tích hợp Coil để xử lý việc tải ảnh từ Internet.

Các chức năng được triển khai:

- Coil 3.
- AsyncImage.
- ImageRequest.
- Loading placeholder.
- Error placeholder.
- Async loading.

## Async Loading

Ảnh được tải bất đồng bộ thông qua Coil.

Điều này giúp tránh thực hiện công việc tải mạng nặng trực tiếp trên Main Thread.

Trong lúc ảnh chưa tải xong, ứng dụng vẫn có thể:

- Scroll.
- Nhấn item.
- Chuyển màn hình.
- Phản hồi thao tác người dùng.

## Loading Placeholder

Trong thời gian ảnh đang được tải:

```text
Request image
      ↓
Loading
      ↓
Placeholder
      ↓
Image loaded
      ↓
Real image
```

Loading placeholder đã được kiểm tra bằng cách:

- Clear application data.
- Giảm tốc độ mạng của emulator.
- Khởi động lại ứng dụng.
- Quan sát placeholder trước khi ảnh thật được tải.

## Error Placeholder

Nếu URL không hợp lệ hoặc server không trả được ảnh:

```text
Request
   ↓
Failure
   ↓
Error Placeholder
```

Test được thực hiện bằng URL không hợp lệ và ứng dụng hiển thị error image thay vì crash.

---

# 9. Stage 3 - Baseline vs Optimized

Stage 3 xây dựng hai chế độ để phục vụ việc so sánh hiệu năng.

## ImageMode

Project sử dụng:

```kotlin
enum class ImageMode {
    BASELINE,
    OPTIMIZED
}
```

Chế độ hiện tại được quản lý bởi:

```kotlin
object ImageConfig {
    var currentMode = ImageMode.OPTIMIZED
}
```

Điều này cho phép chuyển đổi giữa hai phiên bản mà không cần duy trì hai project riêng biệt.

---

# 10. BASELINE Mode

BASELINE đại diện cho phiên bản cơ sở để làm mốc so sánh.

Mục đích của chế độ này là mô phỏng cách tải ảnh chưa áp dụng đầy đủ các kỹ thuật tối ưu của project.

BASELINE được sử dụng để đánh giá:

- First load.
- Reload.
- Fast scrolling.
- Memory usage.
- CPU usage.
- High-resolution image.
- Offline behavior.
- Dataset scaling.

---

# 11. OPTIMIZED Mode

OPTIMIZED áp dụng các kỹ thuật tối ưu tải và hiển thị ảnh.

Các hướng tối ưu chính gồm:

- Memory Cache.
- Disk Cache.
- Resize ảnh theo nhu cầu hiển thị.
- Thumbnail cho danh sách.
- Kích thước ảnh lớn hơn cho Detail Screen.
- Hạn chế decode ảnh lớn không cần thiết.
- Async loading.
- Lazy loading thông qua `LazyColumn`.

Mục tiêu là giảm:

- Network requests không cần thiết.
- Image decoding overhead.
- Memory usage.
- Thời gian reload ảnh.

Đồng thời cải thiện:

- Scroll performance.
- Cache hit.
- Offline behavior.
- Trải nghiệm người dùng.

---

# 12. ImageRequestFactory

`ImageRequestFactory.kt` chịu trách nhiệm tạo ImageRequest phù hợp với từng chế độ.

Luồng tổng quát:

```text
Image URL
    ↓
ImageRequestFactory
    ↓
Check ImageMode
   ↙       ↘
BASELINE   OPTIMIZED
   ↓           ↓
Basic       Cache +
Request     Resize
   ↓           ↓
        Coil
          ↓
       AsyncImage
```

Việc tập trung cấu hình request vào một factory giúp:

- Tránh lặp code.
- Dễ chuyển đổi chế độ.
- Dễ thực hiện testcase.
- Dễ mở rộng các chính sách tối ưu sau này.

---

# 13. Stage 4 - Performance Testing

Stage 4 tập trung thực nghiệm và thu thập bằng chứng hiệu năng.

Môi trường kiểm thử được giữ cố định:

```text
Device: Pixel 5 Emulator
Android: Android 11
API: 30
Image source: Picsum Photos
Profiler: Android Studio Profiler
```

Các bài test được thực hiện cho BASELINE và OPTIMIZED trong cùng điều kiện để tăng tính nhất quán của phép so sánh.

---

# 14. Android Studio Profiler

Project sử dụng Android Studio Profiler / Live Telemetry để theo dõi:

- CPU activity.
- Memory consumption.
- Java memory.
- Native memory.
- Stack memory.
- Code memory.
- Total memory.
- Hoạt động của ứng dụng trong quá trình scroll.

Profiler được sử dụng đặc biệt trong:

- First Load.
- Cache/Reload.
- Fast Scroll.
- High-resolution image.
- Dataset Scaling.

---

# 15. Test Cases

## TC01 - First Load

### Mục tiêu

Đánh giá hành vi của ứng dụng trong lần tải ảnh đầu tiên.

### Quy trình

```text
Clear App Data
      ↓
Run Application
      ↓
Start Profiler
      ↓
Load Product Images
      ↓
Observe CPU & Memory
```

Thực hiện riêng cho:

- BASELINE.
- OPTIMIZED.

### Minh chứng

Profiler screenshot được lưu trong Stage 4 evidence.

---

## TC02 - Cache Hit / Reload

### Mục tiêu

Kiểm tra sự khác biệt khi ảnh được tải lại.

Các trạng thái quan sát:

```text
Image appears immediately
→ Cache/reload hoạt động tốt

Placeholder appears before image
→ Image cần thêm thời gian để được lấy lại

Error
→ Request thất bại
```

Test được thực hiện riêng cho BASELINE và OPTIMIZED.

OPTIMIZED được thiết kế để tận dụng cache nhằm hạn chế việc tải lại dữ liệu ảnh không cần thiết.

---

## TC03 - Fast Scroll

### Mục tiêu

Kiểm tra ứng dụng khi người dùng cuộn nhanh qua danh sách ảnh.

Theo dõi:

- CPU.
- Memory.
- UI responsiveness.
- Image loading.
- Placeholder.
- Crash/OOM.

### Điều kiện đạt

Ứng dụng phải:

- Không crash.
- Không OOM.
- Tiếp tục phản hồi thao tác.
- Không hiển thị sai ảnh cho item.
- Ảnh có thể tiếp tục xuất hiện sau khi request hoàn thành.

---

# 16. TC04 - High Resolution Image

### Mục tiêu

Kiểm tra ảnh kích thước lớn ở Detail Screen.

Test đánh giá ảnh hưởng của việc decode và hiển thị ảnh lớn giữa:

```text
BASELINE
vs
OPTIMIZED
```

OPTIMIZED sử dụng chiến lược kích thước ảnh phù hợp hơn với vùng hiển thị nhằm tránh xử lý bitmap lớn không cần thiết.

---

# 17. TC05 - Offline

### Mục tiêu

Kiểm tra hành vi của ứng dụng khi không có Internet.

Quy trình:

```text
Load images while online
        ↓
Images are available
        ↓
Disable network
        ↓
Scroll / Reload
        ↓
Observe image availability
```

Test giúp đánh giá vai trò của caching.

Trong trường hợp ảnh không tồn tại trong cache, ứng dụng phải hiển thị error placeholder thay vì crash.

---

# 18. TC06 - Bad URL / Slow Network

### Mục tiêu

Kiểm tra khả năng xử lý điều kiện mạng không lý tưởng.

Các trường hợp được kiểm tra gồm:

- URL lỗi.
- Server không phản hồi đúng.
- Mạng chậm.
- Request thất bại.

Emulator được cấu hình với network type chậm để quan sát quá trình loading.

Ứng dụng vẫn phải:

- Phản hồi thao tác.
- Hiển thị placeholder trong khi chờ.
- Hiển thị error image nếu request thất bại.
- Không crash.

---

# 19. TC07 - Dataset Scaling

TC07 kiểm tra khả năng mở rộng của ứng dụng khi số lượng sản phẩm tăng.

Các dataset:

```text
100 products
500 products
1000 products
```

Cả BASELINE và OPTIMIZED đều được kiểm tra.

Tổng cộng:

| Mode      | 100    | 500    | 1000   |
| --------- | ------ | ------ | ------ |
| BASELINE  | Tested | Tested | Tested |
| OPTIMIZED | Tested | Tested | Tested |

Trong mỗi bài test:

1. Cấu hình số lượng sản phẩm.
2. Clear application data.
3. Run application.
4. Start Profiler.
5. Scroll qua danh sách.
6. Theo dõi CPU và Memory.
7. Scroll đến item cuối.
8. Chụp Profiler làm evidence.

Ứng dụng đã có thể hiển thị và scroll đến:

```text
Product 100
Product 500
Product 1000
```

mà không xảy ra crash hoặc OutOfMemoryError trong các lần kiểm thử được ghi nhận.

---

# 20. Một số kết quả quan sát

Trong testcase 1000 products, snapshot Profiler đã ghi nhận:

### BASELINE

```text
Total Memory ≈ 136.2 MB
```

### OPTIMIZED

```text
Total Memory ≈ 128.7 MB
```

Tại hai snapshot được chụp, OPTIMIZED thấp hơn khoảng:

```text
7.5 MB
```

tương đương khoảng:

```text
5.5%
```

Đây là **snapshot tại thời điểm ghi nhận**, không được xem là mức giảm bộ nhớ trung bình của toàn bộ quá trình test.

Kết luận chính xác hơn sẽ được thực hiện trong Stage 5 dựa trên toàn bộ evidence và bảng so sánh.

---

# 21. Evidence Structure

Các screenshot minh chứng được tổ chức theo từng stage:

```text
pic/
│
├── stage 1/
│   └── Basic application evidence
│
├── stage 2/
│   ├── Coil integration
│   ├── AsyncImage
│   ├── Loading placeholder
│   └── Error placeholder
│
├── stage 3/
│   ├── Baseline
│   ├── Optimized
│   ├── ImageConfig
│   └── Request comparison
│
└── stage 4/
    ├── TC01_First_Load/
    ├── TC02_Cache_Hit/
    ├── TC03_Fast_Scroll/
    ├── TC04_High_Resolution/
    ├── TC05_Offline/
    ├── TC06_Error_Slow_Network/
    └── TC07_Dataset_Scaling/
```

---

# 22. TC07 Evidence

TC07 có bộ evidence riêng cho từng kích thước dataset:

```text
TC07_Dataset_Scaling/
├── TC07_Baseline_100_Profiler.png
├── TC07_Baseline_500_Profiler.png
├── TC07_Baseline_1000_Profiler.png
├── TC07_Optimized_100_Profiler.png
├── TC07_Optimized_500_Profiler.png
└── TC07_Optimized_1000_Profiler.png
```

Cấu trúc này giúp dễ dàng đối chiếu BASELINE và OPTIMIZED ở cùng số lượng sản phẩm.

---

# 23. Test Environment

Để đảm bảo kết quả có khả năng so sánh, các testcase chính được thực hiện trên cùng emulator:

```text
Device: Pixel 5
Android Version: Android 11
API Level: 30
```

Các yếu tố được cố gắng giữ giống nhau giữa BASELINE và OPTIMIZED:

- Emulator.
- API level.
- Product dataset.
- Image URL source.
- UI layout.
- Test procedure.
- Profiler.
- Scroll procedure.

Đối với testcase yêu cầu cold state, application data được clear trước khi chạy.

---

# 24. Clear Application Data

Trong quá trình kiểm thử, application data có thể được reset bằng ADB:

```powershell
& "C:\Users\THANH TIEN\AppData\Local\Android\Sdk\platform-tools\adb.exe" shell pm clear com.example.imageoptimizationapp
```

Kết quả thành công:

```text
Success
```

Việc clear data được sử dụng khi cần tránh ảnh hưởng của dữ liệu/cache từ lần chạy trước.

> Đường dẫn Android SDK có thể khác trên máy khác.

---

# 25. Dataset Configuration

Số lượng sản phẩm có thể thay đổi trong `Product.kt`.

Ví dụ:

```kotlin
return List(size = 100) { index ->
```

TC07 lần lượt thay đổi thành:

```kotlin
List(size = 100)
```

```kotlin
List(size = 500)
```

```kotlin
List(size = 1000)
```

Sau mỗi thay đổi, project được chạy lại trong cùng môi trường kiểm thử.

---

# 26. Chuyển đổi BASELINE / OPTIMIZED

Chế độ test được thay đổi trong:

```text
ImageConfig.kt
```

BASELINE:

```kotlin
var currentMode = ImageMode.BASELINE
```

OPTIMIZED:

```kotlin
var currentMode = ImageMode.OPTIMIZED
```

Tên chế độ hiện tại cũng được hiển thị trên giao diện để hỗ trợ việc xác nhận testcase và chụp evidence.

---

# 27. Trạng thái hiện tại

## Stage 1

```text
COMPLETED
```

- Android project.
- Product model.
- 100 URL images.
- LazyColumn.
- Product List.
- Detail Screen.

## Stage 2

```text
COMPLETED
```

- Coil 3.
- Async loading.
- ImageRequest.
- Loading placeholder.
- Error placeholder.
- Error handling.

## Stage 3

```text
COMPLETED
```

- BASELINE mode.
- OPTIMIZED mode.
- ImageConfig.
- ImageRequestFactory.
- Cache strategy.
- Resize / thumbnail strategy.

## Stage 4

```text
COMPLETED
```

- TC01 First Load.
- TC02 Cache Hit / Reload.
- TC03 Fast Scroll.
- TC04 High Resolution.
- TC05 Offline.
- TC06 Bad URL / Slow Network.S
- TC07 Dataset Scaling.
- Android Studio Profiler evidence.
- BASELINE vs OPTIMIZED testing.

---

# 28. Stage 5 - Final Analysis & Deliverables

Stage 5 là giai đoạn cuối của project.

Các công việc tiếp theo:

- Tổng hợp toàn bộ kết quả TC01-TC07.
- Thực hiện TC08 Before vs After Comparison.
- Lập bảng BASELINE vs OPTIMIZED.
- Tạo biểu đồ hiệu năng.
- Phân tích CPU.
- Phân tích Memory.
- Phân tích cache behavior.
- Tổng hợp kết quả 100 / 500 / 1000 products.
- Hoàn thiện báo cáo.
- Hoàn thiện README.
- Build APK.
- Quay video demo.
- Chuẩn bị source code và GitHub repository.
- Hoàn thiện các deliverables D1-D8.

---

# 29. TC08 - Before vs After Comparison

TC08 sẽ được hoàn thiện trong Stage 5.

TC08 không yêu cầu tạo thêm một chức năng mới trong ứng dụng mà sử dụng dữ liệu thu thập được từ các testcase trước để thực hiện so sánh tổng thể:

```text
BASELINE
    ↓
Before Optimization

vs

OPTIMIZED
    ↓
After Optimization
```

Các tiêu chí dự kiến:

- First Load.
- Cache Hit.
- Reload behavior.
- Fast Scroll.
- CPU behavior.
- Memory behavior.
- High-resolution image handling.
- Offline behavior.
- Dataset scaling.

Kết quả sẽ được trình bày dưới dạng:

- Comparison table.
- Memory chart.
- Performance chart.
- Experimental analysis.
- Final conclusion.

---

# 30. Deliverables

Các deliverables cuối cùng của project gồm:

- **D1:** Nội dung lý thuyết và cơ sở kỹ thuật.
- **D2:** Thiết kế ứng dụng và giải pháp.
- **D3:** Kotlin source code và Git repository.
- **D4:** Image loading / caching module.
- **D5:** BASELINE và OPTIMIZED implementation.
- **D6:** Test cases và experimental results.
- **D7:** Performance analysis/report.
- **D8:** APK, video demo và README.

---

# 31. Final Goal

Mục tiêu cuối cùng của project không chỉ là xây dựng một ứng dụng có khả năng hiển thị ảnh từ Internet mà còn chứng minh bằng thực nghiệm rằng các kỹ thuật như:

```text
Async Loading
      +
Lazy Loading
      +
Memory Cache
      +
Disk Cache
      +
Resize / Downsampling
      +
Thumbnail Strategy
```

có thể cải thiện quá trình tải và hiển thị hình ảnh trong ứng dụng Android.

Kết quả cuối cùng sẽ được đánh giá dựa trên dữ liệu thực nghiệm thu thập bằng Android Studio Profiler và các testcase được thực hiện trong cùng môi trường kiểm thử.

---

## Project Status

```text
Stage 1  ██████████  COMPLETED
Stage 2  ██████████  COMPLETED
Stage 3  ██████████  COMPLETED
Stage 4  ██████████  COMPLETED
Stage 5  ░░░░░░░░░░  NOT STARTED
```

**Current Progress: Stage 4 Completed**

Next step:

```text
Stage 5 - Final Analysis, TC08, Charts, Report, APK, Video & Final README
```
