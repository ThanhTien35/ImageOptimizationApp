# ImageOptimizationApp – Development Progress & Technical Notes

Tài liệu này dùng để theo dõi **tiến độ phát triển, cơ sở kỹ thuật, cách triển khai và kết quả kiểm thử** của project `ImageOptimizationApp`.

Mục đích của file này là giúp các thành viên trong nhóm có thể nhanh chóng biết:

- Project đang ở giai đoạn nào.
- Những chức năng nào đã hoàn thành.
- Tại sao các kỹ thuật tối ưu được sử dụng.
- BASELINE và OPTIMIZED khác nhau như thế nào.
- Các testcase đã được thực hiện ra sao.
- Evidence được lưu ở đâu.
- Những công việc nào còn lại trước khi nộp bài.

---

# 1. Project Overview

## Project Information

- **Project:** ImageOptimizationApp
- **Platform:** Android
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Image Loading:** Coil 3
- **Minimum SDK:** API 24
- **Main Test Device:** Pixel 5 Emulator
- **Test Android Version:** Android 11 – API 30
- **Image Source:** Picsum Photos
- **Main Dataset:** 100 products
- **Scaling Test:** 100 / 500 / 1000 products
- **Final Application Mode:** OPTIMIZED

## Main Objective

Project nghiên cứu và thực nghiệm các kỹ thuật:

- Async Image Loading
- Lazy Loading
- Memory Cache
- Disk Cache
- Resize / Downsampling
- Thumbnail Strategy
- Placeholder / Error Handling

Mục tiêu không chỉ là tải được ảnh từ Internet mà còn đánh giá ảnh hưởng của các kỹ thuật trên đến:

- Memory usage
- Image loading behavior
- Cache behavior
- High-resolution image processing
- Network/error handling
- Application stability
- Dataset scalability

---

# 2. Why Image Optimization Is Necessary

Ảnh tải từ Internet thường có kích thước và độ phân giải lớn hơn kích thước thực tế cần hiển thị trên màn hình.

Nếu ứng dụng tải và decode ảnh mà không có chiến lược phù hợp, có thể xảy ra:

- Tăng Memory usage.
- Decode bitmap lớn không cần thiết.
- Tăng network requests.
- Ảnh phải tải lại nhiều lần.
- Scroll danh sách kém ổn định.
- Tăng nguy cơ OutOfMemoryError khi dataset lớn.
- Trải nghiệm kém khi mạng chậm hoặc mất kết nối.

Vì vậy project xây dựng hai chế độ:

```text
BASELINE
    ↓
Before Optimization

vs

OPTIMIZED
    ↓
After Optimization
```

BASELINE đóng vai trò mốc so sánh.

OPTIMIZED áp dụng cache, resize/downsampling và các chiến lược xử lý ảnh phù hợp hơn.

---

# 3. Technical Background

## 3.1 Async Image Loading

Network request không nên thực hiện như một tác vụ nặng trực tiếp trên Main Thread.

Nếu Main Thread bị chiếm dụng quá lâu, UI có thể phản hồi chậm.

Project sử dụng Coil để thực hiện quá trình tải ảnh bất đồng bộ.

Luồng cơ bản:

```text
Image URL
    ↓
Image Request
    ↓
Asynchronous Loading
    ↓
Decode Image
    ↓
Display Image
```

Trong thời gian ảnh đang tải, người dùng vẫn có thể:

- Scroll.
- Chọn sản phẩm.
- Chuyển màn hình.
- Thực hiện thao tác UI.

---

# 4. Lazy Loading

Danh sách sản phẩm được xây dựng bằng:

```kotlin
LazyColumn
```

`LazyColumn` phù hợp với danh sách dài vì các item được compose theo nhu cầu hiển thị thay vì xử lý toàn bộ danh sách cùng lúc.

Điều này đặc biệt quan trọng trong TC07 khi dataset được tăng từ:

```text
100
↓
500
↓
1000 products
```

---

# 5. Memory Cache

Memory Cache lưu dữ liệu ảnh có thể tái sử dụng trong bộ nhớ.

Luồng đơn giản:

```text
Request Image
      ↓
Check Memory Cache
   ↙           ↘
HIT             MISS
 ↓                ↓
Reuse Image     Continue Loading
```

Ưu điểm:

- Ảnh có thể hiển thị nhanh khi được sử dụng lại.
- Giảm việc decode/request lại không cần thiết.

Tuy nhiên, Memory Cache cũng có thể làm Memory snapshot cao hơn tại một số thời điểm vì dữ liệu được giữ lại để phục vụ việc tái sử dụng.

Do đó:

> Memory cao hơn tại một snapshot không tự động đồng nghĩa với hiệu năng tổng thể kém hơn.

---

# 6. Disk Cache

Disk Cache cho phép dữ liệu ảnh được lưu trên bộ nhớ thiết bị.

So với Memory Cache:

```text
Memory Cache
→ nhanh
→ dữ liệu nằm trong RAM

Disk Cache
→ chậm hơn Memory Cache
→ nhưng có thể duy trì dữ liệu lâu hơn
```

Disk Cache đặc biệt có ý nghĩa khi:

- Reload ảnh.
- Mở lại nội dung đã tải.
- Mạng không ổn định.
- Thiết bị offline nhưng ảnh cần thiết đã được cache trước đó.

Ảnh chưa từng được cache vẫn cần network để tải.

---

# 7. Resize / Downsampling

Một ảnh có thể có độ phân giải lớn hơn nhiều so với vùng hiển thị.

Ví dụ:

```text
Original Image
800 × 600
      ↓
Displayed as small thumbnail
```

Việc decode ảnh lớn hơn mức cần thiết có thể gây lãng phí bộ nhớ.

OPTIMIZED sử dụng kích thước request phù hợp hơn với UI:

```text
Product List
→ Thumbnail / smaller image

Product Detail
→ Larger image
```

Mục tiêu:

- Hạn chế oversized decode.
- Giảm lượng dữ liệu ảnh cần xử lý.
- Sử dụng bộ nhớ phù hợp hơn với nhu cầu hiển thị.

---

# 8. Placeholder & Error Handling

Trong quá trình request:

```text
Request
   ↓
Loading
   ↓
Placeholder
```

Nếu thành công:

```text
Placeholder
   ↓
Real Image
```

Nếu thất bại:

```text
Request Failure
      ↓
Error Placeholder
```

Điều này giúp UI vẫn có trạng thái hiển thị rõ ràng khi:

- Mạng chậm.
- URL sai.
- Server lỗi.
- HTTP request thất bại.
- Thiết bị offline.

---

# 9. Main Project Structure

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
    ├── stage 4/
    └── stage 5/
```

---

# 10. Main Application Flow

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
Coil Async Loading
        ↓
Display Product Image
        ↓
Select Product
        ↓
ProductDetailScreen
        ↓
Load Larger Image
        ↓
Back
        ↓
ProductListScreen
```

---

# 11. Stage 1 – Basic Application

## Status

```text
COMPLETED
```

## Implemented

- Android Kotlin project.
- Jetpack Compose.
- Product model.
- 100 products.
- 100 URL images.
- Product List.
- LazyColumn.
- Product Detail.
- Back navigation.

## Why This Stage Was Needed

Stage 1 tạo một phiên bản ứng dụng cơ bản trước khi thêm các kỹ thuật tối ưu.

Điều này giúp tách biệt:

```text
Basic Application
        ↓
Image Loading
        ↓
Optimization
        ↓
Performance Testing
```

Nếu xây dựng tất cả cùng lúc, việc xác định nguyên nhân khi có lỗi hoặc khác biệt hiệu năng sẽ khó hơn.

---

# 12. Stage 2 – Coil & Async Image Loading

## Status

```text
COMPLETED
```

## Implemented

- Coil 3.
- AsyncImage.
- ImageRequest.
- Network image loading.
- Loading placeholder.
- Error placeholder.
- Async loading.
- Error handling.

Dependencies:

```kotlin
implementation(libs.coil.compose)
implementation(libs.coil.network.okhttp)
```

## Testing

Loading placeholder được kiểm tra bằng:

```text
Clear App Data
      ↓
Slow Network
      ↓
Start Application
      ↓
Observe Placeholder
      ↓
Image Loaded
```

Error placeholder được kiểm tra bằng URL không hợp lệ.

## Result

Ứng dụng có thể xử lý các trạng thái:

```text
Loading
Success
Failure
```

mà không cần thực hiện network image loading trực tiếp trên Main Thread.

---

# 13. Stage 3 – BASELINE vs OPTIMIZED

## Status

```text
COMPLETED
```

Stage 3 tạo hai chế độ trong cùng một project.

```kotlin
enum class ImageMode {
    BASELINE,
    OPTIMIZED
}
```

Chế độ được chọn thông qua:

```kotlin
object ImageConfig {
    var currentMode = ImageMode.OPTIMIZED
}
```

## Why One Project Instead of Two

Không cần duy trì hai project riêng biệt.

Việc sử dụng `ImageMode` giúp:

- Giữ cùng UI.
- Giữ cùng dataset.
- Giữ cùng image source.
- Chuyển mode dễ dàng.
- Hạn chế sai lệch khi test.
- Dễ so sánh BASELINE và OPTIMIZED.

---

# 14. ImageRequestFactory

`ImageRequestFactory.kt` tập trung logic tạo request.

```text
Image URL
    ↓
ImageRequestFactory
    ↓
Check ImageMode
   ↙          ↘
BASELINE    OPTIMIZED
   ↓            ↓
Basic       Cache / Resize
Request      Strategy
   ↘            ↙
        Coil
          ↓
      AsyncImage
```

## Why Factory Is Used

Factory giúp:

- Tránh lặp code.
- Tập trung cấu hình image request.
- Chuyển BASELINE/OPTIMIZED dễ hơn.
- Giảm nguy cơ hai phiên bản sử dụng UI khác nhau.
- Thuận tiện cho performance testing.

---

# 15. BASELINE Mode

BASELINE đại diện cho trạng thái trước khi áp dụng đầy đủ chiến lược tối ưu.

Nó được dùng làm mốc để đánh giá:

- First Load.
- Reload.
- Fast Scroll.
- Memory.
- High-resolution image.
- Offline behavior.
- Slow/error network.
- Dataset scaling.

BASELINE không phải một ứng dụng riêng.

Nó là một configuration của cùng project để đảm bảo phép so sánh công bằng hơn.

---

# 16. OPTIMIZED Mode

OPTIMIZED áp dụng các kỹ thuật chính:

- Memory Cache.
- Disk Cache.
- Resize/downsampling.
- Thumbnail strategy.
- Async loading.
- Lazy loading.
- Loading/error states.

Mục tiêu là cải thiện toàn bộ pipeline:

```text
Network
   ↓
Image Request
   ↓
Cache
   ↓
Decode / Resize
   ↓
Display
```

thay vì chỉ tập trung vào một chỉ số Memory duy nhất.

---

# 17. Stage 4 – Performance Testing

## Status

```text
COMPLETED
```

Stage 4 tập trung kiểm thử thực nghiệm.

Test environment:

```text
Device: Pixel 5 Emulator
Android: Android 11
API: 30
Image Source: Picsum Photos
Profiler: Android Studio Profiler / Live Telemetry
```

Các điều kiện chính được cố gắng giữ giống nhau giữa BASELINE và OPTIMIZED:

- Emulator.
- Android version.
- API level.
- Dataset.
- Image source.
- UI.
- Test procedure.
- Profiler.

Điều này giúp hạn chế việc kết quả bị ảnh hưởng bởi thay đổi môi trường thay vì thay đổi optimization strategy.

---

# 18. TC01 – First Load

## Purpose

Đánh giá trạng thái khi ứng dụng tải danh sách ảnh lần đầu.

## Procedure

```text
Clear App Data
      ↓
Select Image Mode
      ↓
Run Application
      ↓
Start Profiler
      ↓
Load Product Images
      ↓
Observe Memory / CPU
```

## Result Snapshot

```text
BASELINE  = 110.9 MB
OPTIMIZED = 152.8 MB
```

Optimized cao hơn:

```text
+41.9 MB
+37.8%
```

## Interpretation

Optimized không luôn có Memory snapshot thấp hơn.

Cache và dữ liệu ảnh được giữ lại trong runtime có thể làm snapshot cao hơn tại một thời điểm.

Vì vậy kết quả này không được sử dụng riêng lẻ để kết luận phiên bản nào tốt hơn.

---

# 19. TC02 – Cache Hit / Reload

## Purpose

Đánh giá khả năng tái sử dụng ảnh khi nội dung được mở lại.

## Result Snapshot

```text
BASELINE  = 114.3 MB
OPTIMIZED = 131.6 MB
```

## Observation

Optimized có Memory snapshot cao hơn, nhưng ảnh đã tải có thể được tái sử dụng thông qua cache.

Điều này minh họa trade-off:

```text
More cached data
        ↓
Potentially higher memory snapshot
        ↓
Faster reuse of previously loaded images
```

---

# 20. TC03 – Fast Scroll

## Purpose

Kiểm tra ứng dụng khi cuộn nhanh qua danh sách ảnh.

Theo dõi:

- Memory.
- CPU activity.
- UI responsiveness.
- Image loading.
- Placeholder behavior.
- Crash.
- OutOfMemoryError.

## Result Snapshot

```text
BASELINE  = 114.2 MB
OPTIMIZED = 141.7 MB
```

Cả hai phiên bản vẫn phản hồi trong các lần kiểm thử được ghi nhận và không xảy ra crash/OOM.

Không sử dụng một Memory snapshot duy nhất để khẳng định scroll performance tốt hay xấu.

---

# 21. TC04 – High Resolution Image

## Purpose

Đánh giá xử lý ảnh lớn trên Detail Screen.

## Result

```text
BASELINE  = 120.3 MB
OPTIMIZED = 115.5 MB

Difference = -4.8 MB
           = -4.0%
```

## Interpretation

Đây là testcase thể hiện rõ hơn lợi ích của resize/downsampling.

OPTIMIZED hạn chế xử lý ảnh lớn hơn nhu cầu hiển thị, từ đó giảm Memory snapshot tại thời điểm đo.

---

# 22. TC05 – Offline

## Purpose

Kiểm tra vai trò của cache khi mất kết nối Internet.

## Procedure

```text
Load Images Online
       ↓
Images Available
       ↓
Disable Network
       ↓
Access Images
       ↓
Observe Cached / Uncached Images
```

## Result

Ảnh đã có dữ liệu cache có thể tiếp tục được sử dụng tùy trạng thái cache.

Ảnh chưa cache:

```text
No Network
    ↓
Request Failure
    ↓
Error Placeholder
```

Điều quan trọng là cache không thể tạo ra dữ liệu cho một ảnh chưa từng được tải.

---

# 23. TC06 – Bad URL / Slow Network

## Purpose

Kiểm tra khả năng xử lý các điều kiện mạng không lý tưởng.

Đã kiểm tra:

- Bad URL.
- Slow network.
- Failed request.
- HTTP 503.

## Expected Behavior

```text
Slow Request
    ↓
Loading Placeholder
```

hoặc:

```text
Failed Request
    ↓
Error Placeholder
```

## Result

Trong các lần kiểm thử được ghi nhận:

- UI vẫn phản hồi.
- Loading state hoạt động.
- Error state hoạt động.
- Không ghi nhận crash.

---

# 24. TC07 – Dataset Scaling

## Purpose

Đánh giá khả năng mở rộng khi số lượng sản phẩm tăng.

Datasets:

```text
100
500
1000
```

Cả BASELINE và OPTIMIZED đều được kiểm tra.

## Results

| Dataset | Baseline | Optimized | Difference |
|---|---:|---:|---:|
| 100 | 130.4 MB | 117.4 MB | -13.0 MB (-10.0%) |
| 500 | 137.7 MB | 136.4 MB | -1.3 MB (-0.9%) |
| 1000 | 136.2 MB | 128.7 MB | -7.5 MB (-5.5%) |

## Interpretation

Ở cả ba snapshot của TC07:

```text
Optimized < Baseline
```

Ứng dụng cũng đã scroll đến:

```text
Product 100
Product 500
Product 1000
```

mà không ghi nhận OutOfMemoryError trong các lần test.

Các giá trị trên là **Profiler snapshots**, không phải average memory usage của toàn bộ quá trình.

---

# 25. Stage 5 – Final Analysis & Deliverables

## Current Status

```text
IN PROGRESS
```

Stage 5 hiện đã bắt đầu.

---

# 26. TC08 – Before vs After Comparison

## Status

```text
COMPLETED
```

TC08 không chạy một testcase mới.

Nó tổng hợp dữ liệu từ:

```text
TC01
TC02
TC03
TC04
TC05
TC06
TC07
```

để đánh giá:

```text
BASELINE
   vs
OPTIMIZED
```

## Completed Evidence

```text
TC08_Before_After_Comparison/
├── TC08_01_Summary_Table.jpg
├── TC08_02_Memory_Comparison.png
└── TC08_03_Analysis_Conclusion.jpg
```

### TC08_01

Tổng hợp:

- Test objective.
- Test condition.
- Baseline result.
- Optimized result.
- Difference.
- Evaluation.
- Conclusion.

### TC08_02

Biểu đồ Memory so sánh:

```text
TC01
TC02
TC03
TC04
TC07-100
TC07-500
TC07-1000
```

TC05 và TC06 không đưa vào Memory chart vì các testcase này chủ yếu đánh giá behavior khi offline/error/slow network.

### TC08_03

Phân tích tổng hợp theo ba nhóm:

```text
Memory & Image Processing
Cache, Network & Stability
Scalability
```

---

# 27. TC08 Overall Interpretation

Kết quả cho thấy OPTIMIZED không luôn có Memory snapshot thấp hơn BASELINE.

Cụ thể:

```text
TC01–TC03
Optimized Memory Snapshot > Baseline
```

Trong khi:

```text
TC04
Optimized < Baseline
```

và:

```text
TC07-100
TC07-500
TC07-1000

Optimized < Baseline
```

Do đó không nên kết luận optimization chỉ dựa trên một Memory snapshot.

Việc đánh giá phải kết hợp:

- Memory.
- Cache behavior.
- Image processing.
- Error handling.
- Stability.
- Scalability.

---

# 28. Final Configuration

## Status

```text
COMPLETED
```

Sau khi TC07 hoàn thành, dataset được đưa về cấu hình chính thức:

```kotlin
return List(size = 100) { index ->
```

Final mode:

```kotlin
var currentMode = ImageMode.OPTIMIZED
```

## Why

`100 products` là dataset chính của application demo.

`500` và `1000` chỉ được sử dụng trong TC07 để kiểm tra scalability.

OPTIMIZED được chọn làm configuration cuối vì đây là phiên bản chứa các kỹ thuật tối ưu cần trình bày trong sản phẩm cuối.

---

# 29. APK Build

## Status

```text
BUILD COMPLETED
```

Debug APK đã được generate thành công.

Final APK copy:

```text
pic/stage 5/APK/ImageOptimizationApp.apk
```

Evidence:

```text
Stage5_APK_Generated.png
Stage5_APK_Build_Success.png
```

APK hiện có kích thước khoảng:

```text
12.7 MB
```

## Remaining APK Task

Cần kiểm tra lại APK bằng cách:

```text
Install APK
    ↓
Open Application
    ↓
Verify OPTIMIZED mode
    ↓
Load Product List
    ↓
Scroll
    ↓
Open Detail
    ↓
Back to List
```

---

# 30. Evidence Structure – Current

```text
pic/
│
├── stage 1/
│   └── Basic Application
│
├── stage 2/
│   └── Coil / Loading / Error Evidence
│
├── stage 3/
│   └── Baseline / Optimized Evidence
│
├── stage 4/
│   ├── TC01_First_Load/
│   ├── TC02_Cache_Hit/
│   ├── TC03_Fast_Scroll/
│   ├── TC04_High_Resolution/
│   ├── TC05_Offline/
│   ├── TC06_Error_Slow_Network/
│   └── TC07_Dataset_Scaling/
│
└── stage 5/
    ├── TC08_Before_After_Comparison/
    │   ├── TC08_01_Summary_Table.jpg
    │   ├── TC08_02_Memory_Comparison.png
    │   └── TC08_03_Analysis_Conclusion.jpg
    │
    ├── Final_Configuration/
    │   ├── Final_01_Optimized_Mode.png
    │   └── Final_02_100_Products.png
    │
    └── APK/
        ├── ImageOptimizationApp.apk
        ├── Stage5_APK_Generated.png
        └── Stage5_APK_Build_Success.png
```

---

# 31. Current Progress

```text
Stage 1  ██████████  COMPLETED
Stage 2  ██████████  COMPLETED
Stage 3  ██████████  COMPLETED
Stage 4  ██████████  COMPLETED
Stage 5  ███████░░░  IN PROGRESS
```

## Stage 5 Checklist

```text
[✓] TC08 – Before vs After Comparison
[✓] TC08 Summary Table
[✓] TC08 Memory Chart
[✓] TC08 Analysis & Conclusion
[✓] Final dataset → 100 products
[✓] Final mode → OPTIMIZED
[✓] Build APK
[ ] Final APK installation test
[ ] Video Demo
[ ] Final Performance Analysis
[ ] D1 – Theory
[ ] D2 – Design
[ ] D3 – Source / Git
[ ] D4 – Image Loading / Cache
[ ] D5 – Baseline / Optimized
[ ] D6 – Test Cases / Results
[ ] D7 – Performance Analysis
[ ] D8 – APK / Video / Documentation
[ ] Final README
```

---

# 32. What Should Be Done Next?

Thành viên tiếp tục project nên làm theo thứ tự:

```text
1. Test final APK
        ↓
2. Record Video Demo
        ↓
3. Complete Performance Analysis
        ↓
4. Complete D1–D8
        ↓
5. Update Final README
        ↓
6. Final Git Commit / Push
        ↓
7. Prepare Submission
```

Không cần chạy lại TC01–TC07 nếu evidence hiện tại vẫn đầy đủ và không có thay đổi ảnh hưởng đến implementation đã kiểm thử.

---

# 33. Deliverables

Các deliverables cuối cùng:

| Deliverable | Nội dung | Status |
|---|---|---|
| D1 | Theory / Technical Background | Pending |
| D2 | Application & Solution Design | Pending |
| D3 | Kotlin Source + Git Repository | In Progress |
| D4 | Image Loading / Caching Module | Implemented |
| D5 | BASELINE / OPTIMIZED Implementation | Implemented |
| D6 | Test Cases / Experimental Results | Mostly Completed |
| D7 | Performance Analysis / Report | In Progress |
| D8 | APK / Video / README | In Progress |

---

# 34. Important Notes for Team Members

### Do not change the final dataset without a reason

Final configuration:

```kotlin
List(size = 100)
```

500 và 1000 chỉ dùng cho scalability testing.

### Do not leave the app in BASELINE mode

Final configuration:

```kotlin
ImageMode.OPTIMIZED
```

### Do not interpret Profiler snapshots as averages

Ví dụ:

```text
136.2 MB
128.7 MB
```

là giá trị tại thời điểm screenshot.

Chúng không đại diện cho average memory của toàn bộ phiên test.

### Keep the same test environment if retesting is necessary

Preferred environment:

```text
Pixel 5
Android 11
API 30
```

Nếu thay đổi emulator hoặc API level, kết quả mới không nên được so sánh trực tiếp với kết quả cũ mà không ghi rõ thay đổi môi trường.

### Preserve existing evidence

Không xóa các folder:

```text
stage 1
stage 2
stage 3
stage 4
stage 5
```

vì chúng được sử dụng cho report và final submission.

---

# 35. Current Handoff Point

Project hiện đang ở trạng thái:

```text
Implementation      → COMPLETED
Stage 1–4           → COMPLETED
TC01–TC07           → COMPLETED
TC08                → COMPLETED
Final Configuration → COMPLETED
APK Build           → COMPLETED
APK Final Test      → NEXT
Video Demo          → PENDING
D1–D8               → PENDING / IN PROGRESS
Final README        → PENDING
```

## Next Immediate Task

```text
Install and verify ImageOptimizationApp.apk
```

Sau khi APK được xác nhận hoạt động:

```text
Video Demo
    ↓
D1–D8
    ↓
Final README
    ↓
Final Submission
```

---

# 36. Final Technical Principle

Mục tiêu của project không phải chứng minh rằng:

```text
OPTIMIZED always uses less RAM
```

mà là đánh giá liệu một pipeline xử lý ảnh có:

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
      +
Loading / Error Handling
```

có thể tạo ra cách tải và hiển thị hình ảnh phù hợp hơn trong các tình huống sử dụng thực tế hay không.

Kết quả hiện tại cho thấy optimization tạo ra **trade-off**:

```text
Cache may retain more data
        ↓
Memory can be higher at some snapshots

BUT

Resize / Downsampling
        +
Cache Reuse
        +
Controlled Image Requests
        +
Scalability
        ↓
provide measurable benefits in other scenarios
```

Vì vậy kết luận cuối cùng phải dựa trên **toàn bộ experimental evidence**, không dựa trên một giá trị Memory riêng lẻ.
