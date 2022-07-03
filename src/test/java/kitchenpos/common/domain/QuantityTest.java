package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuantityTest {
    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    @DisplayName("수량이 비어있거나 0개 미만이면 Exception 발생 확인")
    void validate(Long quantity) {
        // then
        assertThatThrownBy(() -> {
            new Quantity(quantity);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
