package kitchenpos.common.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.IllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("가격 도메인 테스트")
public class PriceTest {
    @DisplayName("가격에 빈 값이 들어올 경우 예외")
    @Test
    void 가격_빈_값_예외() {
        assertThatThrownBy(() -> Price.of(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격에 0미만의 값이 들어올 경우 예외")
    @Test
    void 가격_0미만_값_예외() {
        assertThatThrownBy(() -> Price.of(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("입력받은 숫자가 현재 가격보다 큰지 비교한다.")
    @Test
    void 현재_가격이_입력_숫자보다_큼_비교() {
        // given
        Price 현재가 = Price.of(BigDecimal.valueOf(2000));
        BigDecimal 입력값 = BigDecimal.valueOf(1000);

        // when
        boolean actual = 현재가.isGreaterThan(입력값);

        // then
        assertThat(actual).isTrue();
    }
}
