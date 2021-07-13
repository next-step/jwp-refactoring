package kitchenpos.menu.domain;

import kitchenpos.common.valueobject.exception.InvalidNameException;
import kitchenpos.common.valueobject.exception.NegativePriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("메뉴의 총 가격은 0원 이상이어야한다.")
    @Test
    void createMenuExceptionIfPriceIsNull() {
        //when
        assertThatThrownBy(() -> Menu.of("10만원의 행복 파티 세트 (12인)", BigDecimal.valueOf(-10)))
                .isInstanceOf(NegativePriceException.class); //then
    }

    @DisplayName("메뉴의 이름을 지정해야한다.")
    @Test
    void createMenuExceptionIfNameIsNull() {
        //when
        assertThatThrownBy(() -> Menu.of("", BigDecimal.valueOf(100)))
                .isInstanceOf(InvalidNameException.class); //then
        //when
        assertThatThrownBy(() -> Menu.of(null, BigDecimal.valueOf(100)))
                .isInstanceOf(InvalidNameException.class); //then
    }
}
