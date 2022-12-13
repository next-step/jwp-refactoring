package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    @DisplayName("메뉴가격이 없으면 예외발생")
    @Test
    public void throwsExceptionWhenNullAmount() {
        assertThatThrownBy(() -> Menu.builder().price(null).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가격이 0보다작으면 예외발생")
    @Test
    public void throwsExceptionWhenNetativeAmount() {
        assertThatThrownBy(() -> Menu.builder().price(BigDecimal.valueOf(-1000)).build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
