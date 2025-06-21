package ru.ilezzov.coollobby.database.data;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Repository for managing data in the database.
 * Provides basic CRUD operations with support for batch inserts and updates.
 *
 * @param <K> The key type (e.g., {@link java.util.UUID} or {@link String})
 * @param <V> The data type to store (e.g., {@link ru.ilezzov.coollobby.database.data.player.PlayerData})
 */
public interface DataRepository<K, V> extends QueueManager<V> {

    /**
     * Gets player data by key.
     *
     * @param key The search key (must not be {@code null})
     * @return Player data ({@code V}) or {@code null} if not found
     */
    CompletableFuture<V> get(K key);

    /**
     * Inserts player data into the database if it does not already exist.
     * If a record with the same key exists, the operation is ignored (no-op).
     *
     * @param key  The key associated with the data (must not be {@code null})
     * @param data The data to insert (must not be {@code null})
     */
    void insert(K key, V data);

    /**
     * Batch-inserts a collection of player data.
     * Existing records are ignored (idempotent operation).
     *
     * @param data The data collection (must not be {@code null})
     */
    void insertAll(Collection<? extends V> data);

    CompletableFuture<Integer> delete(K key);

    Map<K, V> asMap();

    void saveCache();
}