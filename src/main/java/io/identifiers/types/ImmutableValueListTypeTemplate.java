package io.identifiers.types;

import java.util.Collections;
import java.util.List;

class ImmutableValueListTypeTemplate<T> extends TypeTemplateWithEncoder<List<T>> {

    ImmutableValueListTypeTemplate(final IdentifierEncoder<List<T>> encoder) {
        super(encoder);
    }

    @Override
    public List<T> initialValue(final List<T> value) {
        return Collections.unmodifiableList(value);
    }
}
