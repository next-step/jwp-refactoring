package kitchenpos.domain.menu;

import kitchenpos.exception.BadRequestException;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static kitchenpos.utils.Message.INVALID_NAME_EMPTY;

public class MenuGroupName {
    private String name;

    private MenuGroupName(String name) {
        this.name = name;
    }

    public static MenuGroupName from(String name) {
        checkNotNull(name);
        return new MenuGroupName(name);
    }

    private static void checkNotNull(String name) {
        if (StringUtils.hasText(name)) {
            return;
        }

        throw new BadRequestException(INVALID_NAME_EMPTY);
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
        MenuGroupName that = (MenuGroupName) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
