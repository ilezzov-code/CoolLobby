package ru.ilezzov.coollobby.enums;

public enum Permission {
    MAIN("cool-lobby.*"),
    NO_COOLDOWN("cool-lobby.no_cooldown"),
    RELOAD("cool-lobby.reload"),
    FIREWORK("cool-lobby.fw"),
    LIGHTING("cool-lobby.lt"),
    SPIT("cool-lobby.spit"),
    FLY("cool-lobby.fly"),
    DOUBLE_JUMP("cool-lobby.double_jump");

    private final String permission;

    Permission(final String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
