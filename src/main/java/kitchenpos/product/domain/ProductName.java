package kitchenpos.product.domain;

import org.springframework.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductName {
    private static final String NAME_NOT_ALLOW_NULL_OR_EMPTY = "상품명은 비어있거나 공백일 수 없습니다.";

    @Column(name = "name")
    private String value;

    protected ProductName() {}

    public ProductName(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (ObjectUtils.isEmpty(value)) {
            throw new IllegalArgumentException(NAME_NOT_ALLOW_NULL_OR_EMPTY);
        }
    }

    public String value() {
        return value;
    }
}
