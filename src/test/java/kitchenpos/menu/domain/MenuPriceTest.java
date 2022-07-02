package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuPriceTest {

    @DisplayName("MenuPrice 생성")
    @ParameterizedTest
    @ValueSource(ints = { 0, 10000, 3500000 })
    void price(int value) {
        new MenuPrice(new BigDecimal(value));
    }

    @DisplayName("MenuPrice 예외 생성")
    @ParameterizedTest
    @ValueSource(ints = { -1000, -100, -1 })
    void priceException(int value) {
        assertThatThrownBy(() -> {
            new MenuPrice(new BigDecimal(value));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
