package kitchenpos.order.domain;

import java.math.BigDecimal;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidPriceException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 메뉴 가격 테스트")
class OrderMenuPriceTest {

    @DisplayName("주문 메뉴 가격이 다른 객체는 동등하지 않다.")
    @Test
    void equalsTest() {
        OrderMenuPrice price1 = OrderMenuPrice.from(BigDecimal.valueOf(16_000));
        OrderMenuPrice price2 = OrderMenuPrice.from(BigDecimal.valueOf(18_000));

        Assertions.assertThat(price1).isNotEqualTo(price2);
    }

    @DisplayName("주문 메뉴 가격이 같은 객체는 동등하다.")
    @Test
    void equalsTest2() {
        OrderMenuPrice price1 = OrderMenuPrice.from(BigDecimal.valueOf(16_000));
        OrderMenuPrice price2 = OrderMenuPrice.from(BigDecimal.valueOf(16_000));

        Assertions.assertThat(price1).isEqualTo(price2);
    }

    @DisplayName("주문 메뉴 가격이 null 이면 예외가 발생한다.")
    @Test
    void createException() {
        Assertions.assertThatThrownBy(() -> OrderMenuPrice.from(null))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_ORDER_MENU_PRICE);
    }

    @DisplayName("주문 메뉴 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void createException2() {
        Assertions.assertThatThrownBy(() -> OrderMenuPrice.from(BigDecimal.valueOf(-1)))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_ORDER_MENU_PRICE);
    }
}
