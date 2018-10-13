package io.identifiers.types;

import java.io.IOException;

import io.identifiers.Assert;
import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

/**
 * Composite identifiers do not have to worry about the mutability of their identifier
 * contents because they manage their own mutability. The user will discover that attempting
 * to modify a mutable identifier value, like byte[], will not affect either that identifier
 * or the composite identifier that contains it.
 */
// Lives in this package so it can access IdentifierDecoders and ValueCodecProvider
class CompositeIdentifierValueCodec implements ValueCodec<Identifier<?>> {

    @Override
    @SuppressWarnings("unchecked")
    public Value encode(Identifier<?> value) {
        IdentifierType type = value.type();
        ValueCodec codec = ValueCodecProvider.getCodec(type);
        Assert.argumentExists(codec, "No codec for type %s", type);
        /*
          This is a copy of logic in AbstractIdentifierEncoder but to share it would require some big holes pushed
          through Identifier, so copying is safer.
         */
        return ValueFactory.newArray(new Value[] {
            ValueFactory.newInteger(type.code()),
            codec.encode(value.value())
        }, true);
    }

    @Override
    public Identifier<?> decode(MessageUnpacker unpacker) throws IOException {
        return IdentifierDecoderSupport.unpackIdentifier(unpacker);
    }
}
