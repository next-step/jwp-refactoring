package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.testfixtures.MenuTestFixtures;
import kitchenpos.order.exception.ClosedTableOrderException;
import kitchenpos.order.exception.CompleteOrderChangeStateException;
import kitchenpos.order.exception.DuplicateOrderLineItemsException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 생성")
    @Test
    void constructor() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(MenuTestFixtures.서비스군만두, new Quantity(5L))
        );

        //when
        Order order = new Order(orderTable, orderLineItems);

        //then
        assertThat(order.getOrderTable()).isEqualTo(orderTable);
    }

    @DisplayName("주문종료 상태의 테이블은 주문할 수 없다.")
    @Test
    void constructor_exception1() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), true);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(MenuTestFixtures.서비스군만두, new Quantity(5L))
        );

        //when, then
        assertThatThrownBy(() -> new Order(orderTable, orderLineItems))
            .isInstanceOf(ClosedTableOrderException.class);
    }

    @DisplayName("주문항목 리스트에 중복되는 메뉴가 존재해서는 안된다.")
    @Test
    void constructor_exception2() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), true);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(MenuTestFixtures.서비스군만두, new Quantity(5L)),
            new OrderLineItem(MenuTestFixtures.서비스군만두, new Quantity(2L))
        );

        //when, then
        assertThatThrownBy(() -> new Order(orderTable, orderLineItems))
            .isInstanceOf(DuplicateOrderLineItemsException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(MenuTestFixtures.서비스군만두, new Quantity(5L))
        );
        Order order = new Order(orderTable, orderLineItems);

        //when
        order.changeOrderStatus(OrderStatus.MEAL);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("계산완료 상태에서는 주문상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_exception() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(MenuTestFixtures.서비스군만두, new Quantity(5L))
        );
        Order order = new Order(orderTable, orderLineItems);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when, then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
            .isInstanceOf(CompleteOrderChangeStateException.class);
    }
}
