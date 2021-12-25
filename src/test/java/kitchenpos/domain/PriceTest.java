package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {
    @DisplayName("가격 생성")
    @Test
    void 생성() {
        Price price = Price.of(1000);

        assertThat(price).isEqualTo(Price.of(1000));
    }

    @DisplayName("가격 생성시 값이 없으면 예외 발생")
    @Test
    void 생성_예외1() {
        assertThatIllegalArgumentException().isThrownBy(
            () -> Price.of(Integer.valueOf(null))
        );
    }

    @DisplayName("가격 생성시 값이 음수면 예외 발생")
    @Test
    void 생성_예외2() {
        assertThatIllegalArgumentException().isThrownBy(
            () -> Price.of(Integer.valueOf(-5000))
        );
    }

}