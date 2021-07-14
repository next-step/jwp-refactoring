package kitchenpos.order.domain;

import kitchenpos.order.domain.exception.CannotChangeOrderStatusException;
import kitchenpos.order.domain.exception.CannotUngroupException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.exception.CannotOrderEmptyTableException;
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
        assertThatThrownBy(() -> Order.createWithMapping(OrderTable.of(0, true), OrderStatus.COOKING, Lists.list()))
                .isInstanceOf(CannotOrderEmptyTableException.class); //then
    }

    @DisplayName("특정 주문의 상태는 조리, 식사, 계산완료 순서로 진행된다.")
    @Test
    void changeOrderStatus() {
        //given
        Order order = Order.createWithMapping(OrderTable.of(3, false), OrderStatus.COOKING, Lists.list());

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
        Order order = Order.createWithMapping(OrderTable.of(2, false), OrderStatus.COOKING, Lists.list());
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(CannotChangeOrderStatusException.class);
    }

    @DisplayName("주문 상태가 완료인 경우에만 테이블 그룹을 해제할 수 있습니다.")
    @Test
    void validateNotCompletionStatus() {
        //given
        Order order = Order.createWithMapping(OrderTable.of(2, false), OrderStatus.COOKING, Lists.list());

        //when
        assertThatThrownBy(order::validateNotCompletionStatus)
                .isInstanceOf(CannotUngroupException.class); //then
    }
}
