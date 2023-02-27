package com.commit451.jounce;

import java.util.HashMap;

/**
 * A map of debouncers, useful for lists of items that all call through to the same underlying methods
 */
public abstract class DebouncerMap<K, T> {

    /**
     * Called upon each debouncer's completion
     * @param key the key associated with the debouncer
     * @param value the value after debouncing
     */
    public abstract void onValueChanged(K key, T value);

    private HashMap<K, Debouncer<T>> debouncerHashMap = new HashMap<>();

    private long delay;

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
        this.delay = delay;
    }

    /**
     * Get the current debounced value. Null if fetched before {@link #setValue(Object, Object)}
     * has been called.
     * @param key the key
     * @return the value of the debouncer, or null if no debouncer found for key
     */
    public T getValue(K key) {
        Debouncer<T> debouncer = debouncerHashMap.get(key);
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
        if (debouncerHashMap.containsKey(key)) {
            Debouncer<T> debouncer = debouncerHashMap.get(key);
            debouncer.setValue(value);
        } else {
            Debouncer<T> debouncer = new KeyedDebouncer(key, delay);
            debouncerHashMap.put(key, debouncer);
            debouncer.setValue(value);
        }
    }

    /**
     * Cancel the debounce of the debouncer with the key
     * @param key key
     */
    public void cancel(K key) {
        Debouncer<T> debouncer = debouncerHashMap.get(key);
        if (debouncer != null) {
            debouncer.cancel();
        }
    }

    /**
     * Cancel all the debouncers in the map
     */
    public void cancelAll() {
        for (Debouncer debouncer : debouncerHashMap.values()) {
            debouncer.cancel();
        }
    }

    /**
     * Immediately call {@link #onValueChanged(Object, Object)} of the debouncer with the given key
     * @param key key
     */
    public void call(K key) {
        Debouncer<T> debouncer = debouncerHashMap.get(key);
        if (debouncer != null) {
            debouncer.call();
        }
    }

    /**
     * Immediately call {@link #onValueChanged(Object, Object)} for all the debouncers in the map
     */
    public void callAll() {
        for (Debouncer debouncer : debouncerHashMap.values()) {
            debouncer.call();
        }
    }

    private class KeyedDebouncer extends Debouncer<T> {

        private K key;

        public KeyedDebouncer(K key, long delay) {
            super(delay);
            this.key = key;
        }

        @Override
        public void onValueSet(T value) {
            debouncerHashMap.remove(key);
            onValueChanged(key, value);
        }
    }

}
