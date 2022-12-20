package kitchenpos.domain;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Quantity Common VO 테스트")
public class QuantityTest {
    @DisplayName("Quantity 생성 테스트")
    @Test
    void create() {
        long quantityValue = 10L;
        Quantity 수량 = new Quantity(quantityValue);

        assertThat(수량.value()).isEqualTo(quantityValue);
    }

    @DisplayName("Quantity 는 null 또는 음수일 수 없다.")
    @Test
    void validateException() {
        assertAll(
                () -> assertThatThrownBy(() -> new Quantity(null)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Quantity(-10L)).isInstanceOf(IllegalArgumentException.class)
        );
    }
}
