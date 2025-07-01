<div align="center">
    <img src="img/logo/coollobby.png" alt="">
    <h4>Supported version: Paper: 1.18.2 — 1.21.x</h4>
    <h1>Make your Lobby experience more fun</h1>
</div>

<div align="center" content="">
    <a href="https://modrinth.com/plugin/coollobby">
        <img src="img/social/modrinth.svg" width="">
    </a>
</div>

### <a href="https://github.com/ilezzov-code/CoolLobby/blob/main/README_RU.md"><img src="img/flags/ru.svg" width=15px> Выбрать русский README.md</a>

##  <a>Table of Contents</a>

- [About](#about)
- [Features](#features)
- [Config.yml](#config)
- [Commands](#commands)
- [Double Jump](#double_jump)
- [Permissions](#permissions)
- [Multi-World Synchronization](#sync)
- [Multiple Spawn System](#spawn-system)
- [Gallery](#pictures)
- [Links](#links)
- [Buy me coffee](#donate)
- [Report a bug](https://github.com/ilezzov-code/CoolLobby/issues)


## <a id="about">About</a>

**CoolLobby** is a multifunctional Lobby plugin that adds various features to enhance gameplay with fun and interesting commands. Easy setup, developer support, high optimization — all in one plugin!

## <a id="features">Features</a>

* **[🔥] Multi-World Synchronization** → [more info](#sync)
* Support for 2 languages (Russian, English) + ability to create your own translation
* Disable damage
* Disable hunger
* Disable weather change
* Set default weather
* Disable time cycle
* Set default time
* Disable mob spawning
* Disable fire spread
* Set default game mode
* Set default experience level
* 5 useful and fun commands → [more info](#commands)
* Multi-spawn system → [more info](#spawn-system)
* Join and leave messages for players
    * Title message on join
    * Sound on join
* Teleport to spawn on join
* Double Jump: → [more info](#double-jump)
    * Sound on double jump
    * Effects on double jump
* Full customization:
    * Enable/disable each feature
    * Delay for each command
    * Detailed permission system
* Support for SQLite, MySQL, PostgreSQL

## <a id="config">Config.yml</a>

<details>
    <summary>View config.yml</summary>

```yaml
# ░█████╗░░█████╗░░█████╗░██╗░░░░░██╗░░░░░░█████╗░██████╗░██████╗░██╗░░░██╗
# ██╔══██╗██╔══██╗██╔══██╗██║░░░░░██║░░░░░██╔══██╗██╔══██╗██╔══██╗╚██╗░██╔╝
# ██║░░╚═╝██║░░██║██║░░██║██║░░░░░██║░░░░░██║░░██║██████╦╝██████╦╝░╚████╔╝░
# ██║░░██╗██║░░██║██║░░██║██║░░░░░██║░░░░░██║░░██║██╔══██╗██╔══██╗░░╚██╔╝░░
# ╚█████╔╝╚█████╔╝╚█████╔╝███████╗███████╗╚█████╔╝██████╦╝██████╦╝░░░██║░░░
# ░╚════╝░░╚════╝░░╚════╝░╚══════╝╚══════╝░╚════╝░╚═════╝░╚═════╝░░░░╚═╝░░░

# Developer / Разработчик: ILeZzoV

# Socials / Ссылки:
# • Contact with me / Связаться: https://t.me/ilezovofficial
# • Telegram Channel / Телеграм канал: https://t.me/ilezzov
# • GitHub: https://github.com/ilezzov-code

# By me coffee / Поддержать разработчика:
# • DA: https://www.donationalerts.com/r/ilezov
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
```

</details>

## <a id="commands">Commands (/cmd → /alias1, /alias2, ... ※ `permission`)</a>

### /firework → /fw, /firework ※ `coollobby.fw`

* Launch a colorful and unique fireworks display

### /lighting → /lt, /lightning ※ `coollobby.lt`

* Summon lightning (without damage or fire)

### /spit → /spit ※ `coollobby.spit`

* Spit far (visual effect of llama spitting)

### /fly → /fly ※ `coollobby.fly`

* Enable / Disable flight

### /spawn → /spawn ※ `coollobby.spawn`

* Teleport to spawn

<details>
    <summary>Other /spawn commands</summary>

### /spawn {name} ※ `coollobby.spawn`

* Teleport to spawn by its name

### /spawn {player} ※ `coollobby.spawn.other_player_tp`

* Teleport a specified player to spawn

### /spawn {name} {player} ※ `coollobby.spawn.other_player_tp`

* Teleport a specified player to spawn by name

### /spawn help ※ `coollobby.spawn`

* Help for all /spawn commands

### /spawn set ※ `coollobby.spawn.set`

* Set a new spawn point in the current world

### /spawn confirm ※ `coollobby.spawn.set`

* Confirm setting a new spawn point in the current world

### /spawn remove ※ `coollobby.spawn.remove`

* Remove spawn in the current world

### /spawn remove ※ `coollobby.spawn.remove`

* Remove spawn by its name

</details>

## <a id="double_jump">Double Jump ※ `coollobby.double_jump`</a>

* Activated by double pressing the spacebar
* Does not work if /fly is enabled or game mode is Creative
* Creates a beautiful particle trail during jump and plays a sound

## <a id="permissions">All plugin permissions</a>

| Право                           | Описание                                   |
|---------------------------------|--------------------------------------------|
| coollobby.*                     | Access to all plugin features              |
| coollobby.reload                | Access to reload plugin /cl reload         |
| coollobby.no_cooldown           | Disable cooldown for any feature           |
| coollobby.fw                    | Access to /firework command                |
| coollobby.lt                    | Access to /lighting command                |
| coollobby.spit                  | Access to /spit command                    |
| coollobby.fly                   | Access to /fly  command                    |
| coollobby.double_jump           | Access to double jump                      |
| coollobby.spawn                 | Access to /spawn command                   |
| coollobby.spawn.set             | Access to /spawn set command               |
| coollobby.spawn.remove          | Access to /spawn remove command            |
| coollobby.spawn.other.player_tp | Ability to teleport other players to spawn |
| coollobby.spawn.*               | Access to all /spawn commands              |


## <a id="sync">Multi-World Synchronization 🔥</a>

The plugin is unique in that it allows you to not dedicate a separate server for the Lobby. You can specify certain worlds in the configuration that will be considered as Lobby, and the plugin will remember the player's level, game mode, and hunger. When leaving the lobby, for example entering a game world, the plugin will restore the player's experience level, previous game mode, and hunger level. See the examples below:

### Teleporting from a normal world to Lobby:

The plugin saves the current player level, game mode, and hunger level, and applies the configured values when teleporting

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

### Teleporting from Lobby to a normal world:
The plugin restores the previously saved hunger, level, and game mod

<img src="img/gifs/teleportFromLobbyToWorld.gif">

## <a id="spawn-system">Multi-Spawn System</a>

You can set a spawn point for each world on your server.

After setting, teleporting to the spawn is available via `/spawn {world_name}`.
If multiple spawns are set, `/spawn` will teleport the player to a random spawn point.

<img src="img/gifs/spawn_system.gif">

## <a id="pictures">Gallery</a>

### Firework `/firework`

<div>
    <img src="img/gifs/firework.gif" width="200"> 
    <img src="img/screenshots/fw_1.png" width="200">
    <img src="img/screenshots/fw_2.png" width="200">
    <img src="img/screenshots/fw_3.png" width="200">
    <img src="img/screenshots/fw_4.png" width="200">
</div>

### Lighting `/lighting`

<div>
    <img src="img/gifs/lighting.gif" width="200"> 
    <img src="img/screenshots/lt1.png" width="200">
    <img src="img/screenshots/lt2.png" width="200">
</div>

### Spit `/spit`

<div>
    <img src="img/gifs/spit.gif" width="200"> 
    <img src="img/screenshots/spit.png" width="200">
    <img src="img/screenshots/spit2.png" width="200">
</div>

### Double jump

<div>
    <img src="img/gifs/double_jump.gif" width="200"> 
    <img src="img/screenshots/db1.png" width="200">
    <img src="img/screenshots/db2.png" width="200">
</div>

## <a id="links">Links</a>

* Contact: https://t.me/ilezovofficial
* Telegram Channel: https://t.me/ilezzov
* Modrinth: https://modrinth.com/plugin/coollobby

## <a id="donate">Buy me coffee</a>

* DA: https://www.donationalerts.com/r/ilezov
* YooMoney: https://yoomoney.ru/to/4100118180919675
* Telegram Gift: https://t.me/ilezovofficial
* TON: UQCInXoHOJAlMpZ-8GIHqv1k0dg2E4pglKAIxOf3ia5xHmKV
* BTC: 1KCM1QN9TNYRevvQD63UF81oBRSK67vCon
* Card: 5536914188326494

## Found an issue or have a question? Create a new issue — https://github.com/ilezzov-code/CoolLobby/issues/new

## <a id="license">License</a>

This project is distributed under the `GPL-3.0` License. See the [LICENSE](LICENSE.txt) file for details.