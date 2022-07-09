package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {
    @Test
    @DisplayName("메뉴 가격이 null 이거나 음수일 경우 - 오류")
    void invalidPrice() {
        // when then
        assertAll(
            () -> assertThatThrownBy(() -> new Price(new BigDecimal(-6_000L)))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }
}
