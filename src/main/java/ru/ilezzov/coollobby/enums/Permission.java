package ru.ilezzov.coollobby.enums;

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
    DOUBLE_JUMP("coollobby.double_jump");

    private final String permission;

    Permission(final String permission) {
        this.permission = permission;
    }
}
