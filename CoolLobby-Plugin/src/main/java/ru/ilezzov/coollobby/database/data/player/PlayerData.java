package ru.ilezzov.coollobby.database.data.player;

import lombok.Getter;
import org.bukkit.GameMode;

import java.util.Objects;
import java.util.UUID;

@Getter
public class PlayerData {
    private final UUID uuid;
    private final String displayName;
    private GameMode mode;
    private int expLevel;
    private float expLevelExp;
    private int foodLevel;
    private boolean isFly;
    private boolean dirty = false;

    public PlayerData(final UUID uuid, final String displayName, final GameMode mode, final int expLevel, final float expLevelExp, final int foodLevel, final boolean isFly) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.mode = mode;
        this.expLevel = expLevel;
        this.expLevelExp = expLevelExp;
        this.foodLevel = foodLevel;
        this.isFly = isFly;
    }

    public void setMode(final GameMode mode) {
        this.mode = mode;
        this.dirty = true;
    }

    public void setExpLevel(final int expLevel) {
        this.expLevel = expLevel;
        this.dirty = true;
    }

    public void setExpLevelExp(final float expLevelExp) {
        this.expLevelExp = expLevelExp;
        this.dirty = true;
    }

    public void setFoodLevel(final int foodLevel) {
        this.foodLevel = foodLevel;
        this.dirty = true;
    }

    public void setFly(final boolean fly) {
        this.isFly = fly;
        this.dirty = true;
    }

    public void markClean() {
        this.dirty = false;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final PlayerData that = (PlayerData) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    @Override
    public String toString() {
        return String.format(
                "| %-36s | %-16s | %-8s | %-8.1f | %-10d | %-10d | %-5b |",
                uuid, displayName, mode, expLevelExp, expLevel, foodLevel, isFly
        );
    }
}
