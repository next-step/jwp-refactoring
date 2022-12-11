package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidMenuPriceException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 가격 테스트")
class MenuPriceTest {

    @DisplayName("메뉴 가격이 다른 객체는 동등하지 않다.")
    @Test
    void equalsTest() {
        MenuPrice price1 = MenuPrice.from(BigDecimal.valueOf(16_000));
        MenuPrice price2 = MenuPrice.from(BigDecimal.valueOf(18_000));

        Assertions.assertThat(price1).isNotEqualTo(price2);
    }

    @DisplayName("메뉴 가격이 같은 객체는 동등하다.")
    @Test
    void equalsTest2() {
        MenuPrice price1 = MenuPrice.from(BigDecimal.valueOf(16_000));
        MenuPrice price2 = MenuPrice.from(BigDecimal.valueOf(16_000));

        Assertions.assertThat(price1).isEqualTo(price2);
    }

    @DisplayName("메뉴 가격이 null 이면 예외가 발생한다.")
    @Test
    void createException() {
        Assertions.assertThatThrownBy(() -> MenuPrice.from(null))
                .isInstanceOf(InvalidMenuPriceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_MENU_PRICE);
    }

    @DisplayName("메뉴 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void createException2() {
        Assertions.assertThatThrownBy(() -> MenuPrice.from(BigDecimal.valueOf(-1)))
                .isInstanceOf(InvalidMenuPriceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_MENU_PRICE);
    }
}
