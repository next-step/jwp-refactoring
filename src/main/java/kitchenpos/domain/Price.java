package kitchenpos.domain;

import kitchenpos.exception.IllegalPriceException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * packageName : kitchenpos.domain
 * fileName : Price
 * author : haedoang
 * date : 2021/12/20
 * description :
 */
@Embeddable
public class Price {
    @Transient
    public static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    public Price (BigDecimal value) {
        validate(value);
        this.price = value;
    }

    private void validate(BigDecimal value) {
        if(Objects.isNull(value) || MIN_PRICE.compareTo(value) > 0) {
            throw new IllegalPriceException();
        }
    }

    public static Price of(BigDecimal value) {
        return new Price(value);
    }

    public BigDecimal value() {
        return price;
    }
}
