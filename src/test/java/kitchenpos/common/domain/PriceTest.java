package kitchenpos.common.domain;

import kitchenpos.common.exception.IllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("가격 도메인 테스트")
public class PriceTest {
    @DisplayName("가격이 0원 미만일 경우 예외 발생")
    @Test
    void 가격_0원_미만_예외() {
        Throwable thrown = catchThrowable(() -> Price.of(BigDecimal.valueOf(-1)));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(("가격은 음수가 될 수 없습니다."));
    }
}
