package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @DisplayName("가격이 없을 때 에러발생")
    @Test
    void newFailBecuaseOfNull() {
        //when && then
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 음수일때 에러발생")
    @Test
    void newFailBecuaseOfMinus() {
        //when && then
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 없거나 음수인 상품은 등록할 수 없습니다.");
    }
}