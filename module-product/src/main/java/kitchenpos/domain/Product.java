package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = Price.of(price);
    }

    public static Product of(final String name, final BigDecimal price) {
        return new Product(null, name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
