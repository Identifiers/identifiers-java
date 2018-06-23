package benchmarks;

import com.github.javafaker.Faker;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CodecBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
            .include(CodecBenchmark.class.getName() + ".*")
            .warmupIterations(3)
            .forks(1)
            .measurementIterations(1)
            .build();
        new Runner(opts).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        byte[][] randomBytes;
        byte[][] strBytes;

        @Setup(Level.Trial)
        public void setUp() {
            randomBytes = new byte[10][];
            Random random = new Random();
            for (int i = 0; i < randomBytes.length; i++) {
                randomBytes[i] = new byte[i + 1];
                random.nextBytes(randomBytes[i]);
            }

            Collection<String> strings = new ArrayList<>();
            Faker faker = new Faker();
            for (int i = 0; i < 2; i++) {
                strings.add(faker.lorem().sentence());
                strings.add(faker.internet().url());
                strings.add(faker.internet().emailAddress());
                strings.add(faker.date().past(1000, TimeUnit.DAYS).toString());
                strings.add(UUID.randomUUID().toString());
            }

            strBytes = strings.stream()
                    .map(this::toMsgPackBytes)
                    .collect(Collectors.toList())
                    .toArray(new byte[][]{});
        }

        private byte[] toMsgPackBytes(String str) {
            try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker()) {
                packer.packString(str);
                return packer.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void roundTripBase32(byte[] bytes) {
        String encoded = io.identifiers.base32.Encoder.encode(bytes);
        byte[] decoded = io.identifiers.base32.Decoder.decode(encoded);
        if (decoded == null) {
            throw new RuntimeException(String.format("Could not decode %s", encoded));
        }
    }

    void roundTripBase128(byte[] bytes) {
        String encoded = io.identifiers.base128.Encoder.encode(bytes);
        byte[] decoded = io.identifiers.base128.Decoder.decode(encoded);
        if (decoded == null) {
            throw new RuntimeException(String.format("Could not decode %s", encoded));
        }
    }

    @Benchmark
    public void timeBase32RandomBytes(BenchmarkState state) {
        roundTripBase32(state.randomBytes[(int) Math.floor(Math.random() * 10)]);
    }

    @Benchmark
    public void timeBase32RandomValues(BenchmarkState state) {
        roundTripBase32(state.strBytes[(int) Math.floor(Math.random() * 10)]);
    }

    @Benchmark
    public void timeBase128RandomBytes(BenchmarkState state) {
        roundTripBase128(state.randomBytes[(int) Math.floor(Math.random() * 10)]);
    }

    @Benchmark
    public void timeBase128RandomValues(BenchmarkState state) {
        roundTripBase128(state.strBytes[(int) Math.floor(Math.random() * 10)]);
    }
}
