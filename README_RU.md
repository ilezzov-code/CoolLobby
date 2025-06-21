<div align="center">
    <img src="img/logo/coollobby.png" alt="">
    <h4>Поддерживаемые версии: Paper: 1.18.2 — 1.21.x</h4>
    <h1>Сделай ваше времяпровождение в Lobby веселее</h1>
</div>

<div align="center" content="">
    <a href="https://modrinth.com/plugin/coollobby">
        <img src="img/social/modrinth.svg" width="">
    </a>
</div>

### <a href="https://github.com/ilezzov-code/CoolLobby/tree/main"><img src="img/flags/en.svg" width=15px> Select English README.md</a>

##  <a>Оглавление</a>

- [Описание](#about)
- [Особенности](#features)
- [Config.yml](#config)
- [Команды](#commands)
- [Двойной прыжок](#double_jump)
- [Права](#permissions)
- [Синхронизация с несколькими мирами](#sync)
- [Система нескольких спавнов](#spawn-system)
- [Галерея](#pictures)


## <a id="about">Описание</a>

**CoolLobby** — это многофункциональный плагин для Lobby, который добавляет разнообразные возможности для улучшения игрового процесса путем веселых и интересных команд. Простая настройка, поддержка от разработчика, высокая оптимизация — всё в одном плагине!

## <a id="features">Особенности</a>

* **[🔥] Синхронизация с несколькими мирами** → [подробнее](#sync)
* Поддержка 2-х языков (Russian, English) + возможность создать собственный перевод
* Отключение урона
* Отключение голода
* Отключение смены погоды
* Установка стандартной погоды
* Отключение смены времени суток
* Установка стандартного времени суток
* Отключение спавна мобов
* Отключение распространения огня
* Установка стандартного игрового режима
* Установка стандартного уровня опыта
* 5 полезных и интересных команд → [подробнее](#commands)
* Система нескольких спавнов → [подробнее](#spawn-system)
* Сообщение при входе и выходе игрока
    * Title при входе игрока
    * Звук при вхоже игрока
* Телепортация на спавн при входе
* Двойной прыжок: → [подробнее](#double_jump)
    * Звук при двойном прыжке
    * Эффекты при двойном прыжке
* Полная кастомизация:
    * Отключение / Включение каждой возможности
    * Задержка на использование каждой команды
    * Подробная система прав
* Поддержка SQLite, MySQL, PostgreSQL

## <a id="config">Config.yml</a>

<details>
    <summary>Посмотреть config.yml</summary>

# ░█████╗░░█████╗░░█████╗░██╗░░░░░██╗░░░░░░█████╗░██████╗░██████╗░██╗░░░██╗
# ██╔══██╗██╔══██╗██╔══██╗██║░░░░░██║░░░░░██╔══██╗██╔══██╗██╔══██╗╚██╗░██╔╝
# ██║░░╚═╝██║░░██║██║░░██║██║░░░░░██║░░░░░██║░░██║██████╦╝██████╦╝░╚████╔╝░
# ██║░░██╗██║░░██║██║░░██║██║░░░░░██║░░░░░██║░░██║██╔══██╗██╔══██╗░░╚██╔╝░░
# ╚█████╔╝╚█████╔╝╚█████╔╝███████╗███████╗╚█████╔╝██████╦╝██████╦╝░░░██║░░░
# ░╚════╝░░╚════╝░░╚════╝░╚══════╝╚══════╝░╚════╝░╚═════╝░╚═════╝░░░░╚═╝░░░

# Developer / Разработчик: ILeZzoV

# Socials / Ссылки:
# • Contact with me / Связаться: https://t.me/ilezovofficial
# • Telegram Channel / Телеграм канал:
#    | RUS: https://t.me/ilezzov
#    | EN: https://t.me/ilezzov_en
# • GitHub: https://github.com/ilezzov-code

# By me coffee / Поддержать разработчика:
# • DA: https://www.donationalerts.com/r/ilezzov_dev
# • YooMoney: https://yoomoney.ru/to/4100118180919675
# • Telegram Gift: https://t.me/ilezovofficial
# • TON: UQCInXoHOJAlMpZ-8GIHqv1k0dg2E4pglKAIxOf3ia5xHmKV
# • BTC: 1KCM1QN9TNYRevvQD63UF81oBRSK67vCon
# • Card: 5536914188326494

# Supporting messages languages / Доступные языки сообщений:
# ru-RU, en-US
language: "en-US"

# Check the plugin for updates
# Проверять плагин на наличие обновлений
check_updates: true

# Enable logging
# Включить логирование
logging: true

lobby_settings:
  # The names of the lobby worlds
  # Имя lobby мира
  worlds:
    - lobby-1

  # Disable damage
  # Отключить получение урона
  disable_damage: true

  # Disable hunger
  # Отключить голод
  disable_hunger: true

  # Disable daylight cycle
  # Отключить смену времени суток
  disable_daylight_cycle:
    enable: true
    # true — /gamerule doDaylightCycle false, false — cyclic /time set (the sun will twitch)
    # true — /gamerule doDaylightCycle false, false — цикличное /time set (будет дергание солнца)
    use_gamerule: false

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

# /firework command. Permission: coollobby.fw
# /firework команда. Права: coollobby.fw
fw_command:
  # Enable the command / Включить команду
  enable: true
  # Use command only lobby world
  only_lobby: true
  # Cooldown command in seconds / Задержка на использование в секундах
  cooldown: 10

# /lighting command. Permission: coollobby.lt
# /lighting команда. Права: coollobby.lt
lt_command:
  # Enable the command / Включить команду
  enable: true
  # Use command only lobby world
  only_lobby: true
  # Cooldown command in seconds / Задержка на использование в секундах
  cooldown: 10

# /spit command. Permission: coollobby.spit
# /spit команда. Права: coollobby.spit
spit_command:
  # Enable the command / Включить команду
  enable: true
  # Use command only lobby world
  only_lobby: true
  # Cooldown command in seconds / Задержка на использование в секундах
  cooldown: 10

# /fly command. Permission: coollobby.fly
# /fly команда. Права: coollobby.fly
fly_command:
  # Enable the command / Включить команду
  enable: true
  # Use command only lobby world
  only_lobby: true
  # Cooldown command in seconds / Задержка на использование в секундах
  cooldown: 10

spawn_command:
  # Enable the command / Включить команду
  enable: true
  # Cooldown command in seconds / Задержка на использование в секундах
  cooldown: 10

# Join actions
# Действия при входе игрока
player_join:
  # Enable join message / Включить сообщение при входе
  join_message:
    enable: true

  # Enable global join message / Включить глобальное сообщение при входе
  global_join_message:
    enable: true

  # Join Title / Сообщение на экран при входе
  join_title:
    enable: true
    fade_in: 1 # Time when the title appeared in seconds / Время появления текста в секундах
    fade_out: 1 # Time when the title disappeared in seconds / Время исчезновения текста в секундах
    stay: 5 # Stay time in milliseconds / Время пребывания в миллисекундах

  # Join sound / Звук при входе
  join_sound:
    enable: true
    sound: ENTITY_EXPERIENCE_ORB_PICKUP

  # Teleport to spawn when logging in to the server / Телепортировать на спавн при входе на сервер
  teleport_to_spawn: true

# Leave actions
# Действия при выходе игрока
player_leave:
  # Enable global leave message / Включить глобальное сообщение о выходе игрока
  global_leave_message:
    enable: true

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

# Don't edit this
# Не редактируйте это
config_version: 1.2

</details>

## <a id="commands">Команды (/команда → /псевдоним1, /псевдоним2, ... ※ `право`)</a>

### /firework → /fw, /фейерверк ※ `coollobby.fw`

* Запустить красочный и неповторимый фейерверк

### /lighting → /lt, /молния ※ `coollobby.lt`

* Призвать молнию (без урона и поджога)

### /spit → /плюнуть ※ `coollobby.spit`

* Плюнуть в даль (Визуальный эффект плевка Ламы)

### /fly → /полет ※ `coollobby.fly`

* Включить / Выключить полет

### /spawn → /спавн ※ `coollobby.spawn`

* Телепортироваться на спавн

<details>
    <summary>Остальные /spawn команды</summary>

### /spawn {name} ※ `coollobby.spawn`

* Телепортироваться на спавн по его имени

### /spawn {player} ※ `coollobby.spawn.other_player_tp`

* Телепортировать указанного игрока на спавн

### /spawn {name} {player} ※ `coollobby.spawn.other_player_tp`

* Телепортировать указанного игрока на спавн по его имени


### /spawn help ※ `coollobby.spawn`

* Помощь по всем командам /spawn

### /spawn set ※ `coollobby.spawn.set`

* Установить новый спавн в текущем мире

### /spawn confirm ※ `coollobby.spawn.set`

* Подтвердить установку нового спавна в текущем мире

### /spawn remove ※ `coollobby.spawn.remove`

* Удалить спавн в текущем мире

### /spawn remove ※ `coollobby.spawn.remove`

* Удалить спавн по его имени

</details>

## <a id="double_jump">Двойной прыжок ※ `coollobby.double_jump`</a>

* Активируется по двойному нажатию на пробел
* Не работает при включенном /fly или игровой режим = Креатив
* Спавнит красивый след из частиц во время прыжка и проигрывает звук

## <a id="permissions">Все права плагина</a>

| Право                           | Описание                                               |
|---------------------------------|--------------------------------------------------------|
| coollobby.*                     | Доступ ко всем возможностям плагина                    |
| coollobby.reload                | Доступ к перезагрузке плагина /cl reload               |
| coollobby.no_cooldown           | Отключает задержку на использование какой-либо функции |
| coollobby.fw                    | Доступ к команде /firework                             |
| coollobby.lt                    | Доступ к команде /lighting                             |
| coollobby.spit                  | Доступ к команде /spit                                 |
| coollobby.fly                   | Доступ к команде /fly                                  |
| coollobby.double_jump           | Доступ к двойному прыжку                               |
| coollobby.spawn                 | Доступ к команде /spawn                                |
| coollobby.spawn.set             | Доступ к команде /spawn set                            |
| coollobby.spawn.remove          | Доступ к команде /spawn remove                         |
| coollobby.spawn.other.player_tp | Возможность телепортировать другого игрока на спавн    |
| coollobby.spawn.*               | Доступ ко всем командам /spawn                         |


## <a id="sync">Синхронизация с несколькими мирами 🔥</a>

Плагин уникален тем, что позволяет вам не выделять отдельный сервер под Lobby. Вы можете указать в конфигурации отдельные миры, которые будут считаться как Lobby, и тогда плагин будет запоминать уровень игрока, игровой режим, голод для того, чтобы при выходе из лобби, например в игровой мир, игроку был установлен его уровень опыта, его прежний игровой режим и уровень голода. Обратитесь к примерам ниже, чтобы понять как это работает: 

### Телепортация из обычного мира в Lobby:

Плагин сохраняет текущий уровень игрока, игровой режим и уровень голода и устанавливает значения уровня и игрового режима из конфигурации

```yml
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
```

<img src="img/gifs/teleportFromWorldToLobby.gif">

### Телепортация из Lobby в обычный мир:

Плагин возвращает игроку раннее сохраненные значения голода, уровня и игрокового режима

<img src="img/gifs/teleportFromLobbyToWorld.gif">

## <a id="spawn-system">Система нескольких спавнов</a>

Вы можете устанавливать спавн для каждого мира на вашем сервере. 

После установки телепортация на установленный спавн доступна по команде `/spawn {имя мира}`. 
При наличии нескольких спавнов, команда `/spawn` будет телепортировать игрока на случайный спавн

<img src="img/gifs/spawn_system.gif">

## <a id="pictures">Галерея</a>

### Фейерверк `/firework`

<div>
    <img src="img/gifs/firework.gif" width="200"> 
    <img src="img/screenshots/fw_1.png" width="200">
    <img src="img/screenshots/fw_2.png" width="200">
    <img src="img/screenshots/fw_3.png" width="200">
    <img src="img/screenshots/fw_4.png" width="200">
</div>

### Молния `/lighting`

<div>
    <img src="img/gifs/lighting.gif" width="200"> 
    <img src="img/screenshots/lt1.png" width="200">
    <img src="img/screenshots/lt2.png" width="200">
</div>

### Плевок `/spit`

<div>
    <img src="img/gifs/spit.gif" width="200"> 
    <img src="img/screenshots/spit.png" width="200">
    <img src="img/screenshots/spit2.png" width="200">
</div>

### Двойной прыжок

<div>
    <img src="img/gifs/double_jump.gif" width="200"> 
    <img src="img/screenshots/db1.png" width="200">
    <img src="img/screenshots/db2.png" width="200">
</div>

## <a id="license">Лицензия</a>

Этот проект распространяется под лицензией `GPL-3.0 License`. Подробнее см. в файле [LICENSE](LICENSE).
