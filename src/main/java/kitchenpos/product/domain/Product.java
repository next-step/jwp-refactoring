package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

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

    private Product(String name, Integer price) {
        this.name = Name.of(name);
        this.price = Price.of(price);
    }

    public Product(Long id, String name, Integer price) {
        this.id = id;
        this.name = Name.of(name);
        this.price = Price.of(price);
    }

    private Product(Long id) {
        this.id = id;
    }

    public static Product of(String name, Integer price) {
        if (price == null) {
            throw new IllegalArgumentException();
        }
        return new Product(name, price);
    }

    public static Product of(Long id) {
        return new Product(id);
    }

    public void priceValidate() {
        price.validate();
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

    public BigDecimal getPriceValue() {
        return price.getValue();
    }

}
