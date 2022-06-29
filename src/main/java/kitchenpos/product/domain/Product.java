package kitchenpos.product.domain;

import kitchenpos.generic.Price;

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

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price unitPrice;

    protected Product() {
    }

    public Product(final String name, final BigDecimal unitPrice) {
        this.name = name;
        this.unitPrice = new Price(unitPrice);
    }

    public Product(final String name, final long unitPrice) {
        this.name = name;
        this.unitPrice = new Price(BigDecimal.valueOf(unitPrice));
    }

    public Product(final Long id, final String name, final long unitPrice) {
        this.id = id;
        this.name = name;
        this.unitPrice = new Price(BigDecimal.valueOf(unitPrice));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice.getValue();
    }

    public BigDecimal getPrice(final Long quantity) {
        return unitPrice.getPrice(quantity);
    }
}
