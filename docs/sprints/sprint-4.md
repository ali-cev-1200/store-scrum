# اسپرینت 4 — دسته‌بندی و زیرساخت تست ViewModel

**هدف اسپرینت:** کاربر محصولات هر دسته را ببیند و همهٔ ViewModelها قابل تست شوند.

## Sprint Backlog

| Story | SP | وضعیت |
|---|---|---|
| US-10 — (انتقالی) تست صفحهٔ اصلی | 5 | ✅ Done |
| US-14 — صفحهٔ دسته‌بندی | 3 | ✅ Done |
| US-26 — زیرساخت تست ViewModel | 2 | ✅ Done |

## تسک‌ها

- [x] ساخت `MainDispatcherRule` برای جایگزینی `Dispatchers.Main` در تست
- [x] استفاده از `InstantTaskExecutorRule` برای تست `LiveData`
- [x] نوشتن تست `MainViewModel` (بدهی اسپرینت 3)
- [x] نوشتن تست `SignInViewModel` (بدهی اسپرینت 2)
- [x] ساخت `CategoryViewModel` و `CategoryScreen` (خواندن محصولات دسته از Room)
- [x] اتصال لمس دسته در صفحهٔ اصلی به مسیر `categoryScreen/{categoryName}`

## یونیت تست‌ها

| فایل تست | چه چیزی را بررسی می‌کند |
|---|---|
| `MainDispatcherRule` | Rule مشترک برای همهٔ تست‌های ViewModel |
| `MainViewModelTest` | بارگذاری محصولات و تبلیغات (۱ تست؛ تست Badge سبد در اسپرینت 5 اضافه شد) |
| `CategoryViewModelTest` | نمایش فقط محصولات دستهٔ انتخاب‌شده (۱ تست) |
| `SignInViewModelTest` | ارسال ایمیل و رمز به Repository و برگرداندن نتیجه (۱ تست) |

## نتیجهٔ Sprint Review

- برنامه‌ریزی‌شده: **10 SP** — انجام‌شده: **10 SP**
- Increment: صفحهٔ دسته‌بندی؛ نقطهٔ عطف **M2 (کاربر و محصول)** کامل شد.

## چالش‌ها و راه‌حل

| چالش | راه‌حل |
|---|---|
| `viewModelScope` روی `Dispatchers.Main` اجرا می‌شود که در JVM وجود ندارد. | `MainDispatcherRule` با `Dispatchers.setMain(UnconfinedTestDispatcher())`. |
| `delay(1000)` در `MainViewModel` تست را کند می‌کرد. | با `runTest` و `advanceUntilIdle()` زمان مجازی جلو می‌رود و تست فوری اجرا می‌شود. |
| `MainViewModel` پارامتر `isNetConnected` می‌گیرد که Koin خودش نمی‌شناسد. | پارامتر هنگام ساخت ViewModel با `parametersOf` پاس داده می‌شود. |
| نام پکیج‌های `cart` و `category` در لایهٔ UI جابه‌جا است. | به‌عنوان TD-03 ثبت شد تا در اسپرینت 9 بررسی شود. |

## رترو

- **خوب بود:** پرداخت بدهی تست، کار اسپرینت‌های بعد را راحت کرد.
- **بهتر شود:** باید زیرساخت تست از اسپرینت 0 آماده می‌شد.
- **اقدام:** از این به بعد تست ViewModel در همان اسپرینت قابلیت نوشته شود.

## برنامهٔ اسپرینت بعد

اسپرینت 5: صفحهٔ جزئیات محصول، نظرات و افزودن به سبد خرید.
