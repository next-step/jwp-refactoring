package kitchenpos.product.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class ProductPrice extends Price {
    private BigDecimal price;

    protected ProductPrice() {
    }

    public ProductPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public BigDecimal multiply(BigDecimal quantity) {
        return price.multiply(quantity);
    }

    public BigDecimal getValue() {
        return price;
    }

    public void setValue(BigDecimal price) {
        this.price = price;
    }
}
