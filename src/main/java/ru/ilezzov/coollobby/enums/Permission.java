package ru.ilezzov.coollobby.enums;

public enum Permissions {
    MAIN("cool-lobby.*"),
    NO_COOLDOWN("cool-lobby.no-cooldown"),
    RELOAD("cool-lobby.reload"),
    FLY("cool-lobby.fly");

    private final String node;

    Permission(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }
}
