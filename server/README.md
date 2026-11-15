# سرور DuniBazaar (PHP + MySQL)

سرور اصلی پروژه (`dunijet.ir`) از دسترس خارج شد، برای همین بک‌اند به‌صورت محلی روی **XAMPP** اجرا می‌شود.

| فایل | کار |
|---|---|
| `index.php` | همهٔ متدهای API (`getProducts`، `signIn`، `addToCart`، `submitOrder`، ...) |
| `pay.php` | صفحهٔ پرداخت آزمایشی (به‌جای درگاه واقعی) |
| `config.php` | اتصال به دیتابیس محلی |
| `database.sql` | دیتابیس (جدول‌ها و داده‌های تستی) |
| `uploads/` | عکس محصولات و تبلیغات |

## اجرا (یک بار)

1. XAMPP را نصب کن (`C:\xampp`) و در XAMPP Control Panel، **Apache** و **MySQL** را Start کن.
2. این پوشه را به htdocs وصل کن (در cmd):
   ```
   mklink /J C:\xampp\htdocs\dunibazaar <مسیر پروژه>\server
   ```
3. دیتابیس را بساز:
   ```
   C:\xampp\mysql\bin\mysql -u root -e "CREATE DATABASE dunibazaar CHARACTER SET utf8mb4"
   C:\xampp\mysql\bin\mysql -u root dunibazaar < database.sql
   ```
4. تست: `http://localhost/dunibazaar/getProducts` باید لیست محصولات را برگرداند.

اپ اندروید (روی شبیه‌ساز) به آدرس `http://10.0.2.2/dunibazaar/` وصل می‌شود؛ `10.0.2.2` همان کامپیوتر میزبان است.
