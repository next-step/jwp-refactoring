package kitchenpos.order.domain;

import static kitchenpos.order.sample.OrderLineItemSample.이십원_후라이트치킨_두마리세트_한개_주문_항목;
import static kitchenpos.order.sample.OrderTableSample.빈_두명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import kitchenpos.common.exception.InvalidStatusException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
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
    @DisplayName("테이블 상태 변경")
    @CsvSource({"EMPTY,true", "FULL,false"})
    void changeStatus(TableStatus changeStatus, boolean expected) {
        //given
        OrderTable orderTable = OrderTable.of(Headcount.from(1), TableStatus.EMPTY);

        //when
        orderTable.changeStatus(changeStatus);

        //then
        assertThat(orderTable.isEmpty()).isEqualTo(expected);
    }

    @Test
    @DisplayName("그룹이 있으면 테이블 상태 변경 불가능")
    void changeStatus_hasGroup_thrownInvalidStatusException() {
        //given
        OrderTable orderTable = OrderTable.of(Headcount.from(1), TableStatus.EMPTY);
        TableGroup.from(Arrays.asList(orderTable, 빈_두명_테이블()));

        //when
        ThrowingCallable changeStatusCallable = () -> orderTable.changeStatus(TableStatus.FULL);

        //then
        assertThatExceptionOfType(InvalidStatusException.class)
            .isThrownBy(changeStatusCallable)
            .withMessageEndingWith("그룹이 지정되어 있어서 상태를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("식사중이라면 테이블 상태 변경 불가능")
    void changeStatus_cooking_thrownInvalidStatusException() {
        //given
        OrderTable orderTable = OrderTable.of(Headcount.from(1), TableStatus.FULL);
        Order.of(orderTable, Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목()));

        //when
        ThrowingCallable changeStatusCallable = () -> orderTable.changeStatus(TableStatus.EMPTY);

        //then
        assertThatExceptionOfType(InvalidStatusException.class)
            .isThrownBy(changeStatusCallable)
            .withMessageEndingWith("상태를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("방문한 손님 수 변경")
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = OrderTable.of(Headcount.from(1), TableStatus.FULL);

        //when
        orderTable.changeNumberOfGuests(Headcount.from(10));

        //then
        assertThat(orderTable.numberOfGuests()).isEqualTo(Headcount.from(10));
    }

    @Test
    @DisplayName("빈 테이블의 방문한 손님 수 변경 불가능")
    void changeNumberOfGuests_empty_thrownInvalidStatusException() {
        //given
        OrderTable orderTable = OrderTable.of(Headcount.from(1), TableStatus.EMPTY);

        //when
        ThrowingCallable changeCallable = () -> orderTable.changeNumberOfGuests(Headcount.from(10));

        //then
        assertThatExceptionOfType(InvalidStatusException.class)
            .isThrownBy(changeCallable)
            .withMessageEndingWith("방문한 손님 수를 변경할 수 없습니다.");
    }


    private static Stream<Arguments> instance_nullHeadcountOrStatus_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, TableStatus.EMPTY),
            Arguments.of(Headcount.from(1), null)
        );
    }
}
