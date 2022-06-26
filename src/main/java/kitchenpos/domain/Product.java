package kitchenpos.domain;

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

    private String name;

    @Embedded
    private Price unitPrice;

    protected Product() {
    }

    public Product(String name, BigDecimal unitPrice) {
        this.name = name;
        this.unitPrice = new Price(unitPrice);
    }

    public Product(Long id, String name, Long unitPrice) {
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

    public BigDecimal getPrice(Quantity quantity) {
        return unitPrice.getPrice(quantity);
    }
}
