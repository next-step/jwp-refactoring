package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.common.Messages.QUANTITY_CANNOT_ZERO_LESS_THAN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class QuantityTest {

    @Test
    @DisplayName("수량 생성")
    void quantity() {
        // given
        Quantity quantity = Quantity.of(1);

        // when
        // then
        assertAll(
                () -> assertThat(quantity).isNotNull(),
                () -> assertThat(quantity.getQuantity()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("수량이 0미만인 경우 생성 실패")
    void quantityCannotZeroLessThan() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Quantity.of(-1))
                .withMessage(QUANTITY_CANNOT_ZERO_LESS_THAN)
        ;
    }
}
