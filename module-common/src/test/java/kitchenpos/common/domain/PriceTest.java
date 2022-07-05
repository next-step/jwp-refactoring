package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    @DisplayName("금액 객체가 같은지 검증")
    void verifyEqualsPrice() {
        assertAll(
                () -> assertThat(Price.of(100L)).isEqualTo(Price.of(100L)),
                () -> assertThat(Price.of(100L).value()).isEqualTo(100L)
        );
    }

    @Test
    @DisplayName("금액 객체에 음수가 들어오면 예외 발생")
    void invalidInputPrice() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.of(-100L));
    }

    @Test
    @DisplayName("금액이 잘 곱해지는지 확인")
    void multiplyPrice() {
        final Price price = Price.of(100L);

        assertThat(price.multiply(10L)).isEqualTo(Price.of(1_000L));
    }

    @Test
    @DisplayName("금액의 합이 잘 되는지 확인")
    void sumOtherPrice() {
        final Price price = Price.of(10L);

        assertThat(price.sum(Price.of(20L))).isEqualTo(Price.of(30L));
    }

    @Test
    @DisplayName("두 금액을 비교")
    void compareToOtherPrice() {
        final Price price1 = Price.of(10L);
        final Price price2 = Price.of(20L);

        assertThat(price1.isMoreThan(price2)).isFalse();
    }
}
