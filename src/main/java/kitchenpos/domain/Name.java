package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(nullable = false)
    private String name;

    protected Name() {
    }

    private Name(final String name) {
        validate(name);

        this.name = name;
    }

    public static Name of(final String name) {
        return new Name(name);
    }

    public String getName() {
        return name;
    }

    private void validate(String name) {
        validateNotNull(name);
        validateNotEmptyString(name);
    }

    private void validateNotEmptyString(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("이름은 빈 문자열 일 수 없습니다.");
        }
    }

    private void validateNotNull(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("이름은 null 일 수 없습니다.");
        }
    }
}
