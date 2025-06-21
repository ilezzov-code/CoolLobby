package ru.ilezzov.coollobby.database.data;

public interface QueueManager<T> {
    void queueSave(T value, boolean deleteFromCache); // Добавить данные в очередь на сохранение

    void flushQueue(); // Асинхронно сохранить очередь

    void stopAutoSave(); // Остановить авто сохранение
}
