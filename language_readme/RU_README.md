<div align="center">
    <img src="https://github.com/ilezzov-code/Plugin-Blank/blob/main/img/logo/pluginblank.png">
</div>

-----
<div align="center">
    <h1>Заготовка для вашего плагина Paper Minecraft</h1>
</div>
<div align="center">
    <a href="https://t.me/ilezovofficial">
    <img src="https://github.com/ilezzov-code/Plugin-Blank/blob/main/img/socials/contact_with_me.png" width="200"> 
    </a>
</div>
<div align="center">
    <a href="https://t.me/ilezzov">
    <img src="https://github.com/ilezzov-code/Plugin-Blank/blob/main/img/socials/tg_channel.png" width="200"> 
    </a>
</div>

### Выберите язык:
* <img src="https://github.com/ilezzov-code/Plugin-Blank/blob/main/img/flags/en.svg" width="15"> [English](https://github.com/ilezzov-code/Plugin-Blank)
* <img src="https://github.com/ilezzov-code/Plugin-Blank/blob/main/img/flags/ru.svg" width="15"> Русский

### Поддержать:
* DA: https://www.donationalerts.com/r/ilezzov_dev
* Подарком в Telegram: https://t.me/ilezovofficial
* По номеру карты: 5536914188326494

## 🔥 Особенности:

* Система файлов
* Проверка версии
* Поддержка устаревших видов форматирования текста
* Мультиязычность
* Система Плейсхолдеров

## Начало работы
Клонируйте мой проект к себе на компьютер при помощи Git или скачайте его с моего GitHub

## Система файлов

`Система файлов` позволяет облегчить работу с файла конфигурации (.yml) Теперь Вы легко можете создавать неограниченное количество файлов, а также редактировать их без перезапуска сервера.

### Создание файла
Для начала вам необходимо создать файл в папке `resources` и прописать все необходимые значения. После этого можно перейти в созданию:

```java
final PluginFile file = FileManager.new(fileName, filePath);
```

`fileName` — Имя файла в папке `resources`

`filePath` — Папка, в которой будет создан файл. Если указать "" файл будет создан в корневой папке вашего плагина


Метод `new` создаст файл и скопирует его содержимое из папки `resources`

#### Пример:

```java
final PluginFile file = new FileManager("config.yml", "");
```

Будет создан файл `config.yml` в папке вашего плагина: `plugins/yourPlugin/config.yml`

```java
final PluginFile file = new FileManager("ru-RU.yml", "languages/ru");
```

Будет создан файл `ru-RU.yml` в подпапке вашего плагина: `plugins/yourPlugin/languages/ru/ru-RU.yml

### Перезагрузка файла
Если файл был изменен, Вы можете загрузить новое содержимое, чтобы использовать его:

```java
file.reload();
```

### Получение значений из файла по ключу
Вы можете использовать один из шести методов для получения значений из файлов по их ключу:

```java
final PluginFile file = FileManager.new("config.yml", "");

        file.getString(key); //Получить строку
file.getInt(key); //Получить целое число
file.getDouble(key); //Получить дробное число
file.getObject(key); //Получить объект
file.getList(key); //Получить список
```

#### Пример:
config.yml
```yml
prefix: "Plugin Prefix"
messages:
  join-message: "Добро пожаловать на сервер"
```

```java
final PluginFile config = FileManager.new("config.yml", "");
final String prefix = config.getString("prefix");
final String joinMessage = config.getString("messages.join-message");

out.println(prefix); //Plugin Prefix
out.println(joinMessage); //Добро пожаловать на сервер
```

## Проверка версии
В плагин встроена система проверки обновлений. Если у вашего плагина выходит новое обновление, то пользователю будет выведено соответствующее сообщение

Для того, чтобы это работа, необходимо выполнить несколько шагов:

1) Создайте репозиторий вашего проекта на GitHub или публичный файл в интернете
2) Внесите в этот файл последнюю версию вашего плагина
3) Скопируйте ссылку на этот файл
4) Измените значение переменной `urlToFileVersion` в классе `Main`

Теперь при каждом запуске плагина, он будет сравнивать текущую версию плагина, с той, которую Вы указали в файле по ссылке. Если версии разные, будет выведено соответствующее сообщение

## Форматирование текста
Плагин поддерживает все виды форматирования сообщений.

`LEGACY` — Цвет через **& / §** и HEX через **&#rrggbb / §#rrggbb** или **&x&r&r&g&g&b&b / §x§r§r§g§g§b§b**

`LEGACY_ADVANCED` — Цвет и HEX через **&##rrggbb / §##rrggbb**

`MINI_MESSAGE` — Цвет через **<цвет>** Подробнее — https://docs.advntr.dev/minimessage/index.html

И все форматы доступные на https://www.birdflop.com/resources/rgb/

Вы можете использовать все форматы одновременно в одном сообщении. Плагин это поддерживает

### Использование
Создайте объект класса `LegacySerialize`

```java
final LegacySerialize legacySerialize = new LegacySerialize();
```

Получите `Component` вашего сообщение при помощи метода `serialize`

```java
final LegacySerialize legacySerialize = new LegacySerialize();
final Component component = legacySerialize.serialize(message);
```

Используйте полученный компонент по назначению

## Мультиязычность
В плагин встроена мультиязычность. Вы легко можете создавать переводы вашего плагина.

## Система Плейсхолдеров
В плагин встроена система Placeholders при помощи которой Вы легко можете изменять свои плейсхолдеры на нужные значения

### Использование
Создайте или получите строку с плейсхолдером:

```java
final String message = "{P}, добро пожаловать на сервер";
```

Создайте `HashMap` с вашим плейсхолдером:

```java
final HashMap<String, String> placeholders = new HashMap<>();
placeholders.put("{P}", player.getName());
```

Преобразуйте вашу строку через статический метод `Placeholder.replacePlaceholder()`

```java
final String newMessage = Placeholder.replacePlaceholder(message, placeholders); //Player name, добро пожаловать на сервер
```




