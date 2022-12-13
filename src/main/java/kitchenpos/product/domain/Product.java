package kitchenpos.product.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

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

    protected Product() {}

    public Product(Name name, Price price) {
        this.name = name;
        this.price = price;
    }

    public Product(Long id, Name name, Price price) {
        this(name, price);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
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
