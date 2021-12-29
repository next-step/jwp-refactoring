package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    protected Product() {}

    private Product(String name, int price) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
    }

    public Product(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = BigDecimal.valueOf(price);
    }

    public static Product of(String name, int price) {
        return new Product(name, price);
    }

    public static Product of(Long id, String name, int price) {
        return new Product(id, name, price);
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
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void createId(Long id) {
        this.id = id;
    }
}
