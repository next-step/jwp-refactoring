package kitchenpos.common.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Name {
    private String name;

    protected Name() {
    }

    public Name(String name) {
        validateNameIsNullOrEmpty(name);
        this.name = name;
    }

    public String toString() {
        return name;
    }

    protected void validateNameIsNullOrEmpty(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("이름은 Null이거나 공백일 수 없습니다.");
        }
    }
}
