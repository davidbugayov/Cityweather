package com.dav.cityweather.basecore.asynch;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ru.sberbank.mobile.core.uri.UriManager;

/**
 * @author QuickNick
 */
public class MultiThreadAsyncProcessor implements IAsyncProcessor {

    private final Context mContext;
    private final ExecutorService mExecutor;
    private final ConcurrentMap<Uri, PendingResultImpl<?>> mPendingResultMap;
    private final UriManager mUriManager;

    public MultiThreadAsyncProcessor(Context context, UriManager uriManager) {
        mContext = context;
        mUriManager = uriManager;
        mExecutor = Executors.newCachedThreadPool();
        mPendingResultMap = new ConcurrentHashMap<>();
    }

    @Override
    public <T> PendingResult<T> submit(@NonNull Uri uri, @NonNull NoExceptionCallable<T> task,
                                       boolean forceReplace) {
        PendingResultImpl<T> pending = (PendingResultImpl<T>) mPendingResultMap.get(uri);
        if (forceReplace || (pending == null)) {
            pending = new PendingResultImpl<>(uri, task, new StateListenerImpl<T>());
            mPendingResultMap.put(uri, pending);
        }
        boolean loading = pending.isLoading();
        if (!loading && !pending.isReady()) {
            submitTaskInternal(pending);
        }
        return pending;
    }

    @Override
    public <T> PendingResult<T> findResult(@NonNull Uri uri) {
        PendingResultImpl<T> pending = (PendingResultImpl<T>) mPendingResultMap.get(uri);
        return pending;
    }

    @Override
    public List<Uri> getTasksUris(@NonNull TaskFilter filter) {
        List<Uri> result = new ArrayList<>();

        for (Uri uri : mPendingResultMap.keySet()) {
            PendingResultImpl<?> value = mPendingResultMap.get(uri);

            // Мало ли что, в реализации synchronizedMap никак не гарантировано сохранение ключей
            // и значений одновременно
            if (value == null) {
                continue;
            }

            if (filter.filter(uri, value)) {
                result.add(uri);
            }
        }

        return Collections.unmodifiableList(result);
    }

    @Override
    public void clearTasks(@NonNull TaskFilter filter) {
        List<Uri> uris = getTasksUris(filter);
        for (Uri uri : uris) {
            clearTaskInternal(uri);
        }
    }

    @Override
    public boolean clearTask(@Nullable Uri uri) {
        boolean cleared = false;
        if (uri != null) {
            cleared = clearTaskInternal(uri);
        }
        return cleared;
    }

    private boolean clearTaskInternal(@NonNull Uri uri) {
        boolean cleared = false;
        PendingResultImpl<?> task = mPendingResultMap.get(uri);
        if (task != null) {
            if (task.getFuture() != null && !task.getFuture().isDone()) {
                task.getFuture().cancel(true);
            }
            mPendingResultMap.remove(uri);
            cleared = true;
        }
        return cleared;
    }

    @Override
    public void markTaskAsDirty(@NonNull Uri uri) {
        PendingResult<?> task = mPendingResultMap.get(uri);
        if (task != null) {
            task.markAsDirty();
        }
    }

    @Override
    public void notifyChange(@NonNull Uri uri) {
        mContext.getContentResolver().notifyChange(uri, null);
    }

    @Override
    public UriManager getUriManager() {
        return mUriManager;
    }

    private <T> void submitTaskInternal(PendingResultImpl<T> pendingResult) {
        pendingResult.setLoading(true);
        Future<T> future = mExecutor.submit(pendingResult);
        pendingResult.setFuture(future);
    }

    /**
     * Метод проверяет, находится ли данный результат в мапе результатов.
     * Возможна ситуация, что мы force-флагом заместили PendingResult в мапе, и:
     * <ol>
     * <li>у отсоединённого результата кто-то вызвал markAsDirty(), и его отправили на
     * довыполнение.</li>
     * <li>отсоединённый результат закончил своё выполнение.</li>
     * </ol>
     *
     * В обоих случаях уведомления нужно проигнорировать.
     *
     * @param pendingResult
     * @return
     */
    private boolean isPendingResultAttached(PendingResult<?> pendingResult) {
        PendingResult<?> mapped = mPendingResultMap.get(pendingResult.getUri());
        return (mapped == pendingResult);
    }

    private class StateListenerImpl<T> implements PendingResultImpl.StateListener<T> {

        @Override
        public void onBecomeDirty(PendingResultImpl<T> sender) {
            if (isPendingResultAttached(sender)) {
                submitTaskInternal(sender);
                // FIXME: 23.11.16 Рассмотреть возможность добавления сюда mContext.getContentResolver().notifyChange(sender.getUri(), null);
                // FIXME: 23.11.16 Есть кейсы в которых по результату выполнения операции необходимо выполнить новый запрос
                // FIXME: 23.11.16 В частности в привязке профиля в SignOn необходимо перезапрашивать список мерчантов после приивязки/отвязки
                // FIXME: 23.11.16 Notify необходим для возможности включить индикатор загрузки
            }
        }

        @Override
        public void onLoaded(PendingResultImpl<T> sender) {
            if (isPendingResultAttached(sender)) {
                mContext.getContentResolver().notifyChange(sender.getUri(), null);
            }
        }
    }
}
