<div align="center">
    <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/logo/coollobby.png"> 
</div>

<div align="center">
    <h1>Make your Lobby time more exciting</h1>
</div>

<div align="center">
    <a><img src="https://i.imgur.com/ZvVP80t.png" width="40"></a>
    <a><img src="https://i.imgur.com/gwrUbOG.png" width="40"></a>
    <a><img src="https://i.imgur.com/Wi0gG3J.png" width="40"></a>
</div>

### <img src="https://raw.githubusercontent.com/ilezzov-code/CoolLobby/refs/heads/main/img/flags/ru.svg" width="15"> [Перейти на русскую версию](https://github.com/ilezzov-code/CoolLobby/blob/main/readmes/README_RU.md)

## Description:

CoolLobby is a plugin that adds many useful commands and features to your Lobby.

## Features:
* Support for 17 languages
* Multi-world synchronization
* Disable damage
* Disable hunger
* Disable daylight cycle
* Disable weather cycle
* Disable mob spawning
* Disable fire spread
* Set default time
* Set default weather
* Set default game mode
* Set player XP level
* `/firework` command
* `/lighting` command
* `/spit` command
* `/fly` command
* Double Jump:
  * Set sound on jump
  * Set particle on jump
* Join & Leave Events:
  * Sound on join
  * Title on join
* Enable/Disable any feature
* Per-command cooldown

## Commands

### /firework [/fw]:
* Launches a firework
* Permission: `coollobby.fw`

### /lighting [/lt]
* Summons lightning
* Permission: `coollobby.lt`

### /spit
* Spits at a player (Llama spit visual effect)
* Permission: `coollobby.spit`

### /fly
* Enables/Disables flight
* Synchronizes flight if used outside Lobby
* Permission: `coollobby.fly`

## Double Jump
* Jump higher when double-tapping space
* Spawns particles during jump
* Plays sound during jump
* Permission: `coollobby.double_jump`

## All Plugin Permissions

| Permission             | Description                                |
|------------------------|--------------------------------------------|
| coollobby.*           | Access to all commands                     |
| coollobby.reload      | Allows plugin reload                       |
| coollobby.no_cooldown | No cooldown for command usage              |
| coollobby.fw          | Access to /firework                        |
| coollobby.lt          | Access to /lighting                        |
| coollobby.spit        | Access to /spit                            |
| coollobby.fly         | Access to /fly                             |
| coollobby.double_jump | Access to double jump                      |

## Supported Languages:
* ru-ru → Russian (Russia)
* en-us → English (US)
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

## Config file: `config.yml`
<details>
  <summary>Config.yml file</summary>
  
  ```yml
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

  # Plugin Pages / Страницы плагинов:
  # • Modrinth:
  # • Spigot:
  # • CurseForge:
  # • Hangar:

  # By me coffee / Поддержать разработчика:
  # • DA: https://www.donationalerts.com/r/ilezzov_dev
  # • YooMoney: https://yoomoney.ru/to/4100118180919675
  # • Telegram Gift: https://t.me/ilezovofficial
  # • TON: UQCInXoHOJAlMpZ-8GIHqv1k0dg2E4pglKAIxOf3ia5xHmKV
  # • BTC: 1KCM1QN9TNYRevvQD63UF81oBRSK67vCon
  # • Card: 5536914188326494

  # Supporting messages languages / Доступные языки сообщений:
  # • Русский (ru-RU)
  # • English (en-US)
  language: "ru-RU"

  # Check the plugin for updates
  # Проверять плагин на наличие обновлений
  check_updates: true

  lobby_settings:
    # The names of the lobby worlds
    # Имя lobby мира
    worlds:
      - world
      - lobby-1
      - lobby-2

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
  config_version: 1.0
  ```

</details>

## What if I don’t want to set up a separate server for the Lobby?
The plugin remembers the player's level, game mode, and hunger so that when leaving the Lobby (for example, to a game world), their previous state is restored. This makes CoolLobby perfect even without a dedicated Lobby server.

### Teleport from world To Lobby world:
The plugin saves exp level, food level, gamemode and sets exp level, gamemode from config

<img src="../img/gifs/FromWorldToLobby.gif">

### Teleport from Lobby world to World:
The plugin sets the previous exp level, food level and gamemode to the player

<img src="../img/gifs/FromLobbyToWorld.gif">

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

## Links:
* Contact: https://t.me/ilezovofficial
* Telegram Channel: https://t.me/ilezzov
* GitHub: https://github.com/ilezzov-code

## Support the Developer:
* DA: https://www.donationalerts.com/r/ilezzov_dev
* YooMoney: https://yoomoney.ru/to/4100118180919675
* Telegram Gift: https://t.me/ilezovofficial
* TON: UQCInXoHOJAlMpZ-8GIHqv1k0dg2E4pglKAIxOf3ia5xHmKV
* BTC: 1KCM1QN9TNYRevvQD63UF81oBRSK67vCon
* Card: 5536914188326494

## Found an issue or have a question? Create a new issue — https://github.com/ilezzov-code/CoolLobby/issues/new
