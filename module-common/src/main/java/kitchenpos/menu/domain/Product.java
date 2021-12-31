package kitchenpos.menu.domain;

import java.math.*;

import javax.persistence.*;

@Entity
public class Product {
    private static final String WRONG_PRICE_EXCEPTION_STATEMENT = "상품의 금액이 잘못되었습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    protected Product() {

    }

    public Product(String name, BigDecimal price) {
        validate(price);
        this.name = name;
        this.price = price;
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price);
    }

    private void validate(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(WRONG_PRICE_EXCEPTION_STATEMENT);
        }
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

}
