package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "name", nullable = false))
    private Name name;
    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "price", nullable = false))
    private Price price;

    protected Product() {
    }

    public Product(final Long id, final Name name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(final Long id, final Name name, final Price price) {
        return new Product(id, name, price);
    }

    public static Product of(final Long id, final String name, final BigDecimal price) {
        return new Product(id, Name.from(name), Price.from(price));
    }

    public static Product of(final String name, final BigDecimal price) {
        return new Product(null, Name.from(name), Price.from(price));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.toName();
    }

    public Price getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
