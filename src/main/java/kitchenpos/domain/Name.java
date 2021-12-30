package kitchenpos.domain;

import io.micrometer.core.instrument.util.StringUtils;
import kitchenpos.common.exceptions.EmptyNameException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
    @Column
    private String name;

    protected Name() {
    }

    private Name(final String name) {
        validate(name);
        this.name = name;
    }

    public static Name from(final String valueOf) {
        return new Name(valueOf);
    }

    private void validate(final String valueOf) {
        if (StringUtils.isEmpty(valueOf) || Objects.isNull(valueOf)) {
            throw new EmptyNameException();
        }
    }

    public String toName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
