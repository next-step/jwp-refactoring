package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class QuantityTest {
    @Test
    void 수량이_null이면_에러가_발생해야_한다() {
        // when and then
        Assertions.assertThatThrownBy(() -> new Quantity(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 수량이_1보다_작으면_에러가_발생해야_한다() {
        // given
        final Integer invalidPrice = 0;

        // when and then
        Assertions.assertThatThrownBy(() -> new Quantity(invalidPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
