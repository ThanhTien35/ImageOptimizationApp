# ImageOptimizationApp – Tiến độ phát triển & Ghi chú kỹ thuật

Tài liệu này ghi lại toàn bộ quá trình **nghiên cứu, phát triển, tối ưu, kiểm thử và hoàn thiện sản phẩm** của project `ImageOptimizationApp`.

Khác với `README.md` tập trung vào hướng dẫn cài đặt, chạy ứng dụng và tái hiện project, `PROGRESS.md` trình bày chi tiết quá trình thực hiện theo từng Stage, bao gồm:

- Cơ sở lý thuyết và kỹ thuật được áp dụng.
- Mục tiêu của từng Stage.
- Các chức năng đã triển khai.
- Các kỹ thuật tối ưu hình ảnh.
- Phương pháp kiểm thử.
- Kết quả thực nghiệm.
- Evidence của từng giai đoạn.
- APK, Report và Video Demo.
- Trạng thái cuối cùng của project.

> **Project Status: COMPLETED**  
> **Stage 1 → Stage 5: COMPLETED**  
> **Video Demo: COMPLETED**

---

# 1. Tổng quan project

## 1.1 Thông tin project

| Thành phần | Thông tin |
|---|---|
| Project | ImageOptimizationApp |
| Platform | Android |
| Ngôn ngữ | Kotlin |
| UI Framework | Jetpack Compose |
| Image Loading Library | Coil 3 |
| Minimum SDK | API 24 |
| Compile / Target SDK | API 37 |
| Thiết bị kiểm thử chính | Pixel 5 Emulator |
| Android kiểm thử | Android 11 – API 30 |
| Nguồn hình ảnh | Picsum Photos |
| Dataset chính | 100 products |
| Dataset kiểm thử mở rộng | 100 / 500 / 1000 products |
| Final Mode | OPTIMIZED |
| Final APK | COMPLETED |
| Video Demo | COMPLETED |

---

## 1.2 Mục tiêu

Project tập trung nghiên cứu và áp dụng các kỹ thuật liên quan đến tải và tối ưu hình ảnh trong ứng dụng Android:

- Async Image Loading.
- Lazy Loading.
- Memory Cache.
- Disk Cache.
- Resize / Downsampling.
- Loading Placeholder.
- Error Handling.
- Image Request Management.
- Dataset Scaling.

Mục tiêu không chỉ là hiển thị được hình ảnh từ Internet mà còn đánh giá hoạt động của ứng dụng trong các điều kiện khác nhau liên quan đến:

- Memory usage.
- Image loading behavior.
- Cache behavior.
- High-resolution image processing.
- Network / error handling.
- Application stability.
- Dataset scalability.

---

## 1.3 Quy trình phát triển

```text
Stage 1
Basic Application & Lazy Loading
        ↓
Stage 2
Async Image Loading & Error Handling
        ↓
Stage 3
Image Optimization
BASELINE vs OPTIMIZED
        ↓
Stage 4
Performance Testing
TC01 → TC07
        ↓
Stage 5
Final Analysis & Deliverables
TC08 + APK + Report + Video
```

| Stage | Nội dung | Trạng thái |
|---|---|---|
| Stage 1 | Basic Application & Lazy Loading | COMPLETED |
| Stage 2 | Async Image Loading & Error Handling | COMPLETED |
| Stage 3 | Image Optimization – BASELINE vs OPTIMIZED | COMPLETED |
| Stage 4 | Performance Testing – TC01 đến TC07 | COMPLETED |
| Stage 5 | Final Analysis, TC08 & Deliverables | COMPLETED |
| Video Demo | Demo sản phẩm cuối cùng | COMPLETED |

---

# 2. Stage 1 – Xây dựng ứng dụng cơ bản & Lazy Loading

## Trạng thái

```text
COMPLETED
```

## 2.1 Mục tiêu

Stage 1 xây dựng phiên bản cơ bản của ứng dụng trước khi triển khai các kỹ thuật tải và tối ưu hình ảnh.

Các thành phần chính gồm:

- Product data.
- Product List.
- Product Detail.
- Navigation.
- Back navigation.
- Scroll state.
- Danh sách có khả năng xử lý dataset lớn.

Stage 1 đóng vai trò nền tảng cho toàn bộ project:

```text
Basic Application
        ↓
Image Loading
        ↓
Image Optimization
        ↓
Performance Testing
```

---

## 2.2 Cơ sở lý thuyết – Lazy Loading

Danh sách sản phẩm được xây dựng bằng:

```kotlin
LazyColumn
```

Trong Jetpack Compose, `LazyColumn` phù hợp với danh sách dài vì các item được compose theo nhu cầu hiển thị thay vì xử lý toàn bộ danh sách trên giao diện cùng một lúc.

Luồng cơ bản:

```text
Large Dataset
      ↓
LazyColumn
      ↓
Visible Items
      ↓
Compose when needed
```

Điều này đặc biệt quan trọng khi quy mô dữ liệu tăng:

```text
100
 ↓
500
 ↓
1000 products
```

Lazy Loading giúp ứng dụng xử lý danh sách lớn hợp lý hơn và tạo nền tảng cho việc kiểm thử Dataset Scaling ở các Stage sau.

---

## 2.3 Chức năng đã triển khai

- Khởi tạo Android project bằng Kotlin.
- Sử dụng Jetpack Compose.
- Xây dựng `Product` model.
- Tạo dataset sản phẩm.
- Tạo URL hình ảnh cho từng product.
- Xây dựng Product List.
- Sử dụng `LazyColumn`.
- Xây dựng Product Detail.
- Điều hướng từ Product List sang Product Detail.
- Back navigation.
- Duy trì vị trí danh sách khi quay lại từ Detail.

Các file chính:

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

Bao gồm:

- Project configuration.
- Dataset.
- Product List.
- Product Detail.
- Navigation.
- Giao diện ứng dụng cơ bản.

---

## 2.5 Kết quả Stage 1

Stage 1 hoàn thành phiên bản ứng dụng cơ bản với Product List và Product Detail hoạt động ổn định.

`LazyColumn` được sử dụng ngay từ giai đoạn đầu để ứng dụng có thể tiếp tục được kiểm thử với dataset lớn hơn trong các Stage sau.

**Status: COMPLETED**

---

# 3. Stage 2 – Async Image Loading & Error Handling

## Trạng thái

```text
COMPLETED
```

## 3.1 Mục tiêu

Stage 2 tập trung tích hợp **Coil 3** để tải hình ảnh từ Internet và xây dựng các trạng thái cần thiết trong quá trình image loading.

Các trạng thái chính:

```text
Loading
Success
Error
```

---

## 3.2 Cơ sở lý thuyết – Async Image Loading

Các tác vụ network và image loading không nên trở thành tác vụ nặng chặn Main Thread.

Nếu Main Thread bị chiếm dụng trong thời gian dài, giao diện có thể phản hồi chậm và ảnh hưởng đến trải nghiệm người dùng.

Project sử dụng Coil để hỗ trợ quá trình tải hình ảnh bất đồng bộ.

Luồng cơ bản:

```text
Image URL
    ↓
Image Request
    ↓
Asynchronous Loading
    ↓
Decode / Process
    ↓
Display Image
```

Trong quá trình tải hình ảnh, người dùng vẫn có thể tương tác với giao diện như:

- Scroll Product List.
- Mở Product Detail.
- Back về danh sách.
- Tương tác với các thành phần khác.

---

## 3.3 Cơ sở lý thuyết – Placeholder & Error Handling

Trong thời gian hình ảnh đang tải:

```text
Image Request
     ↓
Loading
     ↓
Placeholder
```

Khi tải thành công:

```text
Placeholder
     ↓
Real Image
```

Nếu request thất bại:

```text
Request Failure
      ↓
Error Placeholder
```

Loading và Error Placeholder giúp giao diện có trạng thái rõ ràng trong các trường hợp:

- Network chậm.
- URL không hợp lệ.
- Server gặp lỗi.
- Request thất bại.
- Thiết bị mất kết nối Internet.

---

## 3.4 Các thành phần đã triển khai

- Coil 3.
- Coil Compose.
- Coil Network OkHttp.
- Network image loading.
- `AsyncImage`.
- `ImageRequest`.
- Async image loading.
- Loading Placeholder.
- Error Placeholder.
- Error Handling.
- Image loading cho Product List.
- Image loading cho Product Detail.

Dependencies:

```kotlin
implementation(libs.coil.compose)
implementation(libs.coil.network.okhttp)
```

---

## 3.5 Kiểm tra

Loading state được kiểm tra thông qua quá trình tải hình ảnh từ network.

Error state được kiểm tra bằng các URL hoặc điều kiện request không thành công.

Các trạng thái chính được xác nhận:

```text
Loading
   ↓
Success

hoặc

Loading
   ↓
Failure
   ↓
Error Placeholder
```

---

## 3.6 Evidence

```text
evidence/stage 2/
```

Bao gồm:

- Coil dependencies.
- AsyncImage implementation.
- Product List image.
- Product Detail image.
- Loading Placeholder.
- Error Placeholder.

---

## 3.7 Kết quả Stage 2

Stage 2 hoàn thành image-loading pipeline cơ bản.

Ứng dụng có khả năng tải hình ảnh bất đồng bộ và xử lý các trạng thái Loading / Success / Error mà không làm gián đoạn chức năng chính của giao diện.

**Status: COMPLETED**

---

# 4. Stage 3 – Tối ưu hình ảnh: BASELINE vs OPTIMIZED

## Trạng thái

```text
COMPLETED
```

## 4.1 Mục tiêu

Stage 3 tập trung nghiên cứu và triển khai các kỹ thuật tối ưu quá trình tải và xử lý hình ảnh.

Hai configuration được xây dựng:

```text
BASELINE
Cấu hình tham chiếu

        vs

OPTIMIZED
Cấu hình tối ưu
```

Hai mode sử dụng:

- Cùng project.
- Cùng UI.
- Cùng dataset.
- Cùng image source.
- Cùng chức năng.

Điều này giúp hạn chế sự khác biệt không cần thiết trong quá trình so sánh.

---

## 4.2 BASELINE

BASELINE sử dụng `ImageRequest` cơ bản:

```kotlin
ImageRequest.Builder(context)
    .data(imageUrl)
    .build()
```

BASELINE chủ yếu dựa trên các thiết lập mặc định của Coil và được sử dụng làm cấu hình tham chiếu.

BASELINE không được xem là cấu hình “không có cache”, vì Coil đã có các cơ chế cache mặc định.

---

## 4.3 Cơ sở lý thuyết – Memory Cache

Memory Cache cho phép dữ liệu hình ảnh có thể được tái sử dụng từ bộ nhớ.

```text
Request Image
      ↓
Check Memory Cache
     ↙       ↘
   HIT       MISS
    ↓          ↓
Reuse       Continue
Image       Loading
```

Memory Cache có thể giúp:

- Tái sử dụng hình ảnh đã tải.
- Giảm các thao tác request/decode lại không cần thiết.
- Hỗ trợ quá trình hiển thị lại hình ảnh.

Tuy nhiên, dữ liệu được giữ trong cache cũng có thể ảnh hưởng đến lượng RAM đang được sử dụng.

Vì vậy:

> Một Memory snapshot cao hơn tại một thời điểm không tự động đồng nghĩa với hiệu năng tổng thể kém hơn.

---

## 4.4 Cơ sở lý thuyết – Disk Cache

Disk Cache lưu dữ liệu liên quan đến hình ảnh trên bộ nhớ thiết bị.

```text
Memory Cache
→ nằm trong RAM
→ truy cập nhanh

Disk Cache
→ nằm trên storage
→ có thể duy trì dữ liệu lâu hơn
```

Cache có ý nghĩa trong các trường hợp:

- Truy cập lại hình ảnh.
- Reload nội dung đã tải.
- Network không ổn định.
- Một số dữ liệu đã được cache trước khi thiết bị offline.

Tuy nhiên, hình ảnh chưa từng được tải/cache vẫn cần network.

Việc ứng dụng hiển thị lại được một hình ảnh khi offline chỉ cho thấy dữ liệu phù hợp có thể được tái sử dụng từ cache; không nên chỉ dựa vào quan sát giao diện để kết luận chính xác dữ liệu đến từ Memory Cache hay Disk Cache.

---

## 4.5 Cơ sở lý thuyết – Resize / Downsampling

Hình ảnh từ Internet có thể có độ phân giải lớn hơn kích thước thực tế cần hiển thị.

Ví dụ:

```text
Large Source Image
        ↓
Small UI Thumbnail
```

Nếu xử lý ảnh lớn hơn nhiều so với vùng hiển thị, ứng dụng có thể sử dụng thêm tài nguyên không cần thiết.

Vì vậy, OPTIMIZED chủ động xác định target size của request:

```text
Product List
→ Smaller target size

Product Detail
→ Larger target size
```

Mục tiêu:

- Chủ động kiểm soát kích thước request.
- Hạn chế xử lý ảnh lớn hơn nhu cầu hiển thị.
- Sử dụng bộ nhớ phù hợp hơn với UI.

---

## 4.6 OPTIMIZED

OPTIMIZED sử dụng:

```kotlin
ImageRequest.Builder(context)
    .data(imageUrl)
    .size(width, height)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .diskCachePolicy(CachePolicy.ENABLED)
    .build()
```

Các thành phần chính:

- Explicit image size.
- Memory Cache policy.
- Disk Cache policy.
- Async Loading.
- Lazy Loading.
- Loading Placeholder.
- Error Handling.

Việc khai báo cache policy trong OPTIMIZED nhằm làm cấu hình `ImageRequest` rõ ràng và có thể kiểm soát hơn, không có nghĩa BASELINE hoàn toàn không sử dụng cache.

---

## 4.7 ImageMode & ImageRequestFactory

Hai mode:

```kotlin
enum class ImageMode {
    BASELINE,
    OPTIMIZED
}
```

Request được tập trung trong:

```text
ImageRequestFactory.kt
```

Luồng:

```text
Image URL
    ↓
ImageRequestFactory
    ↓
Check ImageMode
   ↙          ↘
BASELINE    OPTIMIZED
   ↓            ↓
Default      Explicit Size
Request      + Cache Policy
   ↘            ↙
        Coil
          ↓
      AsyncImage
```

Cấu trúc này giúp:

- Giữ cùng UI.
- Giữ cùng dataset.
- Giữ cùng image source.
- Giữ cùng functionality.
- Chuyển mode thuận tiện.
- Hạn chế sai lệch khi kiểm thử.

---

## 4.8 Evidence

```text
evidence/stage 3/
```

Bao gồm:

- BASELINE configuration.
- OPTIMIZED configuration.
- `ImageConfig`.
- `ImageRequestFactory`.
- Source comparison.

---

## 4.9 Kết quả Stage 3

Stage 3 hoàn thành hai configuration BASELINE và OPTIMIZED trong cùng một project.

Ứng dụng sẵn sàng chuyển sang giai đoạn kiểm thử để đánh giá hoạt động của hai configuration bằng evidence thực tế.

**Status: COMPLETED**

---

# 5. Stage 4 – Performance Testing

## Trạng thái

```text
COMPLETED
```

## 5.1 Mục tiêu

Stage 4 tập trung kiểm tra BASELINE và OPTIMIZED trong nhiều điều kiện khác nhau.

Việc đánh giá không chỉ dựa trên Memory mà còn xem xét:

- Image loading behavior.
- Cache behavior.
- UI responsiveness.
- Error handling.
- Offline behavior.
- Stability.
- Dataset scalability.

---

## 5.2 Cơ sở lý thuyết – Performance Testing

Hiệu năng của image-loading pipeline không nên được đánh giá chỉ bằng một chỉ số duy nhất.

Một hệ thống tải ảnh có thể chịu ảnh hưởng đồng thời bởi:

```text
Image Request
      +
Cache State
      +
Decode / Processing
      +
Garbage Collection
      +
Network
      +
UI State
      ↓
Observed Performance
```

Đặc biệt, Memory được ghi nhận bằng Android Studio Profiler có thể thay đổi tùy thời điểm.

Do đó, các giá trị Memory trong project được xem là:

> **Profiler snapshots tại thời điểm kiểm thử, không phải average memory usage của toàn bộ phiên chạy.**

---

## 5.3 Môi trường kiểm thử

```text
Device       : Pixel 5 Emulator
Android      : Android 11
API          : 30
Image Source : Picsum Photos
Profiler     : Android Studio Profiler
```

Các điều kiện chính được giữ tương đối thống nhất:

- Emulator.
- Android version.
- API level.
- Dataset.
- Image source.
- UI.
- Test procedure.
- Profiler.

---

## 5.4 TC01 – First Load

**Mục đích:** kiểm tra ứng dụng khi tải hình ảnh lần đầu.

### Memory Snapshot

```text
BASELINE  = 110.9 MB
OPTIMIZED = 152.8 MB
```

Ở snapshot này, OPTIMIZED có Memory cao hơn BASELINE.

Kết quả cho thấy không thể kết luận hiệu quả của optimization chỉ dựa trên một Memory snapshot, vì cache, Garbage Collection và runtime state có thể ảnh hưởng đến kết quả.

Evidence:

```text
evidence/stage 4/TC01_First_Load/
```

---

## 5.5 TC02 – Cache Hit

**Mục đích:** kiểm tra khả năng tái sử dụng những hình ảnh đã được tải.

### Memory Snapshot

```text
BASELINE  = 114.3 MB
OPTIMIZED = 131.6 MB
```

Ở thời điểm đo này, OPTIMIZED có Memory snapshot cao hơn.

Cache có thể tạo ra trade-off giữa:

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

## 5.6 TC03 – Fast Scroll

**Mục đích:** kiểm tra ứng dụng khi người dùng cuộn nhanh qua danh sách hình ảnh.

### Memory Snapshot

```text
BASELINE  = 114.2 MB
OPTIMIZED = 141.7 MB
```

Trong quá trình kiểm thử:

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

## 5.7 TC04 – High Resolution

**Mục đích:** kiểm tra hình ảnh kích thước lớn tại Product Detail.

### Memory Snapshot

```text
BASELINE  = 120.3 MB
OPTIMIZED = 115.5 MB

Difference = -4.8 MB
           ≈ -4.0%
```

Ở snapshot này, OPTIMIZED ghi nhận mức Memory thấp hơn BASELINE.

Kết quả phù hợp với mục tiêu chủ động kiểm soát kích thước image request, tuy nhiên mức chênh lệch Memory vẫn có thể chịu ảnh hưởng bởi cache, Garbage Collection và runtime state tại thời điểm đo.

Evidence:

```text
evidence/stage 4/TC04_High_Resolution/
```

---

## 5.8 TC05 – Offline

**Mục đích:** kiểm tra hoạt động của ứng dụng khi mất kết nối Internet.

Quy trình:

```text
Load Images Online
       ↓
Disable Network
       ↓
Access Images Again
       ↓
Observe Cached / Uncached Images
```

### Kết quả

Những hình ảnh có dữ liệu phù hợp trong cache có thể tiếp tục được tái sử dụng tùy trạng thái cache.

Hình chưa được tải/cache:

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

## 5.9 TC06 – Bad URL / Slow Server

**Mục đích:** kiểm tra ứng dụng trong các điều kiện network không lý tưởng.

Các tình huống được kiểm tra:

- Bad URL.
- Slow network.
- Failed request.
- HTTP 503.

### Kết quả

- Loading state hoạt động.
- Error state hoạt động.
- UI vẫn phản hồi.
- Không ghi nhận crash.

Evidence:

```text
evidence/stage 4/TC06_BadURL_SlowServer/
```

---

## 5.10 TC07 – Dataset Scaling

**Mục đích:** kiểm tra ứng dụng khi dataset tăng:

```text
100 → 500 → 1000 products
```

### Kết quả

| Dataset | BASELINE | OPTIMIZED | Difference |
|---|---:|---:|---:|
| 100 | 130.4 MB | 117.4 MB | -13.0 MB (-10.0%) |
| 500 | 137.7 MB | 136.4 MB | -1.3 MB (-0.9%) |
| 1000 | 136.2 MB | 128.7 MB | -7.5 MB (-5.5%) |

Trong cả ba snapshot của TC07:

```text
OPTIMIZED < BASELINE
```

Ứng dụng đã được kiểm tra đến **1000 products** mà không ghi nhận OutOfMemoryError.

Các giá trị trên là **Profiler snapshots**, không phải Memory trung bình của toàn bộ phiên test.

Memory cũng không tăng tuyến tính theo số lượng product do ảnh hưởng của:

- LazyColumn.
- Cache.
- Garbage Collection.
- Runtime state.
- Các image request đang hoạt động.

Evidence:

```text
evidence/stage 4/TC07_Dataset_Scaling/
```

---

## 5.11 Kết quả Stage 4

Toàn bộ **TC01 → TC07** đã hoàn thành.

Stage 4 cho thấy optimization không thể được đánh giá đơn giản bằng:

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

**Status: COMPLETED**

---

# 6. Stage 5 – Phân tích cuối cùng & Hoàn thiện sản phẩm

## Trạng thái

```text
COMPLETED
```

## 6.1 Mục tiêu

Stage 5 là giai đoạn tổng hợp toàn bộ kết quả, đưa project về final configuration và hoàn thiện toàn bộ deliverable.

Các công việc chính:

- TC08.
- Final performance analysis.
- Final configuration.
- APK build.
- APK verification.
- Final Report.
- README.
- PROGRESS.
- Evidence organization.
- Final Git repository.
- Video Demo.

---

## 6.2 Cơ sở lý thuyết – Before vs After Analysis

Việc đánh giá optimization không nên chỉ dựa vào một testcase riêng lẻ.

Stage 5 sử dụng toàn bộ evidence từ:

```text
TC01
TC02
TC03
TC04
TC05
TC06
TC07
```

để tạo ra:

```text
TC08
Before vs After Comparison
```

TC08 không phải một testcase hoàn toàn mới mà là bước **tổng hợp và phân tích kết quả** giữa:

```text
BASELINE
   vs
OPTIMIZED
```

Ba nhóm chính được xem xét:

```text
Memory & Image Processing
Cache, Network & Stability
Dataset Scalability
```

---

## 6.3 TC08 – Before vs After Comparison

Evidence:

```text
evidence/stage 5/TC08_Before_After_Comparison/
├── TC08_01_Summary_Table.jpg
├── TC08_02_Memory_Comparison.png
└── TC08_03_Analysis_Conclusion.jpg
```

Kết quả tổng hợp cho thấy:

```text
TC01–TC03

OPTIMIZED Memory Snapshot
>
BASELINE Memory Snapshot
```

Trong khi:

```text
TC04
TC07-100
TC07-500
TC07-1000

OPTIMIZED Memory Snapshot
<
BASELINE Memory Snapshot
```

Điều này cho thấy:

> **OPTIMIZED không đồng nghĩa với việc luôn sử dụng ít RAM hơn tại mọi thời điểm.**

---

## 6.4 Phân tích cuối cùng

Optimization trong project được đánh giá dựa trên toàn bộ image-loading pipeline:

```text
Async Loading
      +
Lazy Loading
      +
Memory Cache
      +
Disk Cache
      +
Resize / Request Size
      +
Loading Placeholder
      +
Error Handling
      ↓
Optimized Image Loading Pipeline
```

Memory Cache có thể giữ lại dữ liệu để hỗ trợ image reuse và do đó Memory snapshot có thể cao hơn tại một số thời điểm.

Ngược lại, việc chủ động kiểm soát target size của image request có thể giúp hạn chế xử lý dữ liệu hình ảnh lớn hơn nhu cầu hiển thị trong một số tình huống.

Vì vậy, kết quả cuối cùng được đánh giá dựa trên **toàn bộ evidence**, thay vì chỉ một giá trị Memory riêng lẻ.

---

## 6.5 Final Configuration

Dataset cuối cùng:

```kotlin
List(size = 100)
```

Final mode:

```kotlin
ImageMode.OPTIMIZED
```

Cấu hình cuối:

```text
Dataset : 100 products
Mode    : OPTIMIZED
```

100 products được sử dụng cho final application và Video Demo.

500 và 1000 products được sử dụng trong TC07 – Dataset Scaling.

Evidence:

```text
evidence/stage 5/Final_Configuration/
├── Final_01_Optimized_Mode.png
└── Final_02_100_Products.png
```

**Status: COMPLETED**

---

## 6.6 Final APK

Final APK đã được:

```text
Build
  ↓
Generate
  ↓
Install
  ↓
Run
  ↓
Verify
```

Evidence:

```text
evidence/stage 5/APK/
├── ImageOptimizationApp.apk
├── Stage5_APK_Build_Success.png
├── Stage5_APK_Generated.png
├── Stage5_APK_Installation.png
└── Stage5_APK_Verification.png
```

Các chức năng chính đã được kiểm tra lại trên final APK:

- Application startup.
- Product List.
- Image loading.
- Product Detail.
- Back navigation.
- Final OPTIMIZED configuration.

**Status: COMPLETED**

---

## 6.7 Final Report

Báo cáo cuối cùng đã được hoàn thiện với cấu trúc:

```text
Chapter 1 – Cơ sở lý thuyết

Chapter 2 – Phân tích và thiết kế

Chapter 3 – Cài đặt và tối ưu ứng dụng

Chapter 4 – Phương pháp và kịch bản thực nghiệm

Chapter 5 – Kết quả và phân tích hiệu năng

Chapter 6 – Sản phẩm và kết luận

References – Tài liệu tham khảo
```

Report bao gồm:

- Cơ sở lý thuyết.
- Thiết kế ứng dụng.
- Image loading pipeline.
- BASELINE / OPTIMIZED.
- Test methodology.
- TC01 → TC08.
- Performance analysis.
- APK.
- Final conclusion.

Report được lưu tại:

```text
evidence/stage 5/Report/
```

**Status: COMPLETED**

---

## 6.8 README

`README.md` được sử dụng làm tài liệu hướng dẫn sử dụng và tái hiện project.

README bao gồm:

- Project overview.
- Công nghệ sử dụng.
- Clone repository.
- Build project.
- Run application.
- Product List / Product Detail.
- Chuyển BASELINE / OPTIMIZED.
- Thay đổi dataset.
- Performance testing.
- Evidence.
- APK.
- Video Demo.

Phân biệt:

```text
README.md
→ Hướng dẫn sử dụng / chạy / reproduce project

PROGRESS.md
→ Quá trình nghiên cứu / phát triển / tối ưu / kiểm thử
```

**Status: COMPLETED**

---

## 6.9 Video Demo

Video Demo cuối cùng đã được hoàn thiện.

Nội dung video giới thiệu:

- Thành viên nhóm và đề tài.
- ImageOptimizationApp.
- Product List.
- LazyColumn.
- Product Detail.
- Loading Placeholder.
- Error Placeholder.
- Coil Image Loading.
- BASELINE configuration.
- OPTIMIZED configuration.
- Memory Cache.
- Disk Cache.
- Các tình huống thực tế đã được kiểm thử.
- Final OPTIMIZED application.
- Final APK / project.

Video Demo được cung cấp thông qua phần **Demo Video** trong `README.md`.

```text
Video Demo
    ↓
Application Overview
    ↓
Main Features
    ↓
Image Loading & Optimization
    ↓
Final Product
```

**Status: COMPLETED**

---

## 6.10 Final Evidence Structure

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

# 7. Tổng hợp Deliverables

| Deliverable | Nội dung | Trạng thái |
|---|---|---|
| D1 | Theory / Technical Background | COMPLETED |
| D2 | Application & Solution Design | COMPLETED |
| D3 | Kotlin Source + Git Repository | COMPLETED |
| D4 | Image Loading / Caching Module | COMPLETED |
| D5 | BASELINE / OPTIMIZED | COMPLETED |
| D6 | Test Cases / Experimental Results | COMPLETED |
| D7 | Performance Analysis | COMPLETED |
| D8 | APK / Documentation / Video Demo | COMPLETED |

---

# 8. Trạng thái cuối cùng của project

```text
Stage 1               → COMPLETED
Stage 2               → COMPLETED
Stage 3               → COMPLETED
Stage 4               → COMPLETED
Stage 5               → COMPLETED

Implementation        → COMPLETED

BASELINE              → COMPLETED
OPTIMIZED             → COMPLETED

TC01–TC07             → COMPLETED
TC08                  → COMPLETED

Final Configuration   → COMPLETED

APK                   → COMPLETED
APK Verification      → COMPLETED

Evidence              → COMPLETED
Report                → COMPLETED
README                → COMPLETED
PROGRESS.md           → COMPLETED

Video Demo            → COMPLETED

Git Repository        → COMPLETED
```

Project đã hoàn thành toàn bộ:

```text
Research
   ↓
Implementation
   ↓
Optimization
   ↓
Performance Testing
   ↓
Final Analysis
   ↓
APK
   ↓
Report
   ↓
Video Demo
   ↓
Documentation
   ↓
Final Submission
```

---

# 9. Kết luận kỹ thuật

Project không nhằm chứng minh rằng:

```text
OPTIMIZED always uses less RAM
```

Thay vào đó, project nghiên cứu và đánh giá một image-loading pipeline gồm:

```text
Async Loading
      +
Lazy Loading
      +
Memory Cache
      +
Disk Cache
      +
Resize / Request Size
      +
Loading Placeholder
      +
Error Handling
```

Kết quả TC01–TC08 cho thấy các kỹ thuật tối ưu có thể tạo ra những trade-off khác nhau tùy điều kiện thực thi.

Cache có thể làm Memory snapshot cao hơn tại một số thời điểm do dữ liệu được giữ lại để phục vụ quá trình tái sử dụng.

Trong một số trường hợp khác, việc kiểm soát image request và target size có thể giúp giảm lượng dữ liệu hình ảnh cần xử lý.

Do đó, kết quả cuối cùng được đánh giá dựa trên:

```text
Memory
+
Cache Behavior
+
Image Processing
+
Network Behavior
+
Error Handling
+
Stability
+
Dataset Scalability
```

thay vì chỉ dựa trên một Memory snapshot riêng lẻ.

Final application:

```text
Dataset       : 100 products
Mode          : OPTIMIZED

APK           : COMPLETED
Report        : COMPLETED
Evidence      : COMPLETED
README        : COMPLETED
PROGRESS      : COMPLETED
Video Demo    : COMPLETED

PROJECT       : COMPLETED
```

---

# Final Status

> **ImageOptimizationApp đã hoàn thành toàn bộ 5 Stage.**

> **Final Configuration: 100 Products + OPTIMIZED Mode**

> **TC01 → TC08: COMPLETED**

> **APK: COMPLETED**

> **Report: COMPLETED**

> **Video Demo: COMPLETED**

> **Documentation: COMPLETED**

> **PROJECT STATUS: COMPLETED**
