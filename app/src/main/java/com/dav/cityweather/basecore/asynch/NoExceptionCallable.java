package com.dav.cityweather.basecore.asynch;


import java.util.concurrent.Callable;

/**
 * Callable, дополнительно усиленный требованием не выбрасывать проверяемых исключений.
 *
 * <p>Именно его полагается расширять при отправке команд процессору.</p>
 *
 * @author QuickNick
 */
public interface NoExceptionCallable<V> extends Callable<V> {

    @Override
    V call();
}
