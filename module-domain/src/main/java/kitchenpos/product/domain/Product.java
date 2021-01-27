package kitchenpos.product.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    protected Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(Long id, String name, BigDecimal price) {
        this(name, price);
        this.id = id;
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        validationCheck();
    }

    public BigDecimal calculatePrice(long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    private void validationCheck() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격정보가 잘못되었습니다.");
        }
    }
}
