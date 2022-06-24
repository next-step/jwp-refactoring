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

    private Name(String name) {
        validateName(name);
        this.name = name;
    }

    public static Name of(String name) {
        return new Name(name);
    }

    private static void validateName(String name) {
        if(Objects.isNull(name)) {
            throw new IllegalArgumentException("이름을 지정해야 합니다.");
        }
    }

    public String value() {
        return name;
    }
}

