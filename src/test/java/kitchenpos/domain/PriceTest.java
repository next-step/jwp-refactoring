package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("금액에 대한 단위 테스트")
class PriceTest {

    @DisplayName("금액에 null 을 전달하면 예외가 발생한다")
    @Test
    void price_exception_test() {
        // given
        BigDecimal value = null;

        // then
        assertThatThrownBy(() -> {
            new Price(value);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("금액에 음수를 전달하면 예외가 발생한다")
    @Test
    void price_exception_test2() {
        // given
        BigDecimal value = BigDecimal.valueOf(-300L);

        // then
        assertThatThrownBy(() -> {
            new Price(value);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("금액을 더하면 정상적으로 계산된다")
    @Test
    void price_add_test() {
        // given
        Price source = new Price(BigDecimal.valueOf(300L));
        Price target = new Price(BigDecimal.valueOf(200L));

        // when
        Price sum = source.add(target);

        // then
        assertThat(sum.getValue()).isEqualTo(BigDecimal.valueOf(500L));
    }

    @DisplayName("금액이 전달받은 값 보다 더 크면 true 를 리턴한다")
    @Test
    void price_over_test() {
        // given
        Price source = new Price(BigDecimal.valueOf(300L));
        Price target = new Price(BigDecimal.valueOf(200L));

        // when
        boolean result = source.isOverThan(target);

        // then
        assertTrue(result);
    }
}
