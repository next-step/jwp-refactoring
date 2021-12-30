package kitchenpos.moduledomain.product;

import static kitchenpos.moduledomain.common.exception.Message.PRODUCT_NAME_IS_NOT_EMPTY;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Amount price;

    public static Product of(final String name, final Long price) {
        requireName(name);

        return new Product(null, name, Amount.of(price));
    }

    public static Product of(final Long id, final String name, final Long price) {
        requireName(name);
        return new Product(id, name, Amount.of(price));
    }

    private static void requireName(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException(PRODUCT_NAME_IS_NOT_EMPTY.getMessage());
        }
    }

    private Product(Long id, String name, Amount price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    protected Product() {
    }

    public Amount multiply(Long quantity) {
        return price.multiply(quantity);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
