package domain.order;

import domain.order.exception.CannotChangeOrderStatusException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("특정 주문의 상태는 조리, 식사, 계산완료 순서로 진행된다.")
    @Test
    void changeOrderStatus() {
        //given
        Order order = Order.of(1L, OrderStatus.COOKING, OrderLineItems.of(Lists.list()));

        //when
        order.changeOrderStatus(OrderStatus.MEAL);
        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);

        //when
        order.changeOrderStatus(OrderStatus.COMPLETION);
        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문 완료 상태인 경우 변경할 수 없다.")
    @Test
    void changeOrderStatusExceptionIfSameStatusBefore() {
        //given
        Order order = Order.of(1L, OrderStatus.COOKING, OrderLineItems.of(Lists.list()));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(CannotChangeOrderStatusException.class);
    }

    @DisplayName("주문 상태가 Cooking, Meal 상태인지 확인할 수 있다.")
    @Test
    void validateNotCompletionStatus() {
        //given
        Order order = Order.of(1L, OrderStatus.COOKING, OrderLineItems.of(Lists.list()));

        //when
        boolean actual = order.isCookingOrMeal();

        //then
        assertThat(actual).isTrue();
    }
}
