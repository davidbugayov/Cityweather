package com.dav.cityweather.basecore.asynch;

import android.net.Uri;
import android.support.annotation.NonNull;


/**
 * <p>Фильтр тасков. Может обрабатывать разные условия и объединяться в цепочки.</p>
 *
 * <p>Например, фильтр по всем объектам будет выглядеть так:</p>
 *
 * <pre class="prettyprint">
 *     TaskFilter.all();</pre>
 *
 * <p>Этот фильтр ({@link #all() all}) не может сочетаться с другими фильтрами, т.к. уже выбирает
 * всё. Ещё примеры фильтров и цепочек представлены ниже:</p>
 *
 * <pre class="prettyprint">
 *     // С указанным родительским Uri someUri:
 *     TaskFilter.withParentUri(someUri);
 *
 *     // Уже завершённые таски
 *     TaskFilter.ready();
 *
 *     // Наоборот, ещё не завершённые:
 *     TaskFilter.not(TaskFilter.ready());
 *
 *     // Завершённые, принадлежащие родительскому Uri someUri:
 *     TaskFilter.withParentUri(someUri).and(TaskFilter.ready());</pre>
 *
 * <p>И другие, более сложные условия.</p>
 *
 * <p>Конструктор закрыт снаружи: собственные фильтры следует встраивать их по тому же
 * принципу в этот класс и не плодить наследников в разных участках кода.</p>
 *
 * @author Дмитрий Соколов
 * @since 11.08.16
 */
public abstract class TaskFilter {

    /**
     * Фильтр всех тасков
     */
    private static final TaskFilter ALL = new TaskFilter() {
        @Override
        public boolean filter(Uri taskUri, PendingResult taskPending) {
            return true;
        }

        @Override
        public TaskFilter and(TaskFilter otherFilter) {
            throw new UnsupportedOperationException("ALL.and(somethingElse) is always depends of 'somethingElse'");
        }

        @Override
        public TaskFilter or(TaskFilter otherFilter) {
            throw new UnsupportedOperationException("ALL.or(somethingElse) is always true");
        }
    };

    private TaskFilter() {
    }

    /**
     * Этот метод необходимо переопределить для реализации фильтрации
     * @param taskUri Uri фильтруемого таска для проверки
     * @param taskPending {@link PendingResult} фильтруемого таска; состояние этого объекта может
     *                                         меняться динамически прямо в ходе исполнения!
     * @return {@code true} если таск проходит фильтр
     */
    public abstract boolean filter(Uri taskUri, PendingResult taskPending);

    /**
     * Создаёт фильтр для абсолютно всех тасков (всегда {@code true}). Этот метод нельзя использовать
     * в цепочках вызовов {@link #and(TaskFilter)}, {@link #or(TaskFilter)} и т.д. Попытка вызвать
     * эти методы у возвращённого объекта приведёт к {@code UnsupportedOperationException}.
     * @return Фильтр для всех тасков
     */
    public static TaskFilter all() {
        return ALL;
    }

    /**
     * Возвращает {@link TaskFilter}, фильтрующий таски с вложенными по отношению к переданному Uri.
     * @param parentUri Родительский (вышележащий) URI, дочерние таски для которого должны быть
     *                  отфильтрованы; <strong>не</strong> отфильтрует таск с непосредственно этим Uri.
     * @return Фильтр по вложенности Uri
     * @see UriUtils#isNestedUri(Uri, Uri)
     */
    public static TaskFilter withParentUri(Uri parentUri) {
        return new NestedUriFilter(parentUri);
    }

    /**
     * Фильтрует только задачи, {@link PendingResult} которых указывает в момент фильтрации на
     * текущий статус {@link PendingResult#isReady() isReady()} {@code == true}.
     * @return Фильтр готовых тасков
     */
    public static TaskFilter ready() {
        return new OnlyReadyFilter();
    }

    /**
     * Применяет логическое «И» по отношению к фильтру {@code otherFilter}. Не вызывать у фильтра,
     * возвращённого методом {@link #all()}, или же с этим фильтром в качестве аргумента.
     * @param otherFilter Другой TaskFilter
     * @return TaskFilter, объединяющий два фильтра в цепочку
     * @throws UnsupportedOperationException Если сделан вызов с фильтром, возвращённым методом
     * {@link #all()}
     */
    public TaskFilter and(TaskFilter otherFilter) {
        if (otherFilter == TaskFilter.ALL) {
            // Это приведёт к броску исключения
            ALL.and(otherFilter);
        }

        return new FilterAndChain(this, otherFilter);
    }

    /**
     * Применяет логическое «ИЛИ» по отношению к фильтру {@code otherFilter}. Не вызывать у фильтра,
     * возвращённого методом {@link #all()}, или же с этим фильтром в качестве аргумента.
     * @param otherFilter Другой TaskFilter
     * @return TaskFilter, объединяющий два фильтра в цепочку
     * @throws UnsupportedOperationException Если сделан вызов с фильтром, возвращённым методом
     * {@link #all()}
     */
    public TaskFilter or(TaskFilter otherFilter) {
        if (otherFilter == TaskFilter.ALL) {
            // Это приведёт к броску исключения
            ALL.or(otherFilter);
        }

        return new FilterOrChain(this, otherFilter);
    }

    /**
     * Возвращает фильтр, отрицающий (логическое «НЕ») результат работы другого фильтра.
     * @param condition Фильтр, для которого будет инвертировано значение
     * @return Фильтр-инвертор другого фильтра
     */
    public static TaskFilter not(@NonNull TaskFilter condition) {
        return new NotFilter(condition);
    }

    /**
     * Реализация фильтра по родительскому Uri
     */
    private static class NestedUriFilter extends TaskFilter {
        private final Uri mParentUri;

        private NestedUriFilter(@NonNull Uri mParentUri) {
            this.mParentUri = mParentUri;
        }

        @Override
        public boolean filter(Uri taskUri, PendingResult taskPending) {
            return UriUtils.isNestedUri(mParentUri, taskUri);
        }
    }

    /**
     * Общая реализация цепочки фильтров
     */
    private static abstract class FilterChain extends TaskFilter {
        protected final TaskFilter mFirstFilter;
        protected final TaskFilter mSecondFilter;

        protected FilterChain(TaskFilter mFirstFilter, TaskFilter mSecondFilter) {
            this.mFirstFilter = mFirstFilter;
            this.mSecondFilter = mSecondFilter;
        }
    }

    /**
     * Цепочка фильров «И»
     */
    private static class FilterAndChain extends FilterChain {

        protected FilterAndChain(TaskFilter mFirstFilter, TaskFilter mSecondFilter) {
            super(mFirstFilter, mSecondFilter);
        }

        @Override
        public boolean filter(Uri taskUri, PendingResult taskPending) {
            return mFirstFilter.filter(taskUri, taskPending)
                    && mSecondFilter.filter(taskUri, taskPending);
        }
    }

    /**
     * Цепочка фильтров «ИЛИ»
     */
    private static class FilterOrChain extends FilterChain {

        protected FilterOrChain(TaskFilter mFirstFilter, TaskFilter mSecondFilter) {
            super(mFirstFilter, mSecondFilter);
        }

        @Override
        public boolean filter(Uri taskUri, PendingResult taskPending) {
            return mFirstFilter.filter(taskUri, taskPending)
                    || mSecondFilter.filter(taskUri, taskPending);
        }
    }

    /**
     * Фильтр только готовых тасков ({@link PendingResult#isReady()} {@code == true})
     */
    private static class OnlyReadyFilter extends TaskFilter {
        @Override
        public boolean filter(Uri taskUri, PendingResult taskPending) {
            return taskPending.isReady();
        }
    }


    private static class NotFilter extends TaskFilter {
        private final TaskFilter mOtherFilter;

        private NotFilter(@NonNull TaskFilter mOtherFilter) {
            this.mOtherFilter = mOtherFilter;
        }

        @Override
        public boolean filter(Uri taskUri, PendingResult taskPending) {
            return !mOtherFilter.filter(taskUri, taskPending);
        }
    }
}
