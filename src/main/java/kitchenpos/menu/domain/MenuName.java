package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class MenuName {
    private String name;

    protected MenuName() {
    }

    public MenuName(String name) {
        if (Objects.isNull(name) || name.trim().isEmpty()) {
            throw new IllegalArgumentException("메뉴 이름은 Null 또는 공백일 수 없습니다.");
        }
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
