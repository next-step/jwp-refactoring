package kitchenpos.common.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.StringUtils;

import kitchenpos.common.exception.BadRequestException;

@Embeddable
public class Name {

    @Column
    private String name;

    protected Name() {
    }

    public Name(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (!StringUtils.hasText(name)) {
            throw new BadRequestException(REQUIRED_NAME);
        }
    }

    public String getValue() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Name name1 = (Name)o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
