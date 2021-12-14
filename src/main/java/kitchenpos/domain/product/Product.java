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
import kitchenpos.domain.Price;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {}

    private Product(Long id) {
        this.id = id;
    }

    private Product(Long id, String name, BigDecimal price) {
        this(name, price);
        this.id = id;
    }

    private Product(String name, BigDecimal price) {
        this.name = Name.from(name);
        this.price = Price.from(price);
    }

    public static Product from(Long id) {
        return new Product(id);
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

    public Price getPrice() {
        return price;
    }
}
