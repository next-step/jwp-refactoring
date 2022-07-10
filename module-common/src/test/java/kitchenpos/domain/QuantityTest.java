package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class QuantityTest {

    @DisplayName("수량 생성 시 0보다 작은 수량을 생성하면 예외가 발생해야 한다")
    @Test
    void createQuantityByMinusTest() {
        assertThatIllegalArgumentException().isThrownBy(() -> 수량_생성(-1L));
    }

    @DisplayName("수량 생성 시 0 이상의 수량으로 생성하면 정상 생성되어야 한다")
    @Test
    void createQuantityTest() {
        assertThatNoException().isThrownBy(() -> 수량_생성(0L));
        assertThatNoException().isThrownBy(() -> 수량_생성(Long.MAX_VALUE));
    }

    public static Quantity 수량_생성(Long value) {
        return new Quantity(value);
    }
}