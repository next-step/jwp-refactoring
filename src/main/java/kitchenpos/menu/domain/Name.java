package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.core.exception.BadRequestException;
import kitchenpos.core.exception.ExceptionType;
import org.springframework.util.StringUtils;

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
