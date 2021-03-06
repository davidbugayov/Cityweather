package com.dav.cityweather.basecore.asynch;


import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

/**
 * Контейнер результата асинхронной операции.
 *
 * <p>Свойства и умения отложенного результата:</p>
 * <ol>
 *     <li>Позволяет проверять текущий статус загрузки методом {@link #isLoading()} и/или
 *     готовность контента методом {@link #isReady()}.</li>
 *     <li> Позволяет получить текущий слепок результата методом {@link #getResult()}. Для впервые исполняемой
 *     (не исполненной) задачи он возвратит {@code null}, для исполненной (и, возможно, обновляемой)
 *     задачи — последний полученный результат (который, в общем случае, тоже может быть {@code null}).</li>
 *     <li> Результат может быть помечен как “устаревший” через {@link #markAsDirty() markAsDirty()} —
 *     в таком случае он через систему коллбэков закажет обновление в хосте ({@link IAsyncProcessor}).</li>
 *     <li> Возможно блокирующее получение результата методом {@link #awaitAndGetResult()} — его можно вызывать
 *     только в побочном треде. Такое свойство может нам потребоваться, например, в сервисе пушей:
 *     в побочном треде заказывается отложенный результат, после чего у него немедленно вызывается await().</li>
 *     <li> По окончании загрузки уведомляет подписчиков Uri о событии.</li>
 * </ol>
 *
 * @author QuickNick
 */
public interface PendingResult<T> {

    /**
     * Определяет, является ли этот результат «грязным», т.е. требующим обновления.
     * @return Признак пометки результата как dirty
     */
    boolean isDirty();

    /**
     * Помечает результат как dirty, после чего он должен быть перезагружен. При этом старый результат
     * остаётся доступен в {@link #getResult()}, потому в проверки следует добавлять вызов
     * {@link #isReady()} при необходимости.
     */
    void markAsDirty();

    /**
     * @return Признак идущей в данный момент загрузки (перезагрузки) данных.
     */
    boolean isLoading();

    /**
     * Определяет готовность данных в этом PendingResult-е. Готовность не обязана совпадать
     * с завершённой загрузкой во всех случаях.
     *
     * <p>Данные готовы, если:</p>
     *
     * <ol>
     *     <li>они вообще загружены, и</li>
     *     <li>они не помечены как устаревшие (dirty)</li>
     * </ol>
     *
     * @return Признак готовности данных.
     */
    boolean isReady();

    /**
     * Немедленно возвращает результат по данному URI, если есть.
     * @return Результат или {@code null}
     */
    @Nullable
    T getResult();

    /**
     * Возвращает результат по данному URI, ожидая выполнения задачи если результата ещё нет
     * @return
     */
    @Nullable
    @WorkerThread
    T awaitAndGetResult();

    /**
     * @return Uri, связанный с этим результатом
     */
    Uri getUri();
}
