package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private static final int MINIMUM_PRICE = 0;
    public static final String INVALID_PRODUCT_PRICE_ERROR_MESSAGE = "상품 가격이 올바르지 않습니다. 0원 이상의 가격을 입력해주세요.";

    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {

    }

    private Product(String name, BigDecimal price) {
        validatePrice(price);
        this.name = name;
        this.price = price;
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price);
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MINIMUM_PRICE) {
            throw new IllegalArgumentException(INVALID_PRODUCT_PRICE_ERROR_MESSAGE);
        }
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
}
