package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuTest {
    @DisplayName("메뉴명은 NULL이거나 공백일 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createWithNameIsNullOrEmpty(String name) {
        assertThatThrownBy(() -> new Menu(name, BigDecimal.valueOf(32000), 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴명은 비어있거나 공백일 수 없습니다.");
    }

    @DisplayName("메뉴 그룹은 NULL일 수 없다.")
    @Test
    void createWithMenuGroupIsNull() {
        assertThatThrownBy(() -> new Menu("두마리치킨", BigDecimal.valueOf(18000), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹은 필수 값 입니다.");
    }

    @DisplayName("메뉴의 가격을 비교할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"15000:true", "20000:false"}, delimiter = ':')
    void moreExpensive(int menuPrice, boolean expected) {
        Menu menu = new Menu("두마리치킨", BigDecimal.valueOf(18000), 1L);
        MenuPrice price = new MenuPrice(BigDecimal.valueOf(menuPrice));

        assertThat(menu.moreExpensive(price)).isEqualTo(expected);
    }
}
