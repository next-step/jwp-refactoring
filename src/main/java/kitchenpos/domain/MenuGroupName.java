package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidMenuGroupNameSizeException;
import org.springframework.util.StringUtils;

@Embeddable
public class MenuGroupName {

    @Column(nullable = false)
    private String name;

    protected MenuGroupName() {
    }

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

        throw new InvalidMenuGroupNameSizeException(ExceptionMessage.INVALID_MENU_GROUP_NAME_SIZE);
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
