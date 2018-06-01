package io.identifiers.types;

import io.identifiers.Identifier;

public class StringIdentifier implements Identifier<String> {

    private final String value;

    StringIdentifier(String value) {
        this.value = value;
    }

    @Override
    public TYPE type() {
        return TYPE.STRING;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toDataString() {
        return null;
    }

    @Override
    public String toHumanString() {
        return null;
    }
}
