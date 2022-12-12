package kitchenpos.common.domain;

import kitchenpos.price.domain.Amount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Amount 클래스 테스트")
class AmountTest {

    private final Amount ten = new Amount(BigDecimal.TEN);
    private final Amount one = new Amount(BigDecimal.ONE);

    @DisplayName("1인 금액를 생성한다.")
    @Test
    void successfulCreate() {
        Amount amount = new Amount(BigDecimal.ONE);
        assertThat(amount.getValue()).isEqualTo(BigDecimal.valueOf(1L));
    }

    @DisplayName("-1인 금액을 생성한다.")
    @Test
    void failureCreate() {
        assertThatThrownBy(() -> {
            new Amount(BigDecimal.valueOf(-1));
        }).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("유효하지 않은 금액입니다.");
    }

    @DisplayName("금액(10)과 금액(1)을 더하면 금액(11)이다.")
    @Test
    void add() {
        assertThat(ten.add(one)).isEqualTo(new Amount(BigDecimal.valueOf(11)));
    }

    @DisplayName("금액(10)은 금액(1)보다 크다.")
    @Test
    void isLessThan() {
        assertThat(ten.isGatherThan(one)).isTrue();
    }

    @DisplayName("Amount(10)과 Amount(10)은 동등하다.")
    @Test
    void equals() {
        assertThat(ten).isEqualTo(new Amount(BigDecimal.valueOf(10L)));
    }
}
