package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("가격")
class PriceTest {

    @DisplayName("생성")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 생성 가능")
    @ValueSource(longs = {1, Long.MAX_VALUE})
    void instance(long value) {
        assertThatNoException()
            .isThrownBy(() -> Price.from(BigDecimal.valueOf(value)));
    }

    @Test
    @DisplayName("가격 값은 필수")
    void instance_nullValue_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Price.from(null))
            .withMessage("가격 값은 필수입니다.");
    }

    @DisplayName("가격은 반드시 양수")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @ValueSource(longs = {-1, Long.MIN_VALUE})
    void instance_negativeValue_thrownIllegalArgumentException(long value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Price.from(BigDecimal.valueOf(value)))
            .withMessageEndingWith("반드시 0이상 이어야 합니다.");
    }
}
