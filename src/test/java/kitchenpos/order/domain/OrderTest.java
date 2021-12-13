package kitchenpos.order.domain;

import static kitchenpos.order.sample.OrderLineItemSample.이십원_후라이트치킨_두마리세트_한개_주문_항목;
import static kitchenpos.order.sample.OrderTableSample.빈_두명_테이블;
import static kitchenpos.order.sample.OrderTableSample.채워진_다섯명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.common.exception.InvalidStatusException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("주문")
class OrderTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Order.of(채워진_다섯명_테이블(),
                Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목())));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @DisplayName("주문 테이블과 주문 항목들은 필수")
    @MethodSource
    void instance_nullOrderTableOrOrderLineItem_thrownIllegalArgumentException(
        OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Order.of(orderTable, orderLineItems))
            .withMessageEndingWith("필수입니다.");
    }

    @Test
    @DisplayName("주문 테이블은 채워져 있어야 함")
    void instance_emptyTable_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Order.of(빈_두명_테이블(),
                Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목())))
            .withMessage("주문을 하는 테이블은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 항목들이 비어있을 수 없음")
    void instance_emptyOrderLineItem_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Order.of(채워진_다섯명_테이블(), Collections.emptyList()))
            .withMessage("주문 항목들이 비어있을 수 없습니다.");
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 상태 변경 가능")
    @DisplayName("주문 상태 변경")
    @EnumSource(OrderStatus.class)
    void changeStatus(OrderStatus updatedStatus) {
        //given
        Order order = Order.of(채워진_다섯명_테이블(),
            Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목()));

        //when
        order.changeStatus(updatedStatus);

        //then
        assertThat(order.status()).isEqualTo(updatedStatus);
    }

    @Test
    @DisplayName("완료된 주문은 상태를 변경할 수 없음")
    void changeStatus_completedOrder_thrownInvalidStatusException() {
        //given
        Order order = Order.of(채워진_다섯명_테이블(),
            Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목()));
        order.changeStatus(OrderStatus.COMPLETION);

        //when
        ThrowingCallable changeCallable = () -> order.changeStatus(OrderStatus.MEAL);

        //then
        assertThatExceptionOfType(InvalidStatusException.class)
            .isThrownBy(changeCallable)
            .withMessageEndingWith("상태를 변경할 수 없습니다.");
    }

    @ParameterizedTest(name = "[{index}] 주문 상태가 {0} 라면 조리중 또는 식사중 여부는 {1}")
    @DisplayName("조리중 또는 식사중 여부")
    @CsvSource({"COOKING,true", "MEAL,true", "COMPLETION,false"})
    void isCookingOrMeal(OrderStatus status, boolean expected) {
        //given
        Order order = Order.of(채워진_다섯명_테이블(),
            Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목()));
        order.changeStatus(status);

        //when, then
        assertThat(order.isCookingOrMeal())
            .isEqualTo(expected);
    }

    private static Stream<Arguments> instance_nullOrderTableOrOrderLineItem_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList(mock(OrderLineItem.class))),
            Arguments.of(OrderTable.of(Headcount.from(5), TableStatus.FULL), null)
        );
    }
}
