package kitchenpos.utils;

import java.util.UUID;

public final class TestUtils {
    private TestUtils() {}

    public static Long getRandomId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
