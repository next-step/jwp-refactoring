package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private ProductPrice price;

    public Product(Long id, String name, ProductPrice price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, ProductPrice price) {
        this(null, name, price);
    }

    public Product(String name, int price) {
        this(null, name, new ProductPrice(price));
    }

    public Product(String name, BigDecimal price) {
        this(null, name, new ProductPrice(price));
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

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public void setPrice(final BigDecimal price) {
        this.price = new ProductPrice(price);
    }
}
