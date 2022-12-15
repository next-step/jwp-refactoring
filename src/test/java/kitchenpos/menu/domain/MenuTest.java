package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @Test
    @DisplayName("메뉴를 생성할 때 가격이 null 이 입력되면 exception 이 발생함")
    void throwExceptionMenuPriceIsNull() {
        assertThatThrownBy(() -> Menu.of(1L,"test", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @ValueSource(ints = {-1})
    @DisplayName("메뉴를 생성할 때 가격이 음수가 입력되면 exception이 발생함")
    void throwExceptionMenuPriceIsNegative() {
        assertThatThrownBy(() -> Menu.of(1L,"test", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}