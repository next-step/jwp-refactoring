package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;

@DisplayName("MenuValidator 유효성검사 테스트")
class MenuValidatorTest {

    @Test
    @DisplayName("메뉴금액이 제품금액의 합계보다 클 경우 예외처리")
    void validateMenuPrice() {
        // given
        MenuValidator menuValidator = new MenuValidator();
        Menu menu = new Menu("A", BigDecimal.valueOf(20_000.00), 1L);

        // then
        assertThatThrownBy(() -> menuValidator.validateMenuPrice(menu, BigDecimal.valueOf(19_000.00)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 급액이 제품 합계금액보다 클 수 없습니다.");
    }
}
