package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.NotEnoughMiniumQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("수량 테스트")
public class QuantityTest {

    @DisplayName("수량은 최소 1개 이상어이야 한다.")
    @Test
    void createFail() {
        assertThatThrownBy(() -> new Quantity(0))
            .isInstanceOf(NotEnoughMiniumQuantityException.class);
    }

}
