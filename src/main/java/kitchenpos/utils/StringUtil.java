package kitchenpos.utils;

public class StringUtil {
    private StringUtil() {}
    public static boolean isEmpty(String name) {
        return name == null || name.length() == 0;
    }
}
