package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected ProductEntity() {
    }

    private ProductEntity(Long id, String name, Price price) {
        this.id = id;
        this.name = new Name(name);
        this.price = price;
    }

    private ProductEntity(String name, Price price) {
        this.name = new Name(name);
        this.price = price;
    }

    public static ProductEntity of(String name, BigDecimal price) {
        return new ProductEntity(name, new Price(price));
    }

    public static ProductEntity of(Long id, String name, BigDecimal price) {
        return new ProductEntity(id, name, new Price(price));
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
