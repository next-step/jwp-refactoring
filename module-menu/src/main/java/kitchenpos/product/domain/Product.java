package kitchenpos.product.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

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

    private Product(Name name, Price price) {
        this.name = name;
        this.price = price;
    }

    private Product(Long id, Name name, Price price) {
        this(name, price);
        this.id = id;
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(Name.of(name), Price.of(price));
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, Name.of(name), Price.of(price));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public Price getPrice() {
        return price;
    }
}
