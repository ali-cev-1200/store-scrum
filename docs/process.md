# فرایند اسکرام در این پروژه

این پروژه به‌صورت فردی انجام می‌شود؛ بنابراین هر سه نقش اسکرام بر عهدهٔ یک نفر است و مدرس درس نقش **ذی‌نفع (Stakeholder)** را دارد.

## نقش‌ها

| نقش | مسئولیت در این پروژه |
|---|---|
| Product Owner | نوشتن و اولویت‌بندی Product Backlog، تعیین معیار پذیرش |
| Scrum Master | پایبندی به رویدادها، رفع موانع، ثبت رترو |
| Developer | پیاده‌سازی، نوشتن یونیت تست، Code Review شخصی |
| Stakeholder (مدرس) | دریافت گزارش در Sprint Review هفتگی و بازخورد |

## رویدادها

| رویداد | زمان | خروجی |
|---|---|---|
| Sprint Planning | ابتدای هر هفته (بعد از کلاس) | Sprint Goal و Sprint Backlog در فایل اسپرینت |
| Daily Scrum | روزهایی که روی پروژه کار می‌شود (۵ دقیقه، یادداشت شخصی) | به‌روزرسانی وضعیت تسک‌ها در برد |
| Sprint Review | جلسهٔ کلاس | ارائهٔ Increment و نتیجهٔ اسپرینت |
| Sprint Retrospective | بعد از Review | بخش «رترو» در فایل اسپرینت |
| Backlog Refinement | میانهٔ هفته | به‌روزرسانی `product-backlog.md` |

## مصنوعات (Artifacts)

| مصنوع | محل |
|---|---|
| Product Backlog | [`product-backlog.md`](product-backlog.md) |
| Sprint Backlog | [`sprints/sprint-N.md`](sprints) |
| Increment | تگ `sprint-N` در گیت |
| Definition of Done | [`definition-of-done.md`](definition-of-done.md) |
| رودمپ و Velocity | [`roadmap.md`](roadmap.md) |

## برد کار (Kanban Board)

ستون‌ها: `Backlog` → `Sprint Backlog` → `In Progress` → `Review / Test` → `Done`

هر User Story یک Issue در گیت‌هاب است و هر اسپرینت یک Milestone.

## قرارداد گیت

- هر اسپرینت با یک کامیت با پیام `Sprint N: ...` و تگ `sprint-N` بسته می‌شود.
- پیام کامیت شمارهٔ Storyها را دارد، مثلاً `US-06`.
