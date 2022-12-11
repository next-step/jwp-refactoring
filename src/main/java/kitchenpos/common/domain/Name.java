package kitchenpos.common.domain;

import kitchenpos.common.constant.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
            throw new IllegalArgumentException(ErrorCode.NAME_SHOULD_NOT_EMPTY.getMessage());
        }
    }

    public String value() {
        return name;
    }
}
