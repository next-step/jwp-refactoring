package kitchenpos.menugroup.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class MenuGroupName {
    private String name;

    protected MenuGroupName() {
    }

    public MenuGroupName(String name) {
        if (Objects.isNull(name) || name.trim().isEmpty()) {
            throw new IllegalArgumentException("메뉴그룹 이름은 Null 또는 공백일 수 없습니다.");
        }
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
