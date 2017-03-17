package com.commit451.jounce;

import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * A map of debouncers, useful for lists of items that all call through to the same underlying methods
 */
public abstract class DebouncerMap<K, T> {

    private HashMap<K, Debouncer<T>> mDebouncerHashMap = new HashMap<>();

    private long mDelay;

    /**
     * Construct a debounce map with the default delay {@link Debouncer#DEFAULT_DELAY}
     */
    public DebouncerMap() {
        this(Debouncer.DEFAULT_DELAY);
    }

    /**
     * Construct a debounce map with a custom delay
     * @param delay the custom delay
     */
    public DebouncerMap(int delay) {
        mDelay = delay;
    }

    /**
     * Get the current debounced value. Null if fetched before {@link #setValue(Object, Object)}
     * has been called.
     * @param key the key
     * @return the value of the debouncer, or null if no debouncer found for key
     */
    @Nullable
    public T getValue(K key) {
        Debouncer<T> debouncer = mDebouncerHashMap.get(key);
        if (debouncer != null) {
            return debouncer.getValue();
        }
        return null;
    }

    /**
     * Set the value on the debouncer. See {@link Debouncer#setValue(Object)}
     * @param key the key associated with the debouncer
     * @param value the value to set
     */
    public void setValue(K key, T value) {
        if (mDebouncerHashMap.containsKey(key)) {
            Debouncer<T> debouncer = mDebouncerHashMap.get(key);
            debouncer.setValue(value);
        } else {
            Debouncer<T> debouncer = new KeyedDebouncer(key, mDelay);
            mDebouncerHashMap.put(key, debouncer);
            debouncer.setValue(value);
        }
    }

    /**
     * Cancel the debounce of the debouncer with the key
     * @param key key
     */
    public void cancel(K key) {
        Debouncer<T> debouncer = mDebouncerHashMap.get(key);
        if (debouncer != null) {
            debouncer.cancel();
        }
    }

    /**
     * Called upon each debouncer's completion
     * @param key the key associated with the debouncer
     * @param value the value after debouncing
     */
    public abstract void onValueChanged(K key, T value);

    private class KeyedDebouncer extends Debouncer<T> {

        private K mKey;

        public KeyedDebouncer(K key, long delay) {
            super(delay);
            mKey = key;
        }

        @Override
        public void onValueSet(T value) {
            mDebouncerHashMap.remove(mKey);
            onValueChanged(mKey, value);
        }
    }

}
