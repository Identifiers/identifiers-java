package io.identifiers.types;

import java.util.Collections;
import java.util.Map;

class ImmutableValueMapTypeTemplate<T> extends TypeTemplateWithEncoder<Map<String, T>> {

    ImmutableValueMapTypeTemplate(final IdentifierEncoder<Map<String, T>> encoder) {
        super(encoder);
    }

    @Override
    public Map<String, T> initialValue(Map<String, T> value) {
        return Collections.unmodifiableMap(value);
    }
}
