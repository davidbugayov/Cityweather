package com.dav.cityweather.basecore.asynch;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.sberbank.mobile.core.uri.IUriManagerProvider;

/**
 * Асинхронный процессор для обработки команд.
 *
 * <p>Cвойства и умения процессора:</p>
 * <ol>
 *     <li>Идентифицирует каждую пару “задача-результат” посредством Uri.</li>
 *     <li>Назначает каждой задаче отложенный результат.</li>
 *     <li>При необходимости актуализации результата заново отправляет соответствующую ему задачу на исполнение.</li>
 * </ol>
 *
 * <p>Реализует {@link IUriManagerProvider} для возможности предоставления URI различным менеджерам и
 * (потенциально) проверки на корректность передаваемых URI в реализациях.</p>
 *
 * @author QuickNick
 */
public interface IAsyncProcessor {

    <T> PendingResult<T> submit(@NonNull Uri uri, @NonNull NoExceptionCallable<T> task,
                                boolean forceReplace);

    @Nullable
    <T> PendingResult<T> findResult(@NonNull Uri uri);

    /**
     * Возвращает список Uri тасков, подошедших под переданный фильтр. Например, выбрать все:
     *
     * <pre class="prettyprint">
     *     asyncProcessor.getTasksUris(TaskFilter.all());</pre>
     *
     * @param filter Фильтр
     * @return Список Uri, read-only.
     */
    List<Uri> getTasksUris(@NonNull TaskFilter filter);

    void clearTasks(@NonNull TaskFilter filter);

    boolean clearTask(@Nullable Uri uri);

    /**
     * Помечает таску как "грязную" (которую необходимо обновить).
     *
     * @param uri
     */
    void markTaskAsDirty(@NonNull Uri uri);

    /**
     * Оповещает об изменении данных по заданному Uri
     *  @param uri
     *
     */
    void notifyChange(@NonNull Uri uri);
}
