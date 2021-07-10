package kitchenpos.domain;

import kitchenpos.common.exception.InvalidNameException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
    private String name;

    protected Name() {
    }

    public Name(String name) {
        validate(name);

        this.name = name;
    }

    private void validate(String name) {
        if (Objects.isNull(name)) {
            throw new InvalidNameException();
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
