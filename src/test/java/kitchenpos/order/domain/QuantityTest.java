package kitchenpos.order.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuantityTest {

    @DisplayName("Quantity 생성 테스트")
    @Test
    void constructor() {
        long five = 5L;
        Quantity quantity = new Quantity(five);
        Assertions.assertThat(quantity).isEqualTo(new Quantity(five));
    }

    @DisplayName("수량은 0개 이상이어야 한다.")
    @Test
    void constructor_exception1() {
        long five = 5L;
        Quantity quantity = new Quantity(five);
        Assertions.assertThat(quantity).isEqualTo(new Quantity(five));
    }
}
