package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {
    @Test
    @DisplayName("수량 생성")
    void createQuantity() {
        Quantity actual = Quantity.from(10);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(Quantity.class)
        );
    }

    @ParameterizedTest(name = "[{index}] 수량은 음수일 수 없다.")
    @ValueSource(longs = {-1, Long.MIN_VALUE})
    void createQuantityByNonNegative(long quantity) {
        assertThatThrownBy(() -> Quantity.from(quantity))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("수량은 0 이상이어야 합니다.");
    }
}
