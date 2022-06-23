package kitchenpos.common.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(name = "price")
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public static Price of(Integer price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public BigDecimal getValue() {
        return price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격이 0보다 작을 수 없습니다.");
        }
    }

    public Price add(Price other) {
        return Price.of(price.add(other.price));
    }

    public static Price multiply(Product product, Quantity quantity) {
        return Price.of(product.getPriceValue()
                .multiply(BigDecimal.valueOf(quantity.getValue())));
    }

    public boolean isGreaterThan(Price other) {
        return price.compareTo(other.price) > 0;
    }
}
