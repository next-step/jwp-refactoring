package kitchenpos.product.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class ProductName {
    private String name;

    protected ProductName() {
    }

    public ProductName(String name) {
        validateNameIsNullOrEmpty(name);
        this.name = name;
    }

    public String toString() {
        return name;
    }

    private void validateNameIsNullOrEmpty(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 Null이거나 공백일 수 없습니다.");
        }
    }
}
