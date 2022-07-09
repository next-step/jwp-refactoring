package kitchenpos.product.domain;

import kitchenpos.product.dto.ProductRequest;

import javax.persistence.Column;
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
    @Column
    private String name;
    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(Long productId) {
        this(productId, null, Price.from(0));
    }

    public Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public Product(ProductRequest productRequest) {
        this(null, productRequest.getName(), Price.from(productRequest.getPrice()));
    }

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public Integer getPriceValue() {
        return price.getPriceValue();
    }

    public BigDecimal getPriceBigDecimal() {
        return price.getValue();
    }

    public void setPrice(final BigDecimal price) {
        this.price = Price.from(price);
    }
}
