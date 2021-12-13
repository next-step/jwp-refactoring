package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest(name = "[{index}] 그룹과 주문이 없으면 {0} 상태로 변경 가능")
    @DisplayName("그룹과 주문이 없으면 테이블 상태를 변경 가능")
    @CsvSource({"EMPTY,true", "FULL,false"})
    void changeStatus(TableStatus changeStatus, boolean expected) {
        //given
        OrderTable orderTable = OrderTable.of(Headcount.from(1), TableStatus.EMPTY);

        //when
        orderTable.changeStatus(changeStatus);

        //then
        assertThat(orderTable.isEmpty()).isEqualTo(expected);
    }

    private static Stream<Arguments> instance_nullHeadcountOrStatus_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, TableStatus.EMPTY),
            Arguments.of(Headcount.from(1), null)
        );
    }
}
