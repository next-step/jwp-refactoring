package kitchenpos.product.domain.wrap;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

@Embeddable
@AccessType(Type.FIELD)
public class Price {
    private static final int MINIMUM_PRICE = 0;
    public static final String INVALID_PRODUCT_PRICE_ERROR_MESSAGE = "가격이 올바르지 않습니다. 0원 이상의 가격을 입력해주세요.";

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {

    }

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        validatePriceIsZero(price);
        return new Price(price);
    }

    public static void validatePriceIsZero(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MINIMUM_PRICE) {
            throw new IllegalArgumentException(INVALID_PRODUCT_PRICE_ERROR_MESSAGE);
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
