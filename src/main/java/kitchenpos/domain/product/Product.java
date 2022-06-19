package kitchenpos.domain.product;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.domain.Name;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Product() {}

    private Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = Name.from(name);
        this.price = price;
    }

    private Product(String name, BigDecimal price) {
        this.name = Name.from(name);
        this.price = price;
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
