<div align="center">
    <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/logo/coollobby.png">
</div>

<div align="center">
    <h1>Diversify your time in Lobby</h1>
</div>

<div align="center">
    <a><img src="https://i.imgur.com/ZvVP80t.png" width="40"></a>
    <a><img src="https://i.imgur.com/gwrUbOG.png" width="40"></a>
    <a><img src="https://i.imgur.com/Wi0gG3J.png" width="40"></a>
</div>

### <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/flags/ru.svg" width="15"> [Перейти на русский язык](readmes/README_EN.md)

## Description:

CoolLobby is a plugin that adds many useful commands for your lobby.

## Features:
* Support for 17 languages
* Multi-world synchronization
* Disable damage
* Disable hunger
* Disable time cycle
* Disable weather cycle
* Disable mob spawning
* Disable fire spread
* Set default time
* Set default weather
* Set default gamemode
* Set player level
* `/firework` command
* `/lighting` command
* `/spit` command
* `/fly` command
* Double Jump:
    * Set sound for double jump
    * Set effect for double jump
* Join and Leave Messages:
    * Join sound
    * Join title
* Enable/Disable any plugin feature
* Set cooldown for each command

## Commands
### /firework [/fw]:
* Launches a firework
* Permission: cool-lobby.fw

### /lighting [/lt]
* Summons lightning
* Permission: cool-lobby.lt

### /spit
* Spits at a player (Visual effect of Llama spit)
* Permission: cool-lobby.spit

### /fly
* Enables/Disables flight
* Syncs flight if the command can be used outside the Lobby
* Permission: cool-lobby.fly

## Double Jump
* Jump higher by double-tapping space
* Spawns particles during the jump
* Plays a sound during the jump
* Permission: cool-lobby.double_jump

## Plugin Permissions

| Permission             | Description                        |
|------------------------|------------------------------------|
| cool-lobby.*           | Grants access to all commands      |
| cool-lobby.reload      | Grants access to reload the plugin |
| cool-lobby.no_cooldown | Disables cooldown for commands     |
| cool-lobby.fw          | Access to /firework command        |
| cool-lobby.lt          | Access to /lighting command        |
| cool-lobby.spit        | Access to /spit command            |
| cool-lobby.fly         | Access to /fly command             |
| cool-lobby.double_jump | Access to double jump              |

## Supported Languages:
* ru-ru → Russian (Russia)
* en-us → English (USA)
* cs-cz → Czech (Czech Republic)
* de-de → German (Germany)
* es-es → Spanish (Spain)
* fi-fi → Finnish (Finland)
* fr-fr → French (France)
* id-id → Indonesian (Indonesia)
* it-it → Italian (Italy)
* ja-jp → Japanese (Japan)
* pl-pl → Polish (Poland)
* pt-br → Portuguese (Brazil)
* sk-sk → Slovak (Slovakia)
* tr-tr → Turkish (Turkey)
* uk-ua → Ukrainian (Ukraine)
* zn-cn → Simplified Chinese (China)
* zh-tw → Traditional Chinese (Taiwan)

## config.yml File
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

## What if I don't want a dedicated Lobby server?
The plugin remembers the player's level, game mode, and hunger so that when they leave the lobby (e.g., entering a game world), their previous settings are restored. This makes the plugin suitable for those who don’t want to dedicate an extra server for the Lobby.

## Screenshots
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

## Author Links:
* Contact: https://t.me/ilezovofficial
* Telegram Channel: https://t.me/ilezzov
* GitHub: https://github.com/ilezzov-code

## Support Development:
* DA: https://www.donationalerts.com/r/ilezzov_dev
* YooMoney: https://yoomoney.ru/fundraise/193CD8F13OH.250319
* Telegram Gift: https://t.me/ilezovofficial
* TON: UQCsMXvc2og_Sn91TJ8NsHTU9qyvx6qJPZ4TjEJTPdUY0Fes
* Card: 5536914188326494

## Have an issue or a question? Create new issue — https://github.com/ilezzov-code/CoolLobby/issues/new
