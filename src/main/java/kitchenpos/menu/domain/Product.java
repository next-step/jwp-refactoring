package kitchenpos.menu.domain;

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

    public Product() {
    }

    public Product(Long id, String name, long price) {
        this.id = id;
        this.name = Name.of(name);
        this.price = Price.of(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public long getPrice() {
        return price.longValue();
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
