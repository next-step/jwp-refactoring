package kitchenpos.order.domain;

import kitchenpos.order.domain.exception.UnUseOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.exception.UnUseOrderTableException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문 불가능 상태일 경우 주문할 수 없다.")
    @Test
    void createOrderExceptionIfTableEmptyIsTrue() {
        //when
        assertThatThrownBy(() -> Order.create(OrderTable.of(0, true), Lists.list()))
                .isInstanceOf(UnUseOrderTableException.class); //then
    }

    @DisplayName("특정 주문의 상태는 조리, 식사, 계산완료 순서로 진행된다.")
    @Test
    void changeOrderStatus() {
        //given
        Order order = Order.create(OrderTable.of(3, false), Lists.list());

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
        Order order = Order.create(OrderTable.of(2, false), Lists.list());
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(UnUseOrderException.class);
    }
}
