package ru.ilezzov.coollobby.database.data.spawn;


import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

@Getter
public class SpawnData {
    private final String name;
    private World world;
    private float x;
    private float y;
    private float z;
    private float pitch;
    private float yaw;
    private Location location;
    private boolean dirty = false;

    public SpawnData(final String name, final World world, final float x, final float y, final float z, final float pitch, final float yaw, final Location location) {
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.location = location;
    }

    public static Location createLocation(final World world, final float x, final float y, final float z, final float yaw, final float pitch) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public void update(final Location location) {
        this.world = location.getWorld();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.location = location;
        this.dirty = true;
    }

    @Override
    public String toString() {
        return String.format(
                "| %-16s | %-16s | %.2f | %.2f | %.2f | %.2f | %.2f | %s |",
                name, world.getName(), x, y, z, pitch, yaw, location
        );
    }
}

