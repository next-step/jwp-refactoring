package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuPriceTest {
    @DisplayName("메뉴 가격은 NULL일 수 없다.")
    @Test
    void createWithNull() {
        assertThatThrownBy(() -> new MenuPrice(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 필수 값 입니다.");
    }

    @DisplayName("메뉴 가격은 0원 이하 일 수 없다.")
    @Test
    void createWithNegative() {
        assertThatThrownBy(() -> new MenuPrice(BigDecimal.valueOf(-100)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 0원 미만 일 수 없습니다.");
    }

    @DisplayName("메뉴 가격을 비교할 수 있다.")
    @Test
    void greaterThan() {
        MenuPrice less = new MenuPrice(BigDecimal.valueOf(1500));
        MenuPrice bigger = new MenuPrice(BigDecimal.valueOf(2000));

        assertThat(bigger.greaterThan(less)).isTrue();
    }

    @DisplayName("메뉴 가격을 더 할 수 있다.")
    @Test
    void add() {
        MenuPrice firstPrice = new MenuPrice(BigDecimal.valueOf(1000));
        MenuPrice secondPrice = new MenuPrice(BigDecimal.valueOf(2000));

        assertThat(firstPrice.add(secondPrice).value()).isEqualTo(BigDecimal.valueOf(3000));
    }
}
