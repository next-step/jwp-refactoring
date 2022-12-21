package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal price;

    protected ProductPrice() {
    }

    public ProductPrice(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("상품 등록시, 가격은 반드시 입력 되어야 합니다");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품 가격은 최소 0원 이상이어야 합니다");
        }
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
