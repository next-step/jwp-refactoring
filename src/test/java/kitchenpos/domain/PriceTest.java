package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    @DisplayName("가격이 0원 미만일 수 없습니다")
    void priceZeroNo() {
        assertThatThrownBy(() ->
                new Price(BigDecimal.ZERO)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
