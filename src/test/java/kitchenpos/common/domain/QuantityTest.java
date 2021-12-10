package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("수량")
class QuantityTest {

    @DisplayName("생성")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 생성 가능")
    @ValueSource(longs = {0, Long.MAX_VALUE})
    void instance(long value) {
        assertThatNoException()
            .isThrownBy(() -> Quantity.from(value));
    }

    @DisplayName("수량은 반드시 양수")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @ValueSource(longs = {-1, Long.MIN_VALUE})
    void instance_negativeValue_thrownIllegalArgumentException(long value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Quantity.from(value))
            .withMessageEndingWith("반드시 0이상 이어야 합니다.");
    }
}
