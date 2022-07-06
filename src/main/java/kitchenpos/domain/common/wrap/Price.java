package kitchenpos.domain.common.wrap;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.domain.menu.MenuProducts;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

@Embeddable
@AccessType(Type.FIELD)
public class Price {
    public static final String INVALID_PRODUCT_PRICE_ERROR_MESSAGE = "가격이 올바르지 않습니다. 0원 이상의 가격을 입력해주세요.";
    public static final String MENU_PRICE_IS_OVER_THAN_TOTAL_SUM_OF_MENU_PRODUCT_PRICE_ERROR_MESSAGE = "메뉴에 구성된 상품 가격의 총합 보다 메뉴 가격이 클 수 없습니다.";

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {

    }

    private Price(BigDecimal price) {
        this.price = price;
    }

    private Price(BigDecimal price, MenuProducts menuProducts) {
        validateMenuPrice(price, menuProducts);
        this.price = price;
    }

    public static Price productPriceFrom(BigDecimal price) {
        validatePriceIsZero(price);
        return new Price(price);
    }

    public static Price menuPriceOf(BigDecimal price, MenuProducts menuProducts) {
        return new Price(price, menuProducts);
    }

    private static final int MINIMUM_PRICE = 0;

    public static void validatePriceIsZero(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MINIMUM_PRICE) {
            throw new IllegalArgumentException(INVALID_PRODUCT_PRICE_ERROR_MESSAGE);
        }
    }

    public void validateMenuPrice(BigDecimal price, MenuProducts menuProducts) {
        if (price.compareTo(menuProducts.totalMenuProductPrice()) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_IS_OVER_THAN_TOTAL_SUM_OF_MENU_PRODUCT_PRICE_ERROR_MESSAGE);
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
