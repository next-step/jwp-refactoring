package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final Long id;
    private final String name;
    private final ProductPrice price;

    public Product(final Long id, final String name, final BigDecimal price) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = new ProductPrice(price);
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException("상품의 가격은 반드시 있어야 하며, 0원 이상이어야 합니다.");
        }
    }
}
