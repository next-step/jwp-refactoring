package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price unitPrice;

    protected ProductEntity() {
    }

    public ProductEntity(String name, BigDecimal unitPrice) {
        this.name = name;
        this.unitPrice = new Price(unitPrice);
    }

    public ProductEntity(Long id, String name, Long unitPrice) {
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

    public Long getUnitPrice() {
        return unitPrice.getValue().longValue();
    }
}
