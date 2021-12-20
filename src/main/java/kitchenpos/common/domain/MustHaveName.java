package kitchenpos.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidArgumentException;

@Embeddable
public class MustHaveName {
    @Column(nullable = false)
    private String name;

    protected MustHaveName() {
    }

    private MustHaveName(String name) {
        validate(name);
        this.name = name;
    }

    public static MustHaveName valueOf(String name) {
        return new MustHaveName(name);
    }

    /**
     * 이름은 not null
     *
     * @param name
     */
    private void validate(String name) {
        if (Objects.isNull(name)) {
            throw new InvalidArgumentException("이름은 필수입니다.");
        }
    }

    public String get() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o.getClass().equals(String.class)) {
            return name.equals(o);
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MustHaveName other = (MustHaveName) o;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
