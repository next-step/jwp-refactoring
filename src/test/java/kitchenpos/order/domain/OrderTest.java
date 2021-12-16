package kitchenpos.order.domain;

import static kitchenpos.order.sample.OrderLineItemSample.이십원_후라이트치킨_두마리세트_한개_주문_항목;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Collections;
import kitchenpos.common.domain.Validator;
import kitchenpos.common.exception.InvalidStatusException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("주문")
class OrderTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Order.of(1L,
                Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목())));
    }

    @Test
    @DisplayName("주문 항목들은 필수")
    void instance_nullOrderTableOrOrderLineItem_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Order.of(1L, null))
            .withMessageEndingWith("필수입니다.");
    }


    @Test
    @DisplayName("주문 항목들이 비어있을 수 없음")
    void instance_emptyOrderLineItem_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Order.of(1L, Collections.emptyList()))
            .withMessage("주문 항목들이 비어있을 수 없습니다.");
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 상태 변경 가능")
    @DisplayName("주문 상태 변경")
    @EnumSource(OrderStatus.class)
    void changeStatus(OrderStatus updatedStatus) {
        //given
        Order order = Order.of(1L,
            Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목()))
            .get(Validator.fake());

        //when
        order.changeStatus(updatedStatus);

        //then
        assertThat(order.status()).isEqualTo(updatedStatus);
    }

    @Test
    @DisplayName("완료된 주문은 상태를 변경할 수 없음")
    void changeStatus_completedOrder_thrownInvalidStatusException() {
        //given
        Order order = Order.of(1L,
            Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목()))
            .get(Validator.fake());
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
        Order order = Order.of(1L,
            Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목()))
            .get(Validator.fake());
        order.changeStatus(status);

        //when, then
        assertThat(order.isCookingOrMeal())
            .isEqualTo(expected);
    }
}
