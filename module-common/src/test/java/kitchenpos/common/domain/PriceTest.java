package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {
    @Test
    @DisplayName("가격이 0보다 작으면 실패한다.")
    public void createPriceFail() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0 미만의 값으로 설정할 수 없습니다");
    }

    @Test
    @DisplayName("주어진 값보다 큰지 비교한다.")
    public void isExpensiveThanTest() {
        Price price = new Price(BigDecimal.valueOf(1000));
        boolean result = price.isExpensiveThan(BigDecimal.valueOf(999));
        assertThat(result).isTrue();
    }
}