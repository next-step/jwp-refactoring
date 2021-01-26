package kitchenpos.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class QuantityTest {

    @DisplayName("Quantity 생성")
    @Test
    void testCreateNormal() {
        // given
        long amount = 1;

        // when
        Quantity quantity = new Quantity(amount);

        // then
        assertThat(quantity.getQuantity()).isEqualTo(amount);
    }

    @DisplayName("0보다 작은 값으로 생성 시 예외")
    @Test
    void testCreateWithNegativeAmount() {
        assertThatIllegalArgumentException().isThrownBy(() -> Quantity.of(-10));
    }
}
