package kitchenpos.common.domain;

import io.micrometer.core.instrument.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {
    private static final String ERROR_MESSAGE_INVALID_NAME = "이름이 비어있습니다.";

    @Column(name = "name", nullable = false)
    private String name;

    protected Name() {
    }

    public Name(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_NAME);
        }
    }

    public String getName() {
        return name;
    }
}
