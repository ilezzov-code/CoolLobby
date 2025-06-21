package ru.ilezzov.coollobby.permission;

import lombok.Getter;

@Getter
public enum Permission {
    MAIN("coollobby.*"),
    NO_COOLDOWN("coollobby.no_cooldown"),
    RELOAD("coollobby.reload"),
    FW_COMMAND("coollobby.fw"),
    LT_COMMAND("coollobby.lt"),
    SPIT_COMMAND("coollobby.spit"),
    FLY_COMMAND("coollobby.fly"),
    DOUBLE_JUMP("coollobby.double_jump"),
    SPAWN_COMMAND("coollobby.spawn"),
    SPAWN_SET_COMMAND("coollobby.spawn.set"),
    SPAWN_REMOVE_COMMAND("coollobby.spawn.remove"),
    SPAWN_OTHER_PLAYER_COMMAND("coollobby.spawn.other_player_tp"),
    SPAWN_ALL_COMMAND("coollobby.spawn.*");


    private final String permission;

    Permission(final String permission) {
        this.permission = permission;
    }
}
