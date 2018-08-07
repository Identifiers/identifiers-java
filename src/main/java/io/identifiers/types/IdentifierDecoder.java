package io.identifiers.types;

import java.io.IOException;

import io.identifiers.Identifier;

import org.msgpack.core.MessageUnpacker;

interface IdentifierDecoder {
    Identifier decode(MessageUnpacker unpacker) throws IOException;
}
