package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(final Long id, final Name name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(final Long id, final String name, final long price) {
        return new Product(id, Name.from(name), Price.valueOf(price));
    }

    public static Product of(final String name, final long price) {
        return new Product(null, Name.from(name), Price.valueOf(price));
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
