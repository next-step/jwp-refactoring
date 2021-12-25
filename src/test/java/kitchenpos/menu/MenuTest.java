package kitchenpos.menu;

import kitchenpos.domain.Menu;
import kitchenpos.exception.MenuPriceMoreThanMenuProductPriceSumException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴")
class MenuTest {

    @Test
    @DisplayName("메뉴의 금액이 상품의 총 금액보다 크다면 예외가 발생한다.")
    void menuPriceMoreThanProductPriceSum() {
        // given
        final Menu menu = Menu.builder()
                .price(BigDecimal.valueOf(18000))
                .build();

        // when
        assertThatThrownBy(() -> {
            menu.checkMenuPrice(BigDecimal.valueOf(8000));
        }).isInstanceOf(MenuPriceMoreThanMenuProductPriceSumException.class);
    }
}
