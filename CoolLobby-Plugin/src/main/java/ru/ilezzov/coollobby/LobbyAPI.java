package ru.ilezzov.coollobby;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import ru.ilezzov.coollobby.api.ApiResponse;
import ru.ilezzov.coollobby.api.CoolLobbyApi;
import ru.ilezzov.coollobby.database.data.player.PlayerDataRepository;
import ru.ilezzov.coollobby.database.data.spawn.SpawnData;
import ru.ilezzov.coollobby.database.data.spawn.SpawnDataRepository;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class LobbyAPI implements CoolLobbyApi {
    private final Random RANDOM = new Random();

    private final Plugin plugin;

    private final FileConfiguration config = Main.getConfigFile().getConfig();
    private final ConfigurationSection lobbySettings = config.getConfigurationSection("lobby_settings");

    private final PlayerDataRepository playerDataRepository = Main.getPlayerDataRepository();
    private final SpawnDataRepository spawnDataRepository = Main.getSpawnDataRepository();

    private final boolean enableDefaultGameMode = lobbySettings.getBoolean("default_gamemode.enable");
    private final GameMode defaultGameMode = getDefaultGameMode();

    private final boolean enableDefaultLevel = lobbySettings.getBoolean("default_level.enable");
    private final int expLevel = getDefaultLevel();

    public LobbyAPI(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void spawnFirework(final @NotNull Player player) {
        final World world = player.getWorld();
        final Location location = player.getLocation();

        final Firework firework = createFirework(world, location);
        final FireworkMeta fireworkMeta = firework.getFireworkMeta();
        final FireworkEffect fireworkEffect = FireworkEffect.builder()
                .with(getFireworkRandomType())
                .withColor(getRandomColors())
                .withFade(getRandomColors())
                .flicker(true)
                .trail(true)
                .build();

        fireworkMeta.addEffect(fireworkEffect);
        fireworkMeta.setPower(RANDOM.nextInt(3) + 1);
        firework.setFireworkMeta(fireworkMeta);
    }


    @Override
    public void spawnFirework(final @NotNull Location location) {
        final World world = location.getWorld();

        final Firework firework = createFirework(world, location);
        final FireworkMeta fireworkMeta = firework.getFireworkMeta();
        final FireworkEffect fireworkEffect = FireworkEffect.builder()
                .with(getFireworkRandomType())
                .withColor(getRandomColors())
                .withFade(getRandomColors())
                .flicker(true)
                .trail(true)
                .build();

        fireworkMeta.addEffect(fireworkEffect);
        fireworkMeta.setPower(RANDOM.nextInt(3) + 1);
        firework.setFireworkMeta(fireworkMeta);
    }

    private Firework createFirework(final World world, final Location location) {
        return (Firework) world.spawnEntity(location, EntityType.FIREWORK);
    }

    private FireworkEffect.Type getFireworkRandomType() {
        final FireworkEffect.Type[] types = FireworkEffect.Type.values();
        return types[RANDOM.nextInt(types.length)];
    }

    private Color[] getRandomColors() {
        final int count = RANDOM.nextInt(3) + 1;
        final Color[] colors = new Color[count];

        for (int i = 0; i < count; i++) {
            colors[i] = Color.fromRGB(RANDOM.nextInt(0xFFFFFF));
        }

        return colors;
    }

    @Override
    public void spawnLighting(final @NotNull Player player, final boolean doDamage) {
        final World world = player.getWorld();
        final Location location = player.getLocation();

        if (doDamage) world.strikeLightning(location);
        else world.strikeLightningEffect(location);
    }

    @Override
    public void spawnLighting(final @NotNull Location location, final boolean doDamage) {
        final World world = location.getWorld();

        if (doDamage) world.strikeLightning(location);
        else world.strikeLightningEffect(location);
    }

    @Override
    public void spawnSpit(final @NotNull Player player, final boolean playSound) {
        final World world = player.getWorld();
        final Location spitLocation = player.getLocation().toVector().add(player.getLocation().getDirection().multiply(0.8)).toLocation(player.getWorld()).add(0.0, 1.0, 0.0);
        final LlamaSpit llamaSpit = (LlamaSpit) world.spawnEntity(spitLocation, EntityType.LLAMA_SPIT);

        llamaSpit.setVelocity(player.getEyeLocation().getDirection().multiply(1));
        if (playSound) {
            playSpitSound(player, world);
        }
    }

    private void playSpitSound(final Player player, final World world) {
        world.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 1, 1);
    }

    @Override
    public void createDoubleJump(final @NotNull Player player) {
        addPlayerVelocity(player);
    }

    @Override
    public void createDoubleJump(final @NotNull Player player, final Particle particle, final Sound sound) {
        addPlayerVelocity(player);
        playDoubleJumpParticles(player, particle);
        playDoubleJumpSound(player, sound);
    }

    private void addPlayerVelocity(final Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1));
    }

    private void playDoubleJumpParticles(final Player player, final Particle particle) {
        if (particle == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnGround()) {
                    cancel();
                }

                if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.WATER) {
                    cancel();
                }

                player.getWorld().spawnParticle(particle, player.getLocation(), 1, 0, 0, 0, 0);
            }
        }.runTaskTimer(this.plugin, 0, 1);
    }

    private void playDoubleJumpSound(final Player player, final Sound sound) {
        if (sound == null) return;
        player.getWorld().playSound(player.getLocation(), sound, 1, 1);
    }

    @Override
    public CompletableFuture<ApiResponse> setFlyMode(final @NotNull Player player) {
        final UUID uuid = player.getUniqueId();

        return playerDataRepository.get(uuid).thenCompose(playerData -> {
            if (playerData == null) {
                return CompletableFuture.completedFuture(new ApiResponse(-1, null));
            }

            CompletableFuture<ApiResponse> future = new CompletableFuture<>();
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (playerData.isFly()) {
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    playerData.setFly(false);
                    future.complete(new ApiResponse(0, null));
                } else {
                    player.setAllowFlight(true);
                    playerData.setFly(true);
                    future.complete(new ApiResponse(1, null));
                }
            });

            return future;
        });
    }


    @Override
    public void setPlayerLevel(final @NotNull Player player) {
        setPlayerLevel(player, expLevel, 0);
    }

    @Override
    public void setPlayerLevel(final @NotNull Player player, final int level) {
        if (level < 0) return;
        player.setLevel(level);
    }

    @Override
    public void setPlayerLevel(final @NotNull Player player, final int level, final float exp) {
        if (level < 0 || exp < 0) return;
        player.setLevel(level);
        player.setExp(exp);
    }

    @Override
    public void setFoodLevel(final @NotNull Player player, final int foodLevel) {
        if (foodLevel < 0) return;
        player.setFoodLevel(foodLevel);
    }

    @Override
    public void setGamemode(final @NotNull Player player) {
        setGamemode(player, defaultGameMode);
    }

    @Override
    public void setGamemode(final @NotNull Player player, final GameMode mode) {
        if (mode == null) return;
        player.setGameMode(mode);
    }

    @Override
    public CompletableFuture<ApiResponse> teleportToSpawn(final Player player) {
        final SpawnData spawnData = spawnDataRepository.getRandom();
        return teleportToSpawnFuture(player, spawnData);
    }

    @Override
    public CompletableFuture<ApiResponse> teleportToSpawn(final Player player, final String spawnName) {
        return spawnDataRepository.get(spawnName).thenCompose(data -> teleportToSpawnFuture(player, data));
    }

    private CompletableFuture<ApiResponse> teleportToSpawnFuture(final Player player, final SpawnData spawnData) {
        if (spawnData == null) {
            return CompletableFuture.completedFuture(new ApiResponse(0, null));
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            player.teleport(spawnData.getLocation());
        });

        return CompletableFuture.completedFuture(new ApiResponse(1, spawnData.getName()));
    }

    @Override
    public CompletableFuture<ApiResponse> setSpawn(final Player player) {
        final Location location = player.getLocation();
        final World world = location.getWorld();

        return spawnDataRepository.get(world.getName()).thenCompose(spawnData -> {
            if (spawnData == null) {
                return CompletableFuture.completedFuture(new ApiResponse(1, insertSpawn(location).getName()));
            }

            return CompletableFuture.completedFuture(new ApiResponse(0, spawnData));
        });
    }

    private SpawnData insertSpawn(final Location location) {
        final World world = location.getWorld();
        final String name = world.getName();
        final SpawnData spawnData = new SpawnData(
                location.getWorld().getName(),
                location.getWorld(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                location.getPitch(),
                location.getYaw(),
                location
        );
        spawnDataRepository.insert(name, spawnData);
        return spawnData;
    }

    @Override
    public CompletableFuture<ApiResponse> removeSpawn(final String spawnName) {
        return spawnDataRepository.delete(spawnName).thenCompose(response -> CompletableFuture.completedFuture(new ApiResponse(response, null)));
    }

    @Override
    public void updateSpawn(final String spawnName, final Location location) {
        spawnDataRepository.get(spawnName).thenAccept(spawnData -> {
            spawnData.update(location);
            spawnDataRepository.queueSave(spawnData, false);
        });
    }

    private GameMode getDefaultGameMode() {
        if (!enableDefaultGameMode) {
            return null;
        }

        assert lobbySettings != null;
        final String gameModeType = lobbySettings.getString("default_gamemode.type");

        return switch (gameModeType.toLowerCase()) {
            case "survival" -> GameMode.SURVIVAL;
            case "creative" -> GameMode.CREATIVE;
            case "adventure" -> GameMode.ADVENTURE;
            case "spectator" -> GameMode.SPECTATOR;
            default -> Bukkit.getDefaultGameMode();
        };
    }

    private int getDefaultLevel() {
        if (enableDefaultLevel) {
            return lobbySettings.getInt("default_level.level");
        }
        return -1;
    }
}
