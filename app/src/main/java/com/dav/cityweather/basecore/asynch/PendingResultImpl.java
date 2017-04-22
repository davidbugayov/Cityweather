package com.dav.cityweather.basecore.asynch;


import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;


import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * @author QuickNick
 */
final class PendingResultImpl<T> implements PendingResult<T>, NoExceptionCallable<T> {

    private static final String TAG = "PendingResultImpl";

    private final Uri mUri;
    private final NoExceptionCallable<T> mCallable;
    private final StateListener<T> mStateListener;

    private volatile Content<T> mContentResult;
    private volatile Future<T> mFuture;
    private volatile boolean mLoading;
    private volatile boolean mDirty;

    PendingResultImpl(Uri uri, NoExceptionCallable<T> callable, StateListener<T> stateListener) {
        mUri = uri;
        mCallable = callable;
        mStateListener = stateListener;
        mDirty = true;
        mLoading = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDirty() {
        return mDirty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsDirty() {
        Future<T> future = mFuture;
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        mDirty = true;
        mLoading = true;
        mStateListener.onBecomeDirty(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLoading() {
        return mLoading;
    }

    void setLoading(boolean value) {
        mLoading = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReady() {
        return mContentResult != null && !mDirty;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public T getResult() {
        Content<T> content = mContentResult;
        T result = null;
        if (content != null) {
            result = content.get();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public T awaitAndGetResult() {
        //fixme скорее всего тут NPE
        Future<T> localFuture = mFuture;
        T localResult = null;
        if (localFuture != null) {
            try {
                localResult = localFuture.get();
                mDirty = false;
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (CancellationException e) {
            }
        }
        return localResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Uri getUri() {
        return mUri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized T call() {
        try {
            mContentResult = new Content<>(mCallable.call());
            mDirty = false;
            mLoading = false;
            mStateListener.onLoaded(this);
            return mContentResult.get();
        } catch (RuntimeException ex) {
            Log.e(TAG, "exception", ex);
            throw ex;
        }
    }

    public void setResult(T result) {
        mContentResult = new Content<>(result);
    }

    public void setFuture(Future<T> future) {
        //setLoadingState(true);
        mFuture = future;
    }

    public Future<T> getFuture() {
        return mFuture;
    }

    protected interface StateListener<T> {

        void onBecomeDirty(PendingResultImpl<T> sender);

        void onLoaded(PendingResultImpl<T> sender);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public String toString() {
        return Objects.toString("mUri"+ mUri +"\n"+
                "mCallable"+ mCallable +"\n"+
                "mStateListener"+ mStateListener +"\n"+
                "mContentResult"+ mContentResult +"\n"+
                "mFuture"+ mFuture +"\n"+"mLoading"+ mLoading +"\n"+"mDirty"+ mDirty +"\n");
    }
}
