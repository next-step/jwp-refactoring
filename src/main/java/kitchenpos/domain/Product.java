package kitchenpos.domain;

import kitchenpos.exception.NullPriceException;

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
    private String name;
    private Price price;

    public Product() {
    }

    public Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public static Product of(String name, BigDecimal price) {
        validateNullPrice(price);
        return new Product(name, new Price(price));
    }

    private static void validateNullPrice(BigDecimal price) {
        if (price == null) {
            throw new NullPriceException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(final Price price) {
        this.price = price;
    }
}
