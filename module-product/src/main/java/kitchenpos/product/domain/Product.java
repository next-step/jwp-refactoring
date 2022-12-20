package kitchenpos.product.domain;

import kitchenpos.core.domain.Price;
import javax.persistence.*;
import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    public Product(String name, BigDecimal price) {
        this.name = requireNonNull(name, "name");
        this.price = new Price(price);
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
