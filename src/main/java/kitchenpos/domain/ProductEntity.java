package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;

    public ProductEntity(String name, BigDecimal price) {
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public ProductEntity() {
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
}
