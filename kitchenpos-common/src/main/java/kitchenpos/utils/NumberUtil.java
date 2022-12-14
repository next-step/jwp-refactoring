package kitchenpos.utils;

public class NumberUtil {

    private static final long ZERO = 0;

    public static boolean isNotPositiveNumber(long num) {
        return num < ZERO;
    }
}
