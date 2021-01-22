package kitchenpos.menu.domain;

import javafx.beans.binding.Bindings;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    protected  Product() {
    }

    public Product(String name, BigDecimal price) {
        validateLessThanZero(price);
        this.name = name;
        this.price = price;
    }

    private void validateLessThanZero(BigDecimal price) {
        if (BigDecimal.ZERO.compareTo(price) > 0) {
            throw new IllegalArgumentException("가격이 0보다 적을 수 없습니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
