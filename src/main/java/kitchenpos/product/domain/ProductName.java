package kitchenpos.product.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductName {
    @Column(nullable = false)
    private String name;

    protected ProductName() {
    }

    private ProductName(String name) {
        validate(name);
        this.name = name;
    }

    public static ProductName valueOf(String name) {
        return new ProductName(name);
    }

    /**
     * 이름은 not null
     *
     * @param name
     */
    private void validate(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
    }

    public String get() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o.getClass().equals(String.class)) {
            return name.equals(o);
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductName other = (ProductName) o;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
