package org.parmenid.jsontestmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.converter.ArgumentConversionException;

final class JsonConverter {
    private final ObjectMapper mapper;

    public JsonConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Object convert(String source, Class<?> target) {
        System.out.println(source);
        try {
            return mapper.readValue(source, target);
        } catch (JsonProcessingException e){
            throw new ArgumentConversionException("Can't convert " + source +
                    " to type: " + target.getName() + ". Check json format");
        }
    }
}
