package io.identifiers.types;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;
import org.junit.jupiter.api.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static io.identifiers.IdentifierType.Modifiers.SEMANTIC_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdentifierDecoderProviderTest {

    @Test
    void testFindDecoderForUnknownSemanticType() throws IOException {
        int slot = 256;
        Integer unknownSemanticType = IdentifierType.INTEGER_MAP_MAP.code() | SEMANTIC_TYPE | (slot << 0x8);
        IdentifierDecoder unknownDecoder = IdentifierDecoderProvider.findDecoder(unknownSemanticType);
        assertThat(unknownDecoder).isNotNull();

        Map<String, Map<String, Integer>> intMapMapValue =
            Collections.singletonMap("a",
                Collections.singletonMap("b", 100));
        ValueCodec<Map<String, Map<String, Integer>>> intMapMapCodec = ValueCodecProvider.getCodec(IdentifierType.INTEGER_MAP_MAP);
        Value encodedValue = intMapMapCodec.encode(intMapMapValue);
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packValue(encodedValue);
        byte[] bytes = packer.toByteArray();
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(bytes);
        Identifier actualDecoded = unknownDecoder.decode(unpacker);

        assertThat(actualDecoded.type().name()).isEqualTo("unknown-integer-map-map");
        assertThat(actualDecoded.type().code()).isEqualTo(unknownSemanticType);
        assertThat(actualDecoded.value()).isEqualTo(intMapMapValue);
    }

    @Test
    void testFindDecoderFailsForUnknownType() {
        assertThatThrownBy(() -> IdentifierDecoderProvider.findDecoder(-1))
            .isInstanceOf(IllegalStateException.class);
    }
}