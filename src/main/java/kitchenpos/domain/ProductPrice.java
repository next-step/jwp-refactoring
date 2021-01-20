package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class ProductPrice {
    @Column(nullable = false)
    private BigDecimal price;

    public ProductPrice(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    protected ProductPrice() {
    }

    public BigDecimal multiply(Long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getPrice() {
        return price;
    }

    private void validate(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Product의 price는 필수입니다.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product의 price는 0이상이어야합니다.");
        }
    }
}
