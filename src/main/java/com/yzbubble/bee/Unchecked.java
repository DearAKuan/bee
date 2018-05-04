package com.yzbubble.bee;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Deprecated
public class Unchecked {
    public static <T> Supplier<T> of(UnchekedSupplier<T> unchekedSupplier) {
        Objects.requireNonNull(unchekedSupplier);
        return () -> {
            try {
                return unchekedSupplier.get();
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T> Consumer<T> of(UncheckedConsumer<T> unchekedConsumer) {
        Objects.requireNonNull(unchekedConsumer);
        return obj -> {
            try {
                unchekedConsumer.accept(obj);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T, R> Function<T, R> of(UncheckedFunction<T, R> unchekedFunction) {
        Objects.requireNonNull(unchekedFunction);
        return obj -> {
            try {
                return unchekedFunction.apply(obj);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static Runnable of(UncheckedRunnable uncheckedRunnable) {
        Objects.requireNonNull(uncheckedRunnable);
        return () -> {
            try {
                uncheckedRunnable.run();
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @FunctionalInterface
    public static interface UnchekedSupplier<T> {
        T get() throws Throwable;
    }

    @FunctionalInterface
    public static interface UncheckedConsumer<T> {
        void accept(T obj) throws Throwable;
    }

    @FunctionalInterface
    public static interface UncheckedFunction<T, R> {
        R apply(T obj) throws Throwable;
    }

    @FunctionalInterface
    public static interface UncheckedRunnable {
        void run() throws Throwable;
    }
}
