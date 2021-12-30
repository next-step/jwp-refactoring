package kitchenpos.common.domain;

import kitchenpos.common.exception.IllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("가격 도메인 테스트")
public class QuantityTest {
    @DisplayName("수량이 0개 미만일 경우 예외 발생")
    @Test
    void 가격_0원_미만_예외() {
        Throwable thrown = catchThrowable(() -> Quantity.of(-1));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(("유효한 수량은 0이상 입니다."));
    }
}
