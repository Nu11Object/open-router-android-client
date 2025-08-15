![Logo](./assets/logo.png)

![Education Project](https://img.shields.io/badge/education_project-8A2BE2.svg?style=for-the-badge&logoColor=white)
![Android](https://img.shields.io/badge/Android_/_views-%237F52FF?style=for-the-badge&logo=android&logoColor=white)
![Room](https://img.shields.io/badge/room-%237F52FF.svg?style=for-the-badge&logoColor=white)
![Retrofit2](https://img.shields.io/badge/retrofit2-%237F52FF.svg?style=for-the-badge&logoColor=white)
![Dagger2](https://img.shields.io/badge/dagger2-%237F52FF.svg?style=for-the-badge&logoColor=white)
![Flow](https://img.shields.io/badge/Flow-%237F52FF.svg?style=for-the-badge&logoColor=white)

**OpenRouter::client** — Android-приложение для общения с нейросетями через сервис [OpenRouter.ai](https://openrouter.ai/). <br>Проект разработан в **учебных целях** для закрепления  навыков.

## Возможности 

- Отправка запросов в нейросеть через OpenRouter API
- Выбор и сохранение в избранное AI-моделей из каталога OpenRouter.
- Переключение режима контекста (использование истории чата в запросе)
- Сохранение истории сообщений в виде чата
- Использование кастомного API-ключа
- Сброс истории чата

## Скриншоты

![Screenshots](./assets/screenshots.png)
![Screenshots of messages](./assets/screenshots_messages.png)

## Планы по развитию проекта

Проект будет совершенствоваться по мере моего профессионального роста. В будущем планируются как точечные улучшения и исправления, так и внедрение современных подходов и технологий.

## Архитектура

Проект построен по принципам **Clean Architecture** и разделён на слои:

- **Presentation** (UI, Activity, Fragments, ViewModels)
- **Domain** (UseCases, Entities, Repository interfaces)
- **Data** (Room, Retrofit, Repositories, Mappers)
- **DI** (Component, Modules, Scope, Qualifiers)

Реализован MVVM-паттерн (ViewModel + Flow).
<br>Dependency Injection через Dagger2.

## Технологический стек

- **Kotlin**, **Android SDK**, **Coroutines**, **Flow**, **ViewBinding**
- **Room** (хранение истории чата и избранных AI-моделей)
- **Retrofit2** (сетевые запросы к OpenRouter API)
- **Dagger2** (внедрение зависимостей)
- **Markwon** (рендеринг markdown)

## Сборка и запуск

1. Склонируйте репозиторий:
```bash
  git clone git@github.com:Nu11Object/open-router-android-client.git
  cd open-router-android-client
```
2. Соберите проект и запустите приложение.
3. Добавьте свой OpenRouter API-ключ:
   - Перейдите на сайт [OpenRouter.ai](https://openrouter.ai/) и зарегистрируйтесь.
   - Создайте новый API-ключ в [настройках профиля](https://openrouter.ai/settings/keys).
   - Укажите его в настройках приложения:

![Screenshot API key](./assets/screenshot_api_key.png)
