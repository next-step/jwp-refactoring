package kitchenpos.product.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(getName(), product.getName()) && Objects.equals(getPrice(), product.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice());
    }
}
