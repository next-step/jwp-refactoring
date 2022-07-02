package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductQuantityTest {

    @DisplayName("MenuProductQuantity 생성")
    @ParameterizedTest
    @ValueSource(longs = { 0, 1000, 35000 })
    void quantity(Long value) {
        new MenuProductQuantity(value);
    }

    @DisplayName("MenuProductQuantity 예외 생성")
    @ParameterizedTest
    @ValueSource(longs = { -1000, -100, -1 })
    void quantityException(Long value) {
        assertThatThrownBy(() -> {
            new MenuProductQuantity(value);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
