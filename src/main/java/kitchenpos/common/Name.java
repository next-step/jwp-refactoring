package kitchenpos.common;

import org.apache.logging.log4j.util.Strings;

public class Name {

    public static final String NAME_NOT_EMPTY_EXCEPTION_MESSAGE = "이름은 null 이나 공백일 수 없습니다.";

    private final String name;

    public Name(String name) {
        validateName(name);
        this.name = name;
    }

    private static void validateName(String name) {
        if (Strings.isEmpty(name)) {
            throw new IllegalArgumentException(NAME_NOT_EMPTY_EXCEPTION_MESSAGE);
        }
    }
}
