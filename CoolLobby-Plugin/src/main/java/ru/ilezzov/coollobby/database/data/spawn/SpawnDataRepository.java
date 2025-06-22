package ru.ilezzov.coollobby.database.data.spawn;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.database.SQLDatabase;
import ru.ilezzov.coollobby.database.data.DataRepository;
import ru.ilezzov.coollobby.logging.Logger;
import ru.ilezzov.coollobby.messages.ConsoleMessages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

@Slf4j
public class SpawnDataRepository implements DataRepository<String, SpawnData> {
    private final Logger logger = Main.getPluginLogger();
    private final SQLDatabase database;

    private final Cache<String, SpawnData> cache = Caffeine.newBuilder().build();
    private final Queue<SpawnData> saveQueue = new ConcurrentLinkedQueue<>();
    private final BukkitTask autoSaveTask;

    private final Random RANDOM = new Random();

    public SpawnDataRepository(final SQLDatabase database, final Plugin plugin) {
        this.database = database;
        this.autoSaveTask = startAutoSaveScheduler(plugin);
        loadAll();
    }

    private BukkitTask startAutoSaveScheduler(final Plugin plugin) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::flushQueue, 20L * 10, 20L * 10);
    }

    @Override
    public CompletableFuture<SpawnData> get(final String key) {
        final SpawnData spawnData = cache.getIfPresent(key);

        if (spawnData != null) {
            return CompletableFuture.completedFuture(spawnData);
        }

        return CompletableFuture.supplyAsync(() -> loadFromDatabase(key));
    }

    public SpawnData getRandom() {
        final List<String> keys = List.copyOf(cache.asMap().keySet());
        return getRandomData(keys);
    }

    private SpawnData getRandomData(final List<String> keys) {
        if (keys.isEmpty()) {
            return null;
        }

        final String key = keys.get(RANDOM.nextInt(keys.size()));
        return cache.getIfPresent(key);
    }

    private SpawnData loadFromDatabase(final String key) {
        final String sql = "SELECT * FROM server_spawn WHERE name = ?";

        if (key == null || key.isEmpty()) {
            return null;
        }

        try (final ResultSet resultSet = database.executePreparedQuery(sql, key)) {
            if (!resultSet.next()) {
                return null;
            }

            final String name = resultSet.getString("name");
            final World world = Bukkit.getWorld(resultSet.getString("world_name"));
            final float x = resultSet.getFloat("x");
            final float y = resultSet.getFloat("y");
            final float z = resultSet.getFloat("z");
            final float pitch = resultSet.getFloat("pitch");
            final float yaw = resultSet.getFloat("yaw");
            final Location location = SpawnData.createLocation(world, x, y, z, yaw, pitch);

            final SpawnData spawnData = new SpawnData(
                    name,
                    world,
                    x,
                    y,
                    z,
                    pitch,
                    yaw,
                    location
            );

            cache.put(key, spawnData);
            return spawnData;
        } catch (SQLException e) {
            logger.error(ConsoleMessages.databaseError(String.format("Failed to load spawn data for name %s \n Error: %s", key, e.getMessage())));
            return null;
        }
    }

    @Override
    public void insert(final String key, final SpawnData data) {
        CompletableFuture.runAsync(() -> {
            if (key == null || key.isEmpty()) {
                return;
            }

            if (data == null) {
                return;
            }

            String sql = switch (database.getType()) {
                case SQLITE, POSTGRESQL -> "INSERT INTO server_spawn " +
                        "(name, world_name, x, y, z, pitch, yaw) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT (name) DO NOTHING";
                case MYSQL -> "INSERT INTO server_spawn " +
                        "(name, world_name, x, y, z, pitch, yaw) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE name = name";
            };

            final Object[] params = new Object[]{
                    data.getName(),
                    data.getWorld().getName(),
                    data.getX(),
                    data.getY(),
                    data.getZ(),
                    data.getPitch(),
                    data.getYaw()
            };

            try {
                database.executePreparedUpdate(sql, params);
                cache.put(data.getName(), data);
            } catch (SQLException e) {
                logger.info(ConsoleMessages.errorOccurred("Failed to insert spawn data for name: " + data.getName() + "\n" + e.getMessage()));
            }
        });
    }

    @Override
    public void insertAll(final Collection<? extends SpawnData> data) {
        CompletableFuture.runAsync(() -> {
            if (data == null || data.isEmpty()) {
                return;
            }

            final String sql = switch (database.getType()) {
                case SQLITE, POSTGRESQL -> "INSERT INTO server_spawn " +
                        "(name, world_name, x, y, z, pitch, yaw) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT (name) DO NOTHING";
                case MYSQL -> "INSERT IGNORE INTO players " +
                        "(name, world_name, x, y, z, pitch, yaw) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            };
            final List<Object[]> batchParams = new ArrayList<>(data.size());
            for (final SpawnData spawnData : data) {
                batchParams.add( new Object[]{
                        spawnData.getName(),
                        spawnData.getWorld().getName(),
                        spawnData.getX(),
                        spawnData.getY(),
                        spawnData.getZ(),
                        spawnData.getPitch(),
                        spawnData.getYaw()
                });

                cache.put(spawnData.getName(), spawnData);
            }

            try {
                database.executePreparedBatchUpdate(sql, batchParams);
            } catch (SQLException e) {
                logger.info(ConsoleMessages.errorOccurred("Failed to insert spawn data: " + e.getMessage()));
            }
        });
    }

    @Override
    public CompletableFuture<Integer> delete(final String key) {
        return CompletableFuture.supplyAsync(() -> {
            if (key == null || key.isEmpty()) {
                return -1;
            }

            try {
                if (get(key).get() == null) {
                    return 0;
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.info(ConsoleMessages.errorOccurred("Failed to delete spawn data for name = " + key + "\n" + e.getMessage()));
                return -1;
            }

            final String sql = "DELETE FROM server_spawn WHERE name = ?";
            cache.invalidate(key);

            try {
                database.executePreparedUpdate(sql, key);
                return 1;
            } catch (SQLException e) {
                logger.info(ConsoleMessages.errorOccurred("Failed to delete spawn data for name = " + key + "\n" + e.getMessage()));
                return -1;
            }
        });
    }

    @Override
    public Map<String, SpawnData> asMap() {
        return cache.asMap();
    }

    @Override
    public void saveCache() {

        final List<SpawnData> batch = new ArrayList<>(cache.asMap().values());

        if (batch.isEmpty()) {
            return;
        }

        saveBatch(batch);
    }

    private void saveBatch(final List<SpawnData> batch) {
        final String sql = "UPDATE server_spawn SET " +
                "world_name = ?, " +
                "x = ?, " +
                "y = ?, " +
                "z = ?, " +
                "pitch = ?, " +
                "yaw = ? " +
                "WHERE name = ?";

        final List<Object[]> batchParams = new ArrayList<>(batch.size());

        for (final SpawnData spawnData : batch) {
            batchParams.add(new Object[]{
                    spawnData.getWorld().getName(),
                    spawnData.getX(),
                    spawnData.getY(),
                    spawnData.getZ(),
                    spawnData.getPitch(),
                    spawnData.getYaw(),
                    spawnData.getName()
            });
        }

        try {
            database.executePreparedBatchUpdate(sql, batchParams);
            logger.info(ConsoleMessages.saveQueue());
        } catch (SQLException e) {
            logger.info(ConsoleMessages.errorOccurred("Failed to insert spawn data: " + e.getMessage()));
        }
    }

    @Override
    public void queueSave(final SpawnData value, final boolean deleteFromCache) {
        if (value == null) {
            return;
        }

        if (deleteFromCache) {
            cache.invalidate(value.getName());
        }

        saveQueue.add(value);
    }

    @Override
    public void flushQueue() {
        final List<SpawnData> batch = new ArrayList<>();
        SpawnData data;

        while ((data = saveQueue.poll()) != null) {
            batch.add(data);
        }

        if (batch.isEmpty()) {
            return;
        }

        saveBatch(batch);
    }

    @Override
    public void stopAutoSave() {
        this.autoSaveTask.cancel();
    }

    private void loadAll() {
        final String sql = "SELECT * FROM server_spawn";

        try (final ResultSet resultSet = database.executePreparedQuery(sql)) {
            while (resultSet.next()) {
                final String name = resultSet.getString("name");
                final World world = Bukkit.getWorld(resultSet.getString("world_name"));
                final float x = resultSet.getFloat("x");
                final float y = resultSet.getFloat("y");
                final float z = resultSet.getFloat("z");
                final float pitch = resultSet.getFloat("pitch");
                final float yaw = resultSet.getFloat("yaw");
                final Location location = SpawnData.createLocation(world, x, y, z, yaw, pitch);

                final SpawnData spawnData = new SpawnData(
                        name,
                        world,
                        x,
                        y,
                        z,
                        pitch,
                        yaw,
                        location
                );

                cache.put(name, spawnData);
            }
        } catch (SQLException e) {
            logger.error(ConsoleMessages.databaseError(String.format("Failed to load all spawn data \n Error: %s", e.getMessage())));
        }
    }
}
