package org.parmenid.jsontestmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public final class JsonArgumentsProvider implements AnnotationConsumer<JsonTestMapper>, ArgumentsProvider {
    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;
    private String[] resources;
    private final JsonConverter jsonConverter;

    @Override
    public void accept(JsonTestMapper testJsonMapper) {
        this.resources = testJsonMapper.resources();
    }

    JsonArgumentsProvider() {
        this(Class::getResourceAsStream);
    }
    JsonArgumentsProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
        this.jsonConverter = new JsonConverter(new ObjectMapper());
    }

    private static String values(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream);
        int value = 0;
        while (true) {
            try {
                if ((value = reader.read()) == -1) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            output.append((char) value);
        }
        return output.toString();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        Class<?>[] parameterTypes = context.getRequiredTestMethod().getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException("Arguments count: " + parameterTypes.length + ". JsonMapper supports only one as test's arguments");
        }
        Stream<?> jsonStream = JsonMapperUtil.resourceIsFile(this.resources)
                ? fromFile(context)
                : fromString();
            return jsonStream
                .flatMap(item -> {
                    String json = (String) item;
                    if (JsonMapperUtil.isArray(json)) {
                        if (!JsonMapperUtil.isArrayOrCollection(parameterTypes[0])) {
                            return Arrays.stream((Object[]) this.jsonConverter.convert(json, parameterTypes[0].arrayType()));
                        }
                    }
                    return Stream.of(this.jsonConverter.convert(json, parameterTypes[0]));
                })
            .map(Arguments::arguments);
    }


    private InputStream openInputStream(ExtensionContext context, String resource) {
        Class<?> testClass = context.getRequiredTestClass();
        InputStream inputStream = inputStreamProvider.apply(testClass, resource);
        return Preconditions.notNull(inputStream,
            () -> "Classpath resource does not exist: " + resource);
    }

    private Stream<?> fromFile(ExtensionContext context) {
        return Arrays.stream(this.resources)
                .map(resource -> openInputStream(context, resource))
                .map(JsonArgumentsProvider::values);
    }

    private Stream<?> fromString() {
        return Arrays.stream(this.resources);
    }
}
