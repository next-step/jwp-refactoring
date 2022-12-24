package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {

    @DisplayName("수량을 생성할 수 있다.")
    @Test
    void quantity() {
        //given
        long quantity = 1000;
        //when
        Quantity actual = Quantity.from(quantity);
        //then
        assertThat(actual.value()).isEqualTo(quantity);
    }

    @DisplayName("수량은 0개 이상이어야 한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1, -100})
    void quantityValid(long quantity) {

        //when & then
        assertThatThrownBy(() -> Quantity.from(quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
