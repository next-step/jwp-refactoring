package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.table.domain.Headcount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("인원 수")
class HeadcountTest {

    @DisplayName("생성")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 생성 가능")
    @ValueSource(ints = {0, Integer.MAX_VALUE})
    void instance(int value) {
        assertThatNoException()
            .isThrownBy(() -> Headcount.from(value));
    }

    @DisplayName("인원 수는 반드시 양수")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @ValueSource(ints = {-1, Integer.MIN_VALUE})
    void instance_negativeValue_thrownIllegalArgumentException(int value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Headcount.from(value))
            .withMessageEndingWith("반드시 0이상 이어야 합니다.");
    }
}
