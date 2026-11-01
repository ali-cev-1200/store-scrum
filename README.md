# Store — پروژهٔ آزمایشگاه مهندسی نرم‌افزار (اسکرام)

اپلیکیشن فروشگاهی اندروید با Jetpack Compose که به‌صورت **اسپرینت‌به‌اسپرینت** و با چارچوب **اسکرام** توسعه داده می‌شود.

## قابلیت‌ها

- ثبت‌نام، ورود و ماندگاری نشست
- صفحهٔ اصلی با اسلایدر تبلیغات، دسته‌ها و محصولات بر اساس برچسب
- کش آفلاین محصولات با Room
- صفحهٔ دسته‌بندی و جزئیات محصول با نظرات
- سبد خرید، ثبت سفارش و پرداخت با بازگشت از درگاه (Deep Link)
- پروفایل و ثبت آدرس

## فناوری‌ها

| بخش | ابزار |
|---|---|
| رابط کاربری | Jetpack Compose، Navigation Compose، Coil، Lottie |
| معماری | MVVM + Repository |
| شبکه | Retrofit، OkHttp، Gson |
| پایگاه داده | Room |
| تزریق وابستگی | Koin |
| تست | JUnit 4، MockK، kotlinx-coroutines-test |

## ساختار پروژه

```
app/src/main/java/com/ali1200/store/
├── di/            ماژول Koin
├── model/
│   ├── data/      مدل‌های داده
│   ├── db/        Room
│   ├── net/       Retrofit
│   └── repository/
├── ui/
│   ├── features/  صفحه‌ها و ViewModelها
│   └── theme/
└── util/
```

## اجرا

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
```

## مستندات اسکرام

| سند | توضیح |
|---|---|
| [فرایند اسکرام](docs/process.md) | نقش‌ها، رویدادها، مصنوعات و قرارداد گیت |
| [Product Backlog](docs/product-backlog.md) | User Storyها، معیار پذیرش، تخمین و بدهی‌های فنی |
| [رودمپ](docs/roadmap.md) | برنامهٔ اسپرینت‌ها، نقاط عطف و Velocity |
| [Definition of Done](docs/definition-of-done.md) | شرط Done بودن هر Story |
| [گزارش اسپرینت‌ها](docs/sprints) | هدف، تسک‌ها، تست‌ها، چالش‌ها و رترو هر اسپرینت |

هر اسپرینت در گیت با تگ `sprint-N` مشخص شده است.
