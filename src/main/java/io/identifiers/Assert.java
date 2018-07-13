package io.identifiers;

public final class Assert {

    public static void argumentExists(Object arg, String formattedMessage, Object... args) {
        if (arg == null) {
            throw new IllegalArgumentException(String.format(formattedMessage, args));
        }
    }

    public static void state(final boolean state, final String formattedMessage, Object... args) {
        if (!state) {
            throw new IllegalStateException(String.format(formattedMessage, args));
        }
    }
}
