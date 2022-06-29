package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {
    @Column(nullable = false)
    private BigDecimal price;

    protected ProductPrice() {

    }

    public ProductPrice(BigDecimal price) {
        this.price = price;
    }

    public void minimumCheck() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품의 가격은 0원 미만일 수 없습니다.");
        }
    }

    public BigDecimal calculateAmount(long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getPrice() {
        return price;
    }
}
