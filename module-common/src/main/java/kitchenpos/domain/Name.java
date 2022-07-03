package kitchenpos.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionType;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(nullable = false, name = "name")
    private String value;

    protected Name() {
    }

    public Name(String value) {
        if (!StringUtils.hasText(value)) {
            throw new BadRequestException(ExceptionType.INVALID_NAME);
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
