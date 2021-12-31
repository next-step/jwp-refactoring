package kitchenpos.order.domain;

import kitchenpos.exception.NotChangeCompletionOrderException;
import kitchenpos.exception.OrderLineItemEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static kitchenpos.order.domain.OrderLineItemTest.와퍼_세트_주문;
import static kitchenpos.order.domain.OrderLineItemTest.콜라_주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {
    public static final Order 주문통합 = 주문생성();

    private static Order 주문생성() {
        return new Order(
                9L
                , OrderStatus.COOKING
                , Arrays.asList(와퍼_세트_주문, 콜라_주문));
    }

    private Order 이인석주문;

    @BeforeEach
    void setUp() {
        이인석주문 = new Order(1L, OrderStatus.COOKING);
    }


    @Test
    @DisplayName("주문 생성")
    public void create() {
        // given
        // when
        // then
        assertThat(이인석주문).isEqualTo(new Order(1L, OrderStatus.COOKING));
    }

    @Test
    @DisplayName("주문 상태 변경")
    public void changeOrderStatusTest() {
        // given
        OrderStatus meal = OrderStatus.MEAL;
        // when
        이인석주문.changeOrderStatus(meal);
        // then
        assertThat(이인석주문.getOrderStatus()).isEqualTo(meal);
    }

    @Test
    @DisplayName("주문 상태가 완료인 경우 변경 할 수 없다.")
    public void changeOrderStatusExceptionTest() {
        // given
        Order 완료된_주문 = new Order(1L, OrderStatus.COMPLETION);
        // when
        // then
        assertThatThrownBy(() -> 완료된_주문.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(NotChangeCompletionOrderException.class);
    }

    private static Stream<Arguments> isCompletionTestParam() {
        return Stream.of(
                Arguments.of(OrderStatus.COMPLETION, true),
                Arguments.of(OrderStatus.COOKING, false),
                Arguments.of(OrderStatus.MEAL, false)
        );
    }

    @ParameterizedTest
    @MethodSource("isCompletionTestParam")
    @DisplayName("계산 완료인지 확인함")
    public void isCompletionTest(OrderStatus status, boolean result) {
        // given
        Order order = new Order(1L, status);
        // when
        boolean actual = order.isCompletion();
        // then
        assertThat(actual).isEqualTo(result);
    }

    @Test
    @DisplayName("주문 항목이 없으면 주문을 등록 할 수 없습니다.")
    public void OrderLineItemEmptyExceptionTest() {
        // given
        // when
        // then
        assertThrows(OrderLineItemEmptyException.class, () -> Order.CookingOrder(1L, Collections.emptyList()));
    }

}
