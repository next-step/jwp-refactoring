package kitchenpos.product.domain;

import kitchenpos.product.exception.InputProductDataErrorCode;
import kitchenpos.product.exception.InputProductDataException;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    public Product(Long id, String name, BigDecimal price) {
        this(name, price);
        this.id = id;
    }

    public Product(String name, BigDecimal price) {
        checkValidPrice(price);
        this.name = name;
        this.price = price;
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

    private void checkValidPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InputProductDataException(InputProductDataErrorCode.IT_CAN_NOT_INPUT_PRICE_LESS_THAN_ZERO);
        }
    }
}
