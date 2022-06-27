package kitchenpos.core.domain;

import kitchenpos.core.exception.InvalidQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Quantity 클래스 테스트")
class QuantityTest {

    @DisplayName("1인 Quantity를 생성한다.")
    @Test
    void successfulCreate() {
        Quantity quantity = new Quantity(1L);
        assertThat(quantity.getValue()).isEqualTo(1L);
    }

    @DisplayName("-1인 Quantity를 생성한다.")
    @Test
    void failureCreate() {
        assertThatThrownBy(() -> {
            new Quantity(-1L);
        }).isInstanceOf(InvalidQuantityException.class)
          .hasMessageContaining("유효하지 않은 수량입니다.");
    }

    @DisplayName("Quantity(10)과 Quantity(10)은 동등하다.")
    @Test
    void equals() {
        Quantity ten = new Quantity(10);
        assertThat(ten).isEqualTo(new Quantity(10));
    }
}
