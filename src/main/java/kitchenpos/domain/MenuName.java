package kitchenpos.domain;

import java.util.Objects;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidMenuNameSizeException;
import org.springframework.util.StringUtils;

public class MenuName {
    private String name;

    private MenuName(String name) {
        this.name = name;
    }

    public static MenuName from(String name) {
        checkNotNull(name);
        return new MenuName(name);
    }

    private static void checkNotNull(String name) {
        if (StringUtils.hasText(name)) {
            return;
        }

        throw new InvalidMenuNameSizeException(ExceptionMessage.INVALID_MENU_NAME_SIZE);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuName menuName = (MenuName) o;
        return name.equals(menuName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
