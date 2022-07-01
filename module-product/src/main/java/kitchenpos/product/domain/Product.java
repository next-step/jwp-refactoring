package kitchenpos.product.domain;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
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

    private Product(Long id, String name, Price price) {
        this.id = id;
        this.name = new Name(name);
        this.price = price;
    }

    private Product(String name, Price price) {
        this.name = new Name(name);
        this.price = price;
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, new Price(price));
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, name, new Price(price));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
