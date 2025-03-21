<div align="center">
    <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/logo/coollobby.png"> 
</div>

<div align="center">
    <h1>Разнообразь ваше времяпровожение в Lobby</h1>
</div>

<div align="center">
    <a><img src="https://i.imgur.com/ZvVP80t.png" width="40"></a>
    <a><img src="https://i.imgur.com/gwrUbOG.png" width="40"></a>
    <a><img src="https://i.imgur.com/Wi0gG3J.png" width="40"></a>
</div>

### <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/flags/en.svg" width="15"> [Select English Version](readmes/README_EN.md)

## Описание:

CoolLobby — плагин, который добавит множество полезных команд для вашего лобби

## Возможности:
* Поддержка 17 языков
* Синхронизация с несколькими мирами
* Отключить получение урона
* Отключить голод
* Отключить смену времени суток
* Отключить смену погоды
* Отключить спавн мобов
* Отключить распространение огня
* Установить стандартное время суток
* Установить стандартную погоду
* Установить стандартный игровой режим
* Установить уровень игроку
* Команда `/firework`
* Команда `/lighting`
* Команда `/spit`
* Команда `/fly`
* Двойной прыжок:
    * Установить звук при двойном прыжке
    * Установить эффект при двойном прыжке
* Сообщение при входе и выходе:
    * Звук при входе
    * Title при входе
* Включить / Отключить любую функцию плагина
* Установить задержку на использование для каждой команды

## Команды
### /firework [/fw]:
* Запускает фейерверк
* Право: cool-lobby.fw

### /lighting [/lt]
* Призывает молнию
* Право: cool-lobby.lt

### /spit
* Плюет в игрока (Визуальный эффект плевка Ламы)
* Право: cool-lobby.spit

### /fly
* Включает / Выключает полет
* Синхронизирует полет, если команду можно использовать не только в Lobby
* Право: cool-lobby.fly

## Двойной прыжок
* Прыгните выше при нажатии двойного пробела
* Спавнит частицы во время прыжка
* Проигрывает звук во время прыжка
* Право: cool-lobby.double_jump

## Все права плагина

| Право                  | Описание                                   |
|------------------------|--------------------------------------------|
| cool-lobby.*           | Дает доступ ко всем командам               |
| cool-lobby.reload      | Дает доступ к перезагрузке плагина         |
| cool-lobby.no_cooldown | Отключает задержку на использование команд |
| cool-lobby.fw          | Доступ к команде /firework                 |
| cool-lobby.lt          | Доступ к команде /lighting                 |
| cool-lobby.spit        | Доступ к команде /spit                     |
| cool-lobby.fly         | Доступ к команде /fly                      |
| cool-lobby.double_jump | Доступ к двойному прыжку                   |

## Поддерживаемые языки:
* ru-ru → Русский (Россия)
* en-us → Английский (США)
* cs-cz → Чешский (Чехия)
* de-de → Немецкий (Германия)
* es-es → Испанский (Испания)
* fi-fi → Финский (Финляндия)
* fr-fr → Французский (Франция)
* id-id → Индонезийский (Индонезия)
* it-it → Итальянский (Италия)
* ja-jp → Японский (Япония)
* pl-pl → Польский (Польша)
* pt-br → Португальский (Бразилия)
* sk-sk → Словацкий (Словакия)
* tr-tr → Турецкий (Турция)
* uk-ua → Украинский (Украина)
* zn-cn → Упрощённый китайский (Китай)
* zh-tw → Традиционный китайский (Тайвань)

## Файл config.yml
```yml
language: "ru-RU"

# Check the plugin for updates
# Проверять плагин на наличие обновлений
check_updates: true

lobby_settings:
  # The name of the lobby world
  # Имя lobby мира
  world: "world"

  # Disable damage
  # Отключить получение урона
  disable_damage: true

  # Disable hunger
  # Отключить голод
  disable_hunger: true

  # Disable daylight cycle
  # Отключить смены времени суток
  disable_daylight_cycle: true

  # Disable mob spawning
  # Отключить спавн мобов
  disable_mob_spawning: true

  # Disable fire tick
  # Отключить распространение огня
  disable_fire_tick: true

  # Disable weather cycle
  # Отключить смену погоды
  disable_weather_cycle: true

  # Default time value
  # Стандартное время суток
  default_time:
    enable: true
    value: 1000

  # Default weather
  # Стандартная погода
  default_weather:
    enable: true
    type: CLEAR # CLEAR, RAIN, THUNDER

  # Default gamemode
  # Стандартный игровой режим
  default_gamemode:
    enable: true
    type: ADVENTURE

  # Player XP Level
  # Уровень игрока
  default_level:
    enable: true
    level: 2025

# /firework command. Permission: cool-lobby.fw
# /firework команда. Права: cool-lobby.fw
fw_command:
  # Enable the command / Включить команду
  enable: true
  # Use command only lobby world
  only_lobby: true
  # Cooldown command in seconds / Задержка на использование в секундах
  cooldown: 10

# /lighting command. Permission: cool-lobby.lt
# /lighting команда. Права: cool-lobby.lt
lt_command:
  # Enable the command / Включить команду
  enable: true
  # Use command only lobby world
  only_lobby: true
  # Cooldown command in seconds / Задержка на использование в секундах
  cooldown: 10

# /spit command. Permission: cool-lobby.spit
# /spit команда. Права: cool-lobby.spit
spit_command:
  # Enable the command / Включить команду
  enable: true
  # Use command only lobby world
  only_lobby: true
  # Cooldown command in seconds / Задержка на использование в секундах
  cooldown: 10

# /fly command. Permission: cool-lobby.fly
# /fly команда. Права: cool-lobby.fly
fly_command:
  # Enable the command / Включить команду
  enable: true
  # Use command only lobby world
  only_lobby: true
  # Cooldown command in seconds / Задержка на использование в секундах
  cooldown: 10

# Double Jump
# Двойной прыжок
double_jump:
  enable: true
  cooldown: 10
  # Use double jump only lobby world
  only_lobby: true
  double_jump_sound:
    enable: true
    sound: ENTITY_EXPERIENCE_ORB_PICKUP
  double_jump_particle:
    enable: true
    particle: FLAME

# Join actions
# Действия при входе игрока
player_join:
  join_message:
    enable: true
  global_join_message:
    enable: true
  join_title:
    enable: true
    fade_in: 1 # Time when the title appeared in seconds / Время появления текста в секундах
    fade_out: 1 # Time when the title disappeared in seconds / Время исчезновения текста в секундах
    stay: 5 # Stay time in milliseconds / Время пребывания в миллисекундах
  join_sound:
    enable: true
    sound: ENTITY_EXPERIENCE_ORB_PICKUP

# Leave actions
# Действия при выходе игрока
player_leave:
  global_leave_message:
    enable: true
    message: true

# Don't edit this
# Не редактируйте это
config_version: 1.0
```

## Что делать если я не хочу выделять отдельный сервер под Lobby
Плагин запоминает уровень игрока, игровой режим, голод для того, чтобы при выходе из лобби, например в игровой мир, игроку был установлен его уровень опыта, его прежний игровой режим и уровень голода. Таким образом плагин подойдет тем, кто не хочет выделять дополнительный сервер под Lobby

## Скриншоты
`/firework`
<div>
  <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/screenshots/fw_1.png" width="200">
  <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/screenshots/fw_2.png" width="200">
  <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/screenshots/fw_3.png" width="200">
  <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/screenshots/fw_4.png" width="200">
</div>

`/lighting`
<div>
  <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/screenshots/lt1.png" width="200">
  <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/screenshots/lt2.png" width="200">
</div>

`/spit`
<div>
  <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/screenshots/spit.png" width="200">
  <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/screenshots/spit2.png" width="200">
</div>

`Double Jump`
<div>
  <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/screenshots/db1.png" width="200">
  <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/screenshots/db2.png" width="200">
</div>

## Ссылки на автора:
* Связаться: https://t.me/ilezovofficial
* Телеграм канал: https://t.me/ilezzov
* GitHub: https://github.com/ilezzov-code

## Поддержите разработку:
* DA: https://www.donationalerts.com/r/ilezzov_dev
* YooMoney: https://yoomoney.ru/fundraise/193CD8F13OH.250319
* Telegram Gift: https://t.me/ilezovofficial
* TON: UQCsMXvc2og_Sn91TJ8NsHTU9qyvx6qJPZ4TjEJTPdUY0Fes
* Card: 5536914188326494

## Возникла ошибка или вопрос? Создайте новую тему — https://github.com/ilezzov-code/CoolLobby/issues/new
