package kitchenpos.product.domain;

import java.math.BigDecimal;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.product.domain.ProductPrice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 가격 테스트")
class ProductPriceTest {

    @DisplayName("상품 가격이 다른 객체는 동등하지 않다.")
    @Test
    void equalsTest() {
        ProductPrice price1 = ProductPrice.from(BigDecimal.valueOf(16_000));
        ProductPrice price2 = ProductPrice.from(BigDecimal.valueOf(18_000));

        Assertions.assertThat(price1).isNotEqualTo(price2);
    }

    @DisplayName("상품 가격이 같은 객체는 동등하다.")
    @Test
    void equalsTest2() {
        ProductPrice price1 = ProductPrice.from(BigDecimal.valueOf(16_000));
        ProductPrice price2 = ProductPrice.from(BigDecimal.valueOf(16_000));

        Assertions.assertThat(price1).isEqualTo(price2);
    }

    @DisplayName("상품 가격이 null 이면 예외가 발생한다.")
    @Test
    void createException() {
        Assertions.assertThatThrownBy(() -> ProductPrice.from(null))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_PRODUCT_PRICE);
    }

    @DisplayName("상품 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void createException2() {
        Assertions.assertThatThrownBy(() -> ProductPrice.from(BigDecimal.valueOf(-1)))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_PRODUCT_PRICE);
    }

}
