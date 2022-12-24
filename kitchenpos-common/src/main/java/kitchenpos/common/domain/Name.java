package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.error.ErrorEnum;

@Embeddable
public class Name {
    @Column(nullable = false)
    private String name;

    protected Name() {}

    public Name(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(ErrorEnum.NOT_NULL_OR_EMPTY_NAME.message());
        }
    }

    public String value() {
        return name;
    }
}
