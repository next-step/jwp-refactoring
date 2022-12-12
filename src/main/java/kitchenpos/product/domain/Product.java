package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;

    protected Product() {}

    private Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = Name.from(name);
        this.price = Price.from(price);
    }

    private Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public Long id() {
        return id;
    }

    public Name name() {
        return name;
    }

    public Price price() {
        return price;
    }

    public String nameValue() {
        return name.value();
    }

    public BigDecimal priceValue() {
        return price.value();
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
