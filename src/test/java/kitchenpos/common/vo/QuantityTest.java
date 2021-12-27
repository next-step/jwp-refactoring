package kitchenpos.common.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuantityTest {

    @DisplayName("Quantity 생성 테스트")
    @Test
    void constructor() {
        long five = 5L;
        Quantity quantity = new Quantity(five);
        assertThat(quantity).isEqualTo(new Quantity(five));
    }

    @DisplayName("수량은 0개 이상이어야 한다.")
    @Test
    void constructor_exception1() {
        long five = 5L;
        Quantity quantity = new Quantity(five);
        assertThat(quantity).isEqualTo(new Quantity(five));
    }
}
