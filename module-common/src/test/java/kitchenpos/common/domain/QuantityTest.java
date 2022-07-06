package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    @DisplayName("수량 객체가 같은지 검증")
    void verifyEqualsQuantity() {
        assertThat(Quantity.of(5L)).isEqualTo(Quantity.of(5L));
    }

    @Test
    @DisplayName("수량이 0개 이하이면 오류 발생")
    void invalidQuantityNumber() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Quantity.of(0L));
    }
}
