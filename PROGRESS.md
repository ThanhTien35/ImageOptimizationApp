# ImageOptimizationApp – Development Progress & Technical Notes

Tài liệu này ghi lại toàn bộ **quá trình nghiên cứu, phát triển, tối ưu và kiểm thử** của project `ImageOptimizationApp`.

Khác với `README.md` tập trung vào hướng dẫn cài đặt và sử dụng, `PROGRESS.md` trình bày project theo từng giai đoạn phát triển, bao gồm:

- Cơ sở kỹ thuật được nghiên cứu.
- Mục tiêu của từng Stage.
- Các chức năng đã triển khai.
- Các kỹ thuật tối ưu được áp dụng.
- Phương pháp kiểm thử.
- Kết quả thực nghiệm.
- Evidence của từng giai đoạn.
- Trạng thái các deliverable cuối cùng.

> **Project Status:** COMPLETED  
> **Remaining:** Video Demo – YouTube link will be updated later

---

# 1. Project Overview

## Project Information

- **Project:** ImageOptimizationApp
- **Platform:** Android
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Image Loading Library:** Coil 3
- **Minimum SDK:** API 24
- **Compile / Target SDK:** API 37
- **Main Test Device:** Pixel 5 Emulator
- **Test Android Version:** Android 11 – API 30
- **Image Source:** Picsum Photos
- **Main Dataset:** 100 products
- **Scaling Test:** 100 / 500 / 1000 products
- **Final Application Mode:** OPTIMIZED

## Main Objective

Project tập trung nghiên cứu và thực nghiệm các kỹ thuật:

- Async Image Loading.
- Lazy Loading.
- Memory Cache.
- Disk Cache.
- Resize / Downsampling.
- Placeholder.
- Error Handling.

Mục tiêu không chỉ là tải được hình ảnh từ Internet mà còn đánh giá ảnh hưởng của các kỹ thuật trên đến:

- Memory usage.
- Image loading behavior.
- Cache behavior.
- High-resolution image processing.
- Network / error handling.
- Application stability.
- Dataset scalability.

## Development Process

```text
Stage 1
Basic Application
      ↓
Stage 2
Image Loading
      ↓
Stage 3
Image Optimization
      ↓
Stage 4
Performance Testing
      ↓
Stage 5
Final Analysis & Deliverables
```

| Stage | Nội dung | Status |
|---|---|---|
| Stage 1 | Basic Application & Lazy Loading | COMPLETED |
| Stage 2 | Async Image Loading & Error Handling | COMPLETED |
| Stage 3 | Image Optimization – BASELINE vs OPTIMIZED | COMPLETED |
| Stage 4 | Performance Testing – TC01 đến TC07 | COMPLETED |
| Stage 5 | Final Analysis, TC08 & Deliverables | COMPLETED |
| Video Demo | YouTube Demo | PENDING LINK |

---

# 2. Stage 1 – Basic Application & Lazy Loading

## Status

```text
COMPLETED
```

## 2.1 Objective

Stage 1 xây dựng phiên bản cơ bản của ứng dụng trước khi bổ sung các kỹ thuật xử lý và tối ưu hình ảnh.

Ứng dụng cần có:

- Product data.
- Product List.
- Product Detail.
- Navigation.
- Khả năng hiển thị dataset lớn bằng danh sách phù hợp.

Stage này đóng vai trò làm nền tảng:

```text
Basic Application
       ↓
Image Loading
       ↓
Optimization
       ↓
Performance Testing
```

---

## 2.2 Technical Background – Lazy Loading

Danh sách sản phẩm được xây dựng bằng:

```kotlin
LazyColumn
```

Trong Jetpack Compose, `LazyColumn` phù hợp với danh sách dài vì các item được compose theo nhu cầu hiển thị thay vì hiển thị toàn bộ dataset cùng một lúc.

Điều này đặc biệt quan trọng khi dataset tăng từ:

```text
100
 ↓
500
 ↓
1000 products
```

Việc sử dụng Lazy Loading giúp ứng dụng có thể xử lý danh sách lớn hợp lý hơn và tạo nền tảng cho TC07 – Dataset Scaling.

---

## 2.3 Implemented

- Khởi tạo Android project bằng Kotlin.
- Sử dụng Jetpack Compose.
- Xây dựng `Product` model.
- Tạo dataset 100 products.
- Tạo URL hình ảnh cho từng product.
- Xây dựng Product List.
- Sử dụng `LazyColumn`.
- Xây dựng Product Detail.
- Điều hướng từ Product List sang Product Detail.
- Back navigation.
- Duy trì vị trí danh sách khi quay lại từ Detail.

## Main Files

```text
MainActivity.kt
Product.kt
ProductListScreen.kt
ProductDetailScreen.kt
```

---

## 2.4 Evidence

```text
evidence/stage 1/
```

Bao gồm các evidence về:

- Project configuration.
- Dataset 100 products.
- Product List.
- Product Detail.
- Navigation.

---

## 2.5 Result

Stage 1 hoàn thành phiên bản ứng dụng cơ bản với Product List và Product Detail hoạt động ổn định.

`LazyColumn` được sử dụng ngay từ giai đoạn đầu để ứng dụng có thể tiếp tục được kiểm thử với dataset lớn hơn ở các Stage sau.

---

# 3. Stage 2 – Async Image Loading & Error Handling

## Status

```text
COMPLETED
```

## 3.1 Objective

Stage 2 tập trung tích hợp Coil để tải hình ảnh từ Internet và xây dựng các trạng thái cần thiết trong quá trình image request.

---

## 3.2 Technical Background – Async Image Loading

Network request không nên thực hiện như một tác vụ nặng trực tiếp trên Main Thread.

Nếu Main Thread bị chiếm dụng quá lâu, giao diện có thể phản hồi chậm.

Project sử dụng Coil để thực hiện quá trình tải hình ảnh bất đồng bộ.

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

Trong thời gian hình ảnh đang tải, người dùng vẫn có thể tương tác với giao diện như scroll hoặc chuyển màn hình.

---

## 3.3 Technical Background – Placeholder & Error Handling

Trong quá trình tải:

```text
Image Request
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

Placeholder và Error Handling giúp giao diện có trạng thái rõ ràng khi:

- Network chậm.
- URL không hợp lệ.
- Server lỗi.
- Request thất bại.
- Thiết bị offline.

---

## 3.4 Implemented

- Coil 3.
- Coil Compose.
- Network image loading.
- `AsyncImage`.
- `ImageRequest`.
- Async image loading.
- Loading placeholder.
- Error placeholder.
- Error handling.
- Image loading cho Product List.
- Image loading cho Product Detail.

Dependencies:

```kotlin
implementation(libs.coil.compose)
implementation(libs.coil.network.okhttp)
```

---

## 3.5 Testing

Loading state được kiểm tra theo quy trình:

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

Error state được kiểm tra bằng URL không hợp lệ.

Các trạng thái được xác nhận:

```text
Loading
Success
Failure
```

---

## 3.6 Evidence

```text
evidence/stage 2/
```

Evidence bao gồm:

- Coil dependencies.
- AsyncImage implementation.
- Detail image implementation.
- Loading placeholder.
- Error placeholder.

---

## 3.7 Result

Stage 2 hoàn thành image-loading pipeline cơ bản.

Ứng dụng có thể tải hình ảnh bất đồng bộ và xử lý Loading / Success / Error mà không làm gián đoạn hoạt động chính của giao diện.

---

# 4. Stage 3 – Image Optimization: BASELINE vs OPTIMIZED

## Status

```text
COMPLETED
```

## 4.1 Objective

Stage 3 tập trung nghiên cứu và triển khai các kỹ thuật tối ưu hình ảnh.

Hai configuration được xây dựng:

```text
BASELINE
    ↓
Before Optimization

        vs

OPTIMIZED
    ↓
After Optimization
```

BASELINE đóng vai trò làm mốc so sánh.

OPTIMIZED áp dụng các chiến lược xử lý ảnh phù hợp hơn.

---

## 4.2 Technical Background – Memory Cache

Memory Cache cho phép dữ liệu hình ảnh có thể được tái sử dụng trực tiếp từ bộ nhớ.

```text
Request Image
      ↓
Check Memory Cache
    ↙          ↘
  HIT          MISS
   ↓             ↓
Reuse Image   Continue Loading
```

Ưu điểm:

- Hình ảnh có thể được tái sử dụng nhanh.
- Giảm các thao tác request/decode lại không cần thiết.

Tuy nhiên, cache cũng có thể giữ dữ liệu trong RAM.

Do đó:

> Memory cao hơn tại một Profiler snapshot không tự động đồng nghĩa với hiệu năng tổng thể kém hơn.

---

## 4.3 Technical Background – Disk Cache

Disk Cache lưu dữ liệu hình ảnh trên bộ nhớ thiết bị.

```text
Memory Cache
→ nhanh
→ nằm trong RAM

Disk Cache
→ chậm hơn Memory Cache
→ lưu trên storage
→ có thể duy trì dữ liệu lâu hơn
```

Disk Cache có ý nghĩa khi:

- Reload hình ảnh.
- Mở lại nội dung đã tải.
- Network không ổn định.
- Thiết bị offline nhưng dữ liệu cần thiết đã được cache.

Ảnh chưa từng được cache vẫn cần network để tải.

---

## 4.4 Technical Background – Resize / Downsampling

Ảnh tải từ Internet có thể có độ phân giải lớn hơn nhiều so với kích thước thực tế cần hiển thị.

Ví dụ:

```text
Large Source Image
       ↓
Small UI Thumbnail
```

Nếu decode ảnh ở kích thước lớn không cần thiết, ứng dụng có thể sử dụng nhiều bộ nhớ hơn.

OPTIMIZED vì vậy sử dụng kích thước request phù hợp với UI:

```text
Product List
→ Smaller requested image

Product Detail
→ Larger requested image
```

Mục tiêu:

- Hạn chế oversized decode.
- Giảm dữ liệu hình ảnh cần xử lý.
- Sử dụng bộ nhớ phù hợp hơn với vùng hiển thị.

---

## 4.5 BASELINE Implementation

BASELINE tạo request cơ bản:

```kotlin
ImageRequest.Builder(context)
    .data(imageUrl)
    .build()
```

BASELINE được sử dụng làm mốc để đánh giá trạng thái trước khi bổ sung các configuration tối ưu.

---

## 4.6 OPTIMIZED Implementation

OPTIMIZED sử dụng:

```kotlin
ImageRequest.Builder(context)
    .data(imageUrl)
    .size(width, height)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .diskCachePolicy(CachePolicy.ENABLED)
    .build()
```

Các kỹ thuật chính:

- Explicit image size.
- Memory Cache.
- Disk Cache.
- Async Loading.
- Lazy Loading.
- Loading / Error Handling.

---

## 4.7 ImageMode & ImageRequestFactory

Hai mode:

```kotlin
enum class ImageMode {
    BASELINE,
    OPTIMIZED
}
```

Request được tập trung trong `ImageRequestFactory.kt`:

```text
Image URL
    ↓
ImageRequestFactory
    ↓
Check ImageMode
   ↙         ↘
BASELINE   OPTIMIZED
   ↓           ↓
Basic      Size + Cache
Request       Request
   ↘           ↙
        Coil
          ↓
      AsyncImage
```

Việc sử dụng cùng một project giúp:

- Giữ cùng UI.
- Giữ cùng dataset.
- Giữ cùng image source.
- Giữ cùng functionality.
- Chuyển mode nhanh.
- Hạn chế sai lệch khi performance testing.

---

## 4.8 Evidence

```text
evidence/stage 3/
```

Evidence bao gồm:

- BASELINE configuration.
- OPTIMIZED configuration.
- `ImageConfig`.
- `ImageRequestFactory`.
- Source comparison.

---

## 4.9 Result

Stage 3 hoàn thành hai configuration có thể tái hiện trong cùng project.

Ứng dụng sẵn sàng chuyển sang giai đoạn thực nghiệm để đánh giá các kỹ thuật tối ưu bằng evidence thực tế.

---

# 5. Stage 4 – Performance Testing

## Status

```text
COMPLETED
```

## 5.1 Objective

Stage 4 tập trung kiểm tra BASELINE và OPTIMIZED trong nhiều tình huống khác nhau.

Việc đánh giá không chỉ dựa trên Memory mà còn xem xét:

- Image loading behavior.
- Cache behavior.
- UI responsiveness.
- Error handling.
- Offline behavior.
- Stability.
- Scalability.

---

## 5.2 Test Environment

```text
Device       : Pixel 5 Emulator
Android      : Android 11
API          : 30
Image Source : Picsum Photos
Profiler     : Android Studio Profiler
```

Các điều kiện chính được cố gắng giữ giống nhau:

- Emulator.
- Android version.
- API level.
- Dataset.
- Image source.
- UI.
- Test procedure.
- Profiler.

---

## 5.3 TC01 – First Load

**Purpose:** Đánh giá trạng thái khi ứng dụng tải hình ảnh lần đầu.

```text
Clear App Data
      ↓
Select Image Mode
      ↓
Run Application
      ↓
Open Profiler
      ↓
Load Images
```

### Result Snapshot

```text
BASELINE  = 110.9 MB
OPTIMIZED = 152.8 MB
```

OPTIMIZED có Memory snapshot cao hơn trong lần đo này.

Kết quả cho thấy optimization không thể được đánh giá chỉ dựa trên một snapshot vì cache và runtime state có thể ảnh hưởng đến Memory.

Evidence:

```text
evidence/stage 4/TC01_First_Load/
```

---

## 5.4 TC02 – Cache Hit

**Purpose:** Đánh giá khả năng tái sử dụng hình ảnh đã được tải.

### Result Snapshot

```text
BASELINE  = 114.3 MB
OPTIMIZED = 131.6 MB
```

OPTIMIZED có Memory snapshot cao hơn, nhưng dữ liệu cache có thể được giữ lại để hỗ trợ reuse.

Điều này thể hiện trade-off giữa:

```text
Cache Retention
      ↕
Memory Usage
      ↕
Image Reuse
```

Evidence:

```text
evidence/stage 4/TC02_Cache_Hit/
```

---

## 5.5 TC03 – Fast Scroll

**Purpose:** Kiểm tra ứng dụng khi scroll nhanh qua danh sách hình ảnh.

### Result Snapshot

```text
BASELINE  = 114.2 MB
OPTIMIZED = 141.7 MB
```

Trong các lần test:

- UI vẫn phản hồi.
- Hình ảnh tiếp tục được tải.
- Placeholder hoạt động.
- Không ghi nhận crash.
- Không ghi nhận OutOfMemoryError.

Evidence:

```text
evidence/stage 4/TC03_Fast_Scroll/
```

---

## 5.6 TC04 – High Resolution

**Purpose:** Đánh giá việc xử lý hình ảnh lớn trên Product Detail.

### Result Snapshot

```text
BASELINE  = 120.3 MB
OPTIMIZED = 115.5 MB

Difference = -4.8 MB
            ≈ -4.0%
```

Ở snapshot này, OPTIMIZED có Memory thấp hơn BASELINE.

Kết quả cho thấy lợi ích của việc request hình ảnh với kích thước phù hợp hơn với vùng hiển thị.

Evidence:

```text
evidence/stage 4/TC04_High_Resolution/
```

---

## 5.7 TC05 – Offline

**Purpose:** Kiểm tra vai trò của cache khi thiết bị mất kết nối Internet.

```text
Load Images Online
       ↓
Disable Network
       ↓
Access Images Again
       ↓
Observe Cached / Uncached Images
```

### Result

Hình ảnh đã được cache có thể tiếp tục được sử dụng tùy trạng thái cache.

Hình chưa được cache:

```text
No Network
    ↓
Request Failure
    ↓
Error Placeholder
```

Cache không thể tạo dữ liệu cho một hình ảnh chưa từng được tải.

Evidence:

```text
evidence/stage 4/TC05_Offline/
```

---

## 5.8 TC06 – Bad URL / Slow Server

**Purpose:** Kiểm tra ứng dụng trong các điều kiện network không lý tưởng.

Đã kiểm tra:

- Bad URL.
- Slow network.
- Failed request.
- HTTP 503.

### Result

- Loading state hoạt động.
- Error state hoạt động.
- UI vẫn phản hồi.
- Không ghi nhận crash.

Evidence:

```text
evidence/stage 4/TC06_BadURL_SlowServer/
```

---

## 5.9 TC07 – Dataset Scaling

**Purpose:** Đánh giá ứng dụng khi dataset tăng:

```text
100 → 500 → 1000 products
```

### Results

| Dataset | BASELINE | OPTIMIZED | Difference |
|---|---:|---:|---:|
| 100 | 130.4 MB | 117.4 MB | -13.0 MB (-10.0%) |
| 500 | 137.7 MB | 136.4 MB | -1.3 MB (-0.9%) |
| 1000 | 136.2 MB | 128.7 MB | -7.5 MB (-5.5%) |

Trong cả ba snapshot:

```text
OPTIMIZED < BASELINE
```

Ứng dụng đã được kiểm tra đến 1000 products mà không ghi nhận OutOfMemoryError.

Các giá trị trên là **Profiler snapshots**, không phải average Memory usage của toàn bộ phiên test.

Memory cũng không tăng tuyến tính theo số lượng products do ảnh hưởng của LazyColumn, cache, Garbage Collection và runtime state.

Evidence:

```text
evidence/stage 4/TC07_Dataset_Scaling/
```

---

## 5.10 Stage 4 Result

Toàn bộ TC01–TC07 đã hoàn thành.

Stage 4 cho thấy không thể đánh giá optimization chỉ bằng câu hỏi:

```text
Which mode uses less RAM?
```

Mà cần xem xét đồng thời:

```text
Memory
+
Cache
+
Image Processing
+
Network Behavior
+
Stability
+
Scalability
```

---

# 6. Stage 5 – Final Analysis & Deliverables

## Status

```text
COMPLETED
```

## 6.1 Objective

Stage 5 là giai đoạn tổng hợp toàn bộ kết quả, đưa project về final configuration và hoàn thiện các deliverable.

Các công việc chính:

- TC08.
- Final performance analysis.
- Final configuration.
- APK.
- APK verification.
- Report.
- README.
- Evidence organization.
- Final Git repository.

---

## 6.2 TC08 – Before vs After Comparison

TC08 không chạy một testcase mới.

TC08 tổng hợp:

```text
TC01
TC02
TC03
TC04
TC05
TC06
TC07
```

để so sánh:

```text
BASELINE
   vs
OPTIMIZED
```

Evidence:

```text
evidence/stage 5/TC08_Before_After_Comparison/
├── TC08_01_Summary_Table.jpg
├── TC08_02_Memory_Comparison.png
└── TC08_03_Analysis_Conclusion.jpg
```

TC08 tổng hợp ba nhóm chính:

```text
Memory & Image Processing
Cache, Network & Stability
Scalability
```

---

## 6.3 Final Analysis

Kết quả cho thấy:

```text
TC01–TC03
OPTIMIZED Memory Snapshot > BASELINE
```

Trong khi:

```text
TC04
TC07-100
TC07-500
TC07-1000

OPTIMIZED Memory Snapshot < BASELINE
```

Do đó:

> OPTIMIZED không đồng nghĩa với việc luôn sử dụng ít RAM hơn tại mọi thời điểm.

Optimization được đánh giá dựa trên toàn bộ pipeline:

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
Error Handling
      ↓
Optimized Image Loading Pipeline
```

---

## 6.4 Final Configuration

Dataset được đưa về:

```kotlin
List(size = 100)
```

Final mode:

```kotlin
ImageMode.OPTIMIZED
```

100 products được sử dụng cho final application và demo.

500 và 1000 products chỉ được sử dụng trong TC07 – Dataset Scaling.

Evidence:

```text
evidence/stage 5/Final_Configuration/
├── Final_01_Optimized_Mode.png
└── Final_02_100_Products.png
```

---

## 6.5 Final APK

Final APK đã được build, cài đặt và kiểm tra.

```text
evidence/stage 5/APK/
├── ImageOptimizationApp.apk
├── Stage5_APK_Build_Success.png
├── Stage5_APK_Generated.png
├── Stage5_APK_Installation.png
└── Stage5_APK_Verification.png
```

Quy trình đã hoàn thành:

```text
Build APK
    ↓
Generate APK
    ↓
Install APK
    ↓
Open Application
    ↓
Verify Product List
    ↓
Verify Product Detail
    ↓
Final Verification
```

**Status: COMPLETED**

---

## 6.6 Final Report

Báo cáo cuối cùng đã được hoàn thiện với cấu trúc:

```text
Chapter 1 – Cơ sở lý thuyết
Chapter 2 – Phân tích và thiết kế
Chapter 3 – Cài đặt và tối ưu
Chapter 4 – Phương pháp và kịch bản thực nghiệm
Chapter 5 – Kết quả và phân tích hiệu năng
Chapter 6 – Sản phẩm và kết luận
References – Tài liệu tham khảo
```

Các hình ảnh sử dụng trong report được lưu tại:

```text
evidence/stage 5/Report/
```

**Status: COMPLETED**

---

## 6.7 README

`README.md` đã được hoàn thiện để hướng dẫn:

- Project overview.
- Clone repository.
- Build project.
- Run application.
- Sử dụng Product List / Product Detail.
- Chuyển BASELINE / OPTIMIZED.
- Thay đổi dataset.
- Performance testing.
- Evidence.
- APK.
- Demo Video.

Phân biệt:

```text
README.md
→ Hướng dẫn sử dụng / reproduce project

PROGRESS.md
→ Quá trình nghiên cứu / phát triển / kiểm thử
```

**Status: COMPLETED**

---

## 6.8 Final Evidence Structure

```text
evidence/
│
├── stage 1/
│   └── Basic Application
│
├── stage 2/
│   └── Coil / Loading / Error
│
├── stage 3/
│   └── BASELINE / OPTIMIZED
│
├── stage 4/
│   ├── TC01_First_Load/
│   ├── TC02_Cache_Hit/
│   ├── TC03_Fast_Scroll/
│   ├── TC04_High_Resolution/
│   ├── TC05_Offline/
│   ├── TC06_BadURL_SlowServer/
│   └── TC07_Dataset_Scaling/
│
└── stage 5/
    ├── APK/
    ├── Final_Configuration/
    ├── Report/
    └── TC08_Before_After_Comparison/
```

---

## 6.9 Video Demo

Video Demo là phần duy nhất chưa được gắn link vào repository.

Dự kiến video trình bày:

- Application startup.
- Product List.
- Product Detail.
- Loading state.
- Error state.
- BASELINE.
- OPTIMIZED.
- Performance testing.
- Android Studio Profiler.
- Final APK.

```text
YouTube Demo:
[LINK WILL BE UPDATED]
```

**Status: PENDING YOUTUBE LINK**

---

# 7. Final Deliverables

| Deliverable | Nội dung | Status |
|---|---|---|
| D1 | Theory / Technical Background | COMPLETED |
| D2 | Application & Solution Design | COMPLETED |
| D3 | Kotlin Source + Git Repository | COMPLETED |
| D4 | Image Loading / Caching Module | COMPLETED |
| D5 | BASELINE / OPTIMIZED | COMPLETED |
| D6 | Test Cases / Experimental Results | COMPLETED |
| D7 | Performance Analysis | COMPLETED |
| D8 | APK / Documentation | COMPLETED |
| Video | YouTube Demo | PENDING LINK |

---

# 8. Final Project Status

```text
Stage 1               → COMPLETED
Stage 2               → COMPLETED
Stage 3               → COMPLETED
Stage 4               → COMPLETED
Stage 5               → COMPLETED

Implementation        → COMPLETED
BASELINE               → COMPLETED
OPTIMIZED              → COMPLETED

TC01–TC07             → COMPLETED
TC08                  → COMPLETED

Final Configuration   → COMPLETED
APK                   → COMPLETED
APK Verification      → COMPLETED
Evidence              → COMPLETED
Report                → COMPLETED
README                → COMPLETED
PROGRESS.md           → COMPLETED
Git Repository        → COMPLETED

Video Demo            → PENDING YOUTUBE LINK
```

Project hiện đã hoàn thành toàn bộ **nghiên cứu lý thuyết, implementation, optimization, testing, performance analysis, APK, evidence và documentation**.

Công việc cuối cùng còn lại:

```text
Record / Upload Video Demo
            ↓
Add YouTube Link
            ↓
Final Submission
```

---

# 9. Final Technical Conclusion

Project không nhằm chứng minh:

```text
OPTIMIZED always uses less RAM
```

Thay vào đó, project đánh giá tác động của một image-loading pipeline gồm:

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
Loading / Error Handling
```

Kết quả TC01–TC08 cho thấy optimization tạo ra các trade-off khác nhau tùy điều kiện kiểm thử.

Cache có thể làm Memory snapshot cao hơn tại một số thời điểm, trong khi resize/downsampling và controlled image requests có thể mang lại lợi ích trong các trường hợp khác.

Vì vậy, kết quả cuối cùng được đánh giá dựa trên **toàn bộ experimental evidence** thay vì một giá trị Memory riêng lẻ.

Final application:

```text
Dataset       : 100 products
Mode          : OPTIMIZED
APK           : COMPLETED
Report        : COMPLETED
Evidence      : COMPLETED
Documentation : COMPLETED
Video Demo    : PENDING YOUTUBE LINK
```
