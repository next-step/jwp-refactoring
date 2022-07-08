package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PriceTest {
    @Test
    @DisplayName("가격은 0원 이상이어야 한다.")
    void validate() {
        // then
        assertThatThrownBy(() -> {
            new Price(BigDecimal.valueOf(-1L));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 더 비싸면 true 를 반환한다.")
    void isMoreExpensive() {
        // given
        Price price1 = new Price(BigDecimal.valueOf(5_500));
        Price price2 = new Price(BigDecimal.valueOf(5_000));

        // then
        assertThat(price1.isMoreExpensive(price2)).isTrue();
    }
}
