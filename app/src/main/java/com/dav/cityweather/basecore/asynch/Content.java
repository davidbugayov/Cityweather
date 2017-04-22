package com.dav.cityweather.basecore.asynch;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import java.util.Objects;

/**
 * Внутренний класс, используемый для хранения контента в реализациях {@link PendingResult}. Может
 * быть полезен для тех случаев, когда контент оказывается {@code null} в результате загрузки. В
 * итоге нельзя проверять готовность просто через:
 *
 * <div>
 * <pre class="prettyprint">
 *     if (mContent == null) {
 *         // Может, загружено… может и нет
 *     }
 * </pre>
 * </div>
 *
 * <p>А благодаря такой обёртке проверка становится валидной: сам контент придётся получать через
 * вызов {@link #get()}. Для пользователя core это означает возможность проверки через
 * {@link PendingResult#isReady()} факта готовности данных.</p>
 *
 *
 * <p>Это immutable-объект, потому метод set() не предусмотрен и не должен добавляться.</p>
 */
final class Content<Type> {

    private final Type mContent;

    // Из задумок: добавить сюда timestamp

    Content(@Nullable Type content) {
        mContent = content;
    }

    Type get() {
        return mContent;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Content)) {
            return false;
        }
        Content<?> content = (Content<?>) o;
        return Objects.equals(mContent, content.mContent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hashCode(mContent);
    }
}
