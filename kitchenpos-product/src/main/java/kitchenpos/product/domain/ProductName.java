package kitchenpos.product.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.NameValidator;
import kitchenpos.exception.ExceptionMessage;

@Embeddable
public class ProductName {

    @Column(nullable = false)
    private String name;

    protected ProductName() {
    }

    private ProductName(String name) {
        this.name = name;
    }

    public static ProductName from(String name) {
        NameValidator.checkNotNull(name, ExceptionMessage.INVALID_PRODUCT_NAME_SIZE);
        return new ProductName(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductName that = (ProductName) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
