package kitchenpos.domain.product;

import kitchenpos.utils.Validator;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import static kitchenpos.utils.Message.INVALID_NAME_EMPTY;

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
        Validator.checkNotNull(name, INVALID_NAME_EMPTY);
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
