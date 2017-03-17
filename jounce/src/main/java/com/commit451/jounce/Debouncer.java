package com.commit451.jounce;

import android.os.Handler;

/**
 * Generic debouncer to delay things like updating a count and then passing that quantity to a server
 */
public abstract class Debouncer<T> {

    public static final int DEFAULT_DELAY = 500;

    private T value;
    private Handler handler;
    private long delay = DEFAULT_DELAY;
    private Runnable valueSetRunnable = new Runnable() {
        @Override
        public void run() {
            onValueSet(value);
        }
    };

    /**
     * Create a debouncer with the default delay {@link #DEFAULT_DELAY}
     */
    public Debouncer() {
        this(DEFAULT_DELAY);
    }

    /**
     * Create a debouncer with a custom debounce time
     * @param delay the amount of time before delivering the debounce result
     */
    public Debouncer(long delay) {
        this.delay = delay;
        handler = new Handler();
    }

    /**
     * Get the value currently being debounced.
     * @return the stored value, which will be passed to {@link #onValueSet(Object)} upon completion
     */
    public T getValue() {
        return value;
    }

    /**
     * Set the value, which reset the deliver, effectively debouncing the result
     * @param value the value to set
     */
    public void setValue(T value) {
        if (this.value == null || !this.value.equals(value)) {
            this.value = value;
            handler.removeCallbacks(valueSetRunnable);
            handler.postDelayed(valueSetRunnable, delay);
        }
    }

    /**
     * Immediately deliver the value to {@link #onValueSet(Object)}
     */
    public void call() {
        onValueSet(value);
        cancel();
    }

    /**
     * Cancel the current debouncing so that {@link #onValueSet(Object)} is not called
     */
    public void cancel() {
        handler.removeCallbacks(valueSetRunnable);
    }

    /**
     * Method called when deboucing is complete and the final value should be processed
     * @param value the value
     */
    public abstract void onValueSet(T value);
}
