package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.common.Messages.PRICE_CANNOT_ZERO_LESS_THAN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class PriceTest {

    @Test
    @DisplayName("가격 생성 테스트")
    void price() {
        // given
        Price price = Price.of(BigDecimal.valueOf(17_000));

        // when
        // then
        assertAll(
                () -> assertThat(price).isNotNull(),
                () -> assertThat(price.getPrice()).isEqualTo(BigDecimal.valueOf(17_000))
        );
    }

    @Test
    @DisplayName("가격이 0원 보다 작은 경우 실패")
    void priceCannotZero() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Price.of(BigDecimal.valueOf(-1)))
                .withMessage(PRICE_CANNOT_ZERO_LESS_THAN)
        ;
    }
}
