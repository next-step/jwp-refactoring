package kitchenpos.domain;

import kitchenpos.common.exceptions.NegativeQuantityException;
import kitchenpos.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("수량 도메인 테스트")
class QuantityTest {

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(Quantity.valueOf(1L))
                .isEqualTo(Quantity.valueOf(1L));
    }

    @DisplayName("최소값 1 이상이어야 한다")
    @Test
    void validateTest() {
        assertThatThrownBy(() -> Quantity.valueOf(0L))
                .isInstanceOf(NegativeQuantityException.class);
    }

    @DisplayName("null 이면 안된다")
    @Test
    void validateTest2() {
        assertThatThrownBy(() -> Quantity.valueOf(null))
                .isInstanceOf(NegativeQuantityException.class);
    }

}