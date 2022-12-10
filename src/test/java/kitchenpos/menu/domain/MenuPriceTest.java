package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 가격 클래스 테스트")
class MenuPriceTest {
    @Test
    void 메뉴_가격은_null일_수_없음() {
        assertThatThrownBy(() -> {
            new MenuPrice(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuExceptionCode.REQUIRED_PRICE.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -100, -500, -1000 })
    void 메뉴_가격은_0보다_작을_수_없음(int price) {
        assertThatThrownBy(() -> {
            new MenuPrice(new BigDecimal(price));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuExceptionCode.INVALID_PRICE.getMessage());
    }
}
