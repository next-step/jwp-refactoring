package kitchenpos.product.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ProductName implements Name {

    private String name;

    private ProductName(String name) {
        validLength(name);
        this.name = name;
    }

    public static ProductName from(String name) {
        return new ProductName(name);
    }

    protected ProductName() {
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
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
