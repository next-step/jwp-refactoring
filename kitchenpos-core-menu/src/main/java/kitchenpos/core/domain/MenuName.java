package kitchenpos.core.domain;

import kitchenpos.core.exception.IllegalMenuNameException;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuName {
    private static final String EMPTY_ERROR_MESSAGE = "메뉴 이름은 빈 값이 될 수 없습니다.";
    @Column(nullable = false)
    private String name;

    public MenuName() {
    }

    private MenuName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalMenuNameException(EMPTY_ERROR_MESSAGE);
        }
    }

    public static MenuName of(String name) {
        return new MenuName(name);
    }

    public String getName() {
        return name;
    }

    public boolean matchName(String targetName) {
        return this.name.equals(targetName);
    }
}
