package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("주문 테이블")
class OrderTableTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> OrderTable.of(Headcount.from(1), TableStatus.EMPTY));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @DisplayName("손님 수와 상태는 필수")
    @MethodSource
    void instance_nullHeadcountOrStatus_thrownIllegalArgumentException(Headcount numberOfGuests,
        TableStatus status) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> OrderTable.of(numberOfGuests, status))
            .withMessageEndingWith("필수입니다.");
    }

    private static Stream<Arguments> instance_nullHeadcountOrStatus_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, TableStatus.EMPTY),
            Arguments.of(Headcount.from(1), null)
        );
    }
}
