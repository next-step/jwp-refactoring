package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.exception.NegativeQuantityException;

class QuantityTest {

    @DisplayName("Quantity 는 0 이상의 숫자로 생성한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 10, 100})
    void create1(int quantity) {
        // when & then
        assertThatNoException().isThrownBy(() -> Quantity.from(quantity));
    }

    @DisplayName("Quantity 는 0 미만의 음수로 생성 시, 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create2(int quantity) {
        // when & then
        assertThrows(NegativeQuantityException.class, () -> Quantity.from(quantity));
    }
}