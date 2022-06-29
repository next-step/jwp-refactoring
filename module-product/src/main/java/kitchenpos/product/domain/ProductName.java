package kitchenpos.product.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.product.exception.EmptyNameException;
import kitchenpos.product.utils.StringUtil;

@Embeddable
public class ProductName {
    @Column(name = "name", nullable = false)
    private String value;

    protected ProductName() {}

    private ProductName(String name) {
        this.value = name;
    }

    public static ProductName from(String name) {
        validateName(name);
        return new ProductName(name);
    }

    public String getValue() {
        return this.value;
    }

    private static void validateName(String name) {
        if (StringUtil.isEmpty(name)) {
            throw new EmptyNameException(name);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductName name = (ProductName) o;
        return Objects.equals(getValue(), name.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
