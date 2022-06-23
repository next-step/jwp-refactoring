package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.domain.common.Name;
import kitchenpos.domain.common.Price;

public class Product {
    private Long id;
    private Name name;
    private Price price;

    public Product() {
    }

    public Product(String name, BigDecimal price) {
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = new Name(name);
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = new Price(price);
    }
}
