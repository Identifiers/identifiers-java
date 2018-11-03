package io.identifiers;

/**
 * Used internally to test values at runtime.
 */
public final class Assert {

    private Assert() {
        // static class
    }

    public static void argumentExists(Object arg, String formattedMessage, Object... args) {
        if (arg == null) {
            throw new IllegalArgumentException(String.format(formattedMessage, args));
        }
    }

    public static void state(boolean state, String formattedMessage, Object... args) {
        if (!state) {
            throw new IllegalStateException(String.format(formattedMessage, args));
        }
    }
}
