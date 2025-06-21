package ru.ilezzov.coollobby.api;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * CoolLobby API
 */

public interface CoolLobbyApi {
    /**
     * Spawns a firework at the player's current location.
     *
     * @param player the player at whose location the firework will be spawned
     */
    void spawnFirework(Player player);

    /**
     * Spawns a firework at the specified location.
     *
     * @param location the location where the firework will be spawned
     */
    void spawnFirework(Location location);

    /**
     * Summons lightning at the player's location.
     *
     * @param player    the player at whose location lightning will strike
     * @param doDamage  whether the lightning should cause damage
     */
    void spawnLighting(Player player, boolean doDamage);

    /**
     * Summons lightning at the specified location.
     *
     * @param location  the location where lightning will strike
     * @param doDamage  whether the lightning should cause damage
     */
    void spawnLighting(Location location, boolean doDamage);

    /**
     * Spawns a llama spit projectile from the player.
     *
     * @param player     the player from whom the spit is spawned
     * @param playSound  whether a sound effect should be played
     */
    void spawnSpit(Player player, boolean playSound);

    /**
     * Creates a double jump for the player.
     *
     * @param player the player who will be able to double jump
     */
    void createDoubleJump(Player player);

    /**
     * Creates a double jump for the player with custom particle and sound effects.
     *
     * @param player   the player who will be able to double jump
     * @param particle the particle effect to display (nullable)
     * @param sound    the sound to play (nullable)
     */
    void createDoubleJump(Player player, @Nullable Particle particle, @Nullable Sound sound);

    /**
     * Toggles the player's fly mode.
     *
     * @param player the player whose fly mode will be toggled
     * @return the new fly mode state (e.g., 0 = off, 1 = on, -1 = error)
     */
    CompletableFuture<ApiResponse> setFlyMode(Player player);

    /**
     * Sets the player's lobby experience level.
     *
     * @param player the player whose level will be set
     */
    void setPlayerLevel(Player player);

    /**
     * Sets the player's experience level.
     *
     * @param player the player whose level will be set
     * @param level  the level to set
     */
    void setPlayerLevel(Player player, int level);

    /**
     * Sets the player's experience level and experience progress.
     *
     * @param player the player whose level and experience will be set
     * @param level  the level to set
     * @param exp    the experience progress (0.0 - 1.0)
     */
    void setPlayerLevel(Player player, int level, float exp);

    /**
     * Sets the player's food (hunger) level.
     *
     * @param player    the player whose food level will be set
     * @param foodLevel the amount of food (0–20)
     */
    void setFoodLevel(Player player, int foodLevel);


    /**
     * Sets the player's lobby game mode.
     *
     * @param player the player whose game mode will be set
     */
    void setGamemode(Player player);

    /**
     * Sets the player's game mode.
     *
     * @param player the player whose game mode will be set
     * @param mode   the game mode to set
     */
    void setGamemode(Player player, GameMode mode);

    /**
     * Teleports the specified player to a random spawn point on the server.
     *
     * @param player The player to teleport.
     * @return 0 — Spawn not found;
     * 1 — Teleportation successful.
     */

    CompletableFuture<ApiResponse> teleportToSpawn(Player player);

    /**
     * @param player    The player to teleport.
     * @param spawnName The name of the spawn location (world name)
     * @return 0 — Spawn not found;
     * 1 — Teleportation successful.
     */
    CompletableFuture<ApiResponse> teleportToSpawn(Player player, String spawnName);

    /**
     *
     */
    CompletableFuture<ApiResponse> setSpawn(Player player);

    CompletableFuture<ApiResponse> removeSpawn(String spawnName);

    void updateSpawn(String spawnName, Location location);
}