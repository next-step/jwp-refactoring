package kitchenpos.common.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.common.Quantity;
import kitchenpos.exception.ErrorMessage;

@DisplayName("수량 관련 단위테스트")
public class QuantityTest {
    @ParameterizedTest(name = "객체 생성시 수량이 음수면 에러가 발생한다. [수량: {0}]")
    @ValueSource(ints = {-1, -2})
    void create_quantity_negative_exception(int quantity) {
        // when - then
        assertThatThrownBy(() -> Quantity.of(quantity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorMessage.cannotBeNegative("수량"));
    }
}
