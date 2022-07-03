package kitchenpos.common.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PriceTest {
    @Test
    void 가격이_null이면_에러가_발생해야_한다() {
        // when and then
        Assertions.assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_0보다_작으면_에러가_발생해야_한다() {
        // given
        final Long invalidPrice = -1L;

        // when and then
        Assertions.assertThatThrownBy(() -> new Price(invalidPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
