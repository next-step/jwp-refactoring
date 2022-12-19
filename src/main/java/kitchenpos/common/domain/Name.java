package kitchenpos.common.domain;

import io.micrometer.core.instrument.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
    private static final String EXCEPTION_MESSAGE_EMPTY_NAME = "이름은 공백일 수 없습니다";
    @Column(nullable = false)
    private String name;

    protected Name() {
    }

    public Name(String name) {
        validateName(name);
        this.name = name;
    }

    public String value() {
        return name;
    }

    private void validateName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_EMPTY_NAME);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Name)) return false;
        Name name1 = (Name) o;
        return Objects.equals(value(), name1.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value());
    }
}
