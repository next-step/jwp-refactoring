package kitchenpos.menu.domain;

import kitchenpos.menu.exception.IllegalMenuPriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * packageName : kitchenpos.menu.domain
 * fileName : Price
 * author : haedoang
 * date : 2021/12/20
 * description :
 */
@Embeddable
public class MenuPrice {
    @Transient
    public static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal price;

    protected MenuPrice() {
    }

    public MenuPrice(BigDecimal value) {
        validate(value);
        this.price = value;
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value) || MIN_PRICE.compareTo(value) > 0) {
            throw new IllegalMenuPriceException();
        }
    }

    public static MenuPrice of(BigDecimal value) {
        return new MenuPrice(value);
    }

    public BigDecimal value() {
        return price;
    }
}
