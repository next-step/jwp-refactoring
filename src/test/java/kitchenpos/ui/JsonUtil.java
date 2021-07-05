package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
