package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuPriceException;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuPriceTest {

    @DisplayName("메뉴가격이 없으면 예외발생")
    @Test
    public void throwsExceptionWhenNullAmount() {
        assertThatThrownBy(() -> MenuPrice.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("메뉴가격이 0보다작으면 예외발생")
    @Test
    public void throwsExceptionWhenNetativeAmount() {
        BigDecimal price = Arbitraries.bigDecimals().lessOrEqual(BigDecimal.ZERO).sample();
        assertThatThrownBy(() -> MenuPrice.of(price))
                .isInstanceOf(MenuPriceException.class)
                .hasMessageContaining("가격은 0보다 작을수 없습니다");
    }

    @DisplayName("메뉴가격이 비교값보다 크면 true반환")
    @Test
    public void throwsExceptionWhenGreaterThenInput() {
        BigDecimal price = Arbitraries.bigDecimals().lessOrEqual(BigDecimal.ZERO).sample();
        MenuPrice menuPrice = MenuPrice.of(BigDecimal.valueOf(10000));

        assertThat(menuPrice.isExceedPrice(price)).isTrue();
    }

}
