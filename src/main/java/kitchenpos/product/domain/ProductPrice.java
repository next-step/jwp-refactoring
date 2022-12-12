package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {

    @Column(nullable = false)
    private BigDecimal amount;

    public ProductPrice(){

    }

    private ProductPrice(BigDecimal amount) {
        this.amount = amount;
    }

    public static ProductPrice of(Long amount) {
        validateMoney(amount);
        return new ProductPrice(BigDecimal.valueOf(amount));
    }

    private static void validateMoney(Long amount) {
        if (Objects.isNull(amount)) {
            throw new IllegalArgumentException();
        }
        if (BigDecimal.valueOf(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal value() {
        return amount;
    }
}
