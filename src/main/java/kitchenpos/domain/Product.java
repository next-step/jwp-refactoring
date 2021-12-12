package kitchenpos.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Product() {}

    private Product(Long id) {
        this.id = id;
    }

    private Product(Long id, String name, BigDecimal price) {
        this(name, price);
        this.id = id;
    }

    private Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
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

    public BigDecimal getPrice() {
        return price;
    }
}
