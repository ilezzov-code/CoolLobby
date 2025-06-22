package ru.ilezzov.coollobby.database.data.player;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.database.DatabaseType;
import ru.ilezzov.coollobby.database.SQLDatabase;
import ru.ilezzov.coollobby.database.data.DataRepository;
import ru.ilezzov.coollobby.logging.Logger;
import ru.ilezzov.coollobby.messages.ConsoleMessages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayerDataRepository implements DataRepository<UUID, PlayerData> {
    private final Logger logger = Main.getPluginLogger();
    private final SQLDatabase database;

    private final Cache<UUID, PlayerData> cache = Caffeine.newBuilder().build();
    private final Queue<PlayerData> saveQueue = new ConcurrentLinkedQueue<>();
    private final BukkitTask autoSaveTask;

    public PlayerDataRepository(final SQLDatabase database, final Plugin plugin) {
        this.database = database;
        this.autoSaveTask = startAutoSaveScheduler(plugin);
    }

    private BukkitTask startAutoSaveScheduler(final Plugin plugin) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::flushQueue, 20L * 10, 20L * 10);
    }

    public PlayerData getFromCacheSync(final UUID key) {
        return cache.getIfPresent(key);
    }

    @Override
    public CompletableFuture<PlayerData> get(final UUID key) {
        final PlayerData playerData = cache.getIfPresent(key);

        if (playerData != null) {
            return CompletableFuture.completedFuture(playerData);
        }

        return CompletableFuture.supplyAsync(() -> loadFromDatabase(key));
    }

    private PlayerData loadFromDatabase(final UUID key) {
        final String sql = "SELECT * FROM players WHERE uuid = ?";

        try (final ResultSet resultSet = database.executePreparedQuery(sql, key.toString())) {
            if (!resultSet.next()) {
                return null;
            }

            final PlayerData playerData = new PlayerData(
                    key,
                    resultSet.getString("display_name"),
                    GameMode.valueOf(resultSet.getString("game_mode")),
                    resultSet.getInt("exp_level"),
                    resultSet.getFloat("exp_level_exp"),
                    resultSet.getInt("food_level"),
                    resultSet.getBoolean("fly_mode")
            );

            cache.put(key, playerData);
            return playerData;
        } catch (SQLException e) {
            logger.error(ConsoleMessages.databaseError(String.format("Failed to load player data for uuid %s \n Error: %s", key, e.getMessage())));
            return null;
        }
    }

    @Override
    public void insert(final UUID key, final PlayerData data) {
        CompletableFuture.runAsync(() -> {
            if (data == null) {
                return;
            }

            String sql = switch (database.getType()) {
                case SQLITE, POSTGRESQL -> "INSERT INTO players " +
                        "(uuid, display_name, game_mode, exp_level, exp_level_exp, food_level, fly_mode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT (uuid) DO NOTHING";
                case MYSQL -> "INSERT INTO players " +
                        "(uuid, display_name, game_mode, exp_level, exp_level_exp, food_level, fly_mode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE uuid = uuid";
            };

            final Object[] params = new Object[]{
                    data.getUuid().toString(),
                    data.getDisplayName(),
                    data.getMode().name(),
                    data.getExpLevel(),
                    data.getExpLevelExp(),
                    data.getFoodLevel(),
                    data.isFly()
            };

            try {
                database.executePreparedUpdate(sql, params);
                cache.put(data.getUuid(), data);
            } catch (SQLException e) {
                logger.info(ConsoleMessages.errorOccurred("Failed to insert player data for uuid: " + data.getUuid() + "\n" + e.getMessage()));
            }
        });
    }

    @Override
    public void insertAll(final Collection<? extends PlayerData> data) {
        CompletableFuture.runAsync(() -> {
            if (data.isEmpty()) return;

            final String sql = switch (database.getType()) {
                case SQLITE, POSTGRESQL -> "INSERT INTO players " +
                        "(uuid, display_name, game_mode, exp_level, exp_level_exp, food_level, fly_mode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT (uuid) DO NOTHING";
                case MYSQL -> "INSERT IGNORE INTO players " +
                        "(uuid, display_name, game_mode, exp_level, exp_level_exp, food_level, fly_mode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            };

            final List<Object[]> batchParams = new ArrayList<>(data.size());
            final List<PlayerData> playerDataList = new ArrayList<>(data);

            for (final PlayerData player : data) {
                batchParams.add(new Object[]{
                        player.getUuid().toString(),
                        player.getDisplayName(),
                        player.getMode().name(),
                        player.getExpLevel(),
                        player.getExpLevelExp(),
                        player.getFoodLevel(),
                        player.isFly()
                });
            }

            try {
                final int[] affectedRows = database.executePreparedBatchUpdate(sql, batchParams);

                for (int i = 0; i < affectedRows.length; i++) {
                    boolean wasInserted = affectedRows[i] == 1;

                    if (wasInserted) {
                        final PlayerData playerData = playerDataList.get(i);
                        cache.put(playerData.getUuid(), playerData);
                    }
                }
            } catch (SQLException e) {
                logger.info(ConsoleMessages.errorOccurred("Failed to insert player data: " + e.getMessage()));
            }
        });
    }

    @Override
    public CompletableFuture<Integer> delete(final UUID key) {
        return CompletableFuture.supplyAsync(() -> {
            if (key == null) {
                return -1;
            }

            if (get(key) == null) {
                return 0;
            }

            final String sql = "DELETE FROM players WHERE uuid = ?";

            try {
                database.executePreparedUpdate(sql, key.toString());
                return 1;
            } catch (SQLException e) {
                logger.info(ConsoleMessages.errorOccurred("Failed to delete player data for uuid = " + key + "\n" + e.getMessage()));
                return -1;
            }
        });
    }

    @Override
    public Map<UUID, PlayerData> asMap() {
        return cache.asMap();
    }

    @Override
    public void queueSave(final PlayerData value, final boolean deleteFromCache) {
        if (value == null) {
            return;
        }

        if (deleteFromCache) {
            cache.invalidate(value.getUuid());
        }

        if (!value.isDirty()) {
            return;
        }

        saveQueue.add(value);
    }

    @Override
    public void flushQueue() {
        final List<PlayerData> batch = new ArrayList<>();

        PlayerData data;
        while ((data = saveQueue.poll()) != null) {
            if (data.isDirty()) {
                batch.add(data);
                data.markClean();
                logger.info(ConsoleMessages.savePlayer(data.getDisplayName()));
            }
        }

        if (batch.isEmpty()) {
            return;
        }

        saveBatch(batch);
    }

    @Override
    public void saveCache() {
        final List<PlayerData> batch = new ArrayList<>();

        for (final PlayerData data : cache.asMap().values()) {
            final Player player = Bukkit.getPlayer(data.getUuid());

            if (player == null) {
                continue;
            }

            savePlayer(player, data);
            batch.add(data);
        }

        if (batch.isEmpty()) {
            return;
        }

        saveBatch(batch);
    }

    private void savePlayer(final Player player, final PlayerData data) {
        final GameMode mode = player.getGameMode();
        final int foodLevel = player.getFoodLevel();
        final int expLevel = player.getLevel();
        final float exp = player.getExp();
        final World world = player.getWorld();

        if (!Main.getLobbyManager().isLobby(world)) {
            data.setMode(mode);
            data.setFoodLevel(foodLevel);
            data.setExpLevel(expLevel);
            data.setExpLevelExp(exp);
        }
    }

    private void saveBatch(final List<PlayerData> batch) {
        final String sql = "UPDATE players SET " +
                "display_name = ?, " +
                "game_mode = ?, " +
                "exp_level = ?, " +
                "exp_level_exp = ?, " +
                "food_level = ?, " +
                "fly_mode = ? " +
                "WHERE uuid = ?";

        final List<Object[]> batchParams = new ArrayList<>(batch.size());

        for (final PlayerData player : batch) {
            batchParams.add(new Object[]{
                        player.getDisplayName(),
                        player.getMode().name(),
                        player.getExpLevel(),
                        player.getExpLevelExp(),
                        player.getFoodLevel(),
                        player.isFly(),
                        player.getUuid().toString()
            });
        }

        try {
            database.executePreparedBatchUpdate(sql, batchParams);
            logger.info(ConsoleMessages.saveQueue());
        } catch (SQLException e) {
            logger.info(ConsoleMessages.errorOccurred("Failed to insert player data: " + e.getMessage()));
        }
    }

    @Override
    public void stopAutoSave() {
        this.autoSaveTask.cancel();
    }
}
