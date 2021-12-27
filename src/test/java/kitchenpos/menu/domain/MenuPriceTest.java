package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.exception.MenuPriceNotAcceptableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuPriceTest {

    @DisplayName("메뉴 가격 생성")
    @Test
    void constructor() {
        BigDecimal value = BigDecimal.valueOf(1000L);
        MenuPrice menuPrice = new MenuPrice(value);
        assertThat(menuPrice).isEqualTo(new MenuPrice(value));
    }

    @DisplayName("메뉴 가격은 0원 이상이어야 한다.")
    @Test
    void constructor_exception() {
        BigDecimal value = BigDecimal.valueOf(-1L);
        assertThatThrownBy(() -> new MenuPrice(value))
            .isInstanceOf(MenuPriceNotAcceptableException.class);
    }
}
