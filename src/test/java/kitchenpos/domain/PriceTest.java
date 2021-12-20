package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("가격 테스트")
class PriceTest {

    @DisplayName("가격이 null이면 예외가 발생한다.")
    @Test
    void createNull() {
        assertThatThrownBy(() -> Price.of(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void createNegative() {
        assertThatThrownBy(() -> Price.of(BigDecimal.valueOf(-1_000L)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}