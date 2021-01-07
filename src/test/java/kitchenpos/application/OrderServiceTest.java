package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.exceptions.*;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Transactional
    @DisplayName("1개 미만의 주문 항목으로 주문할 수 없다.")
    @Test
    void createOrderFailWithNotEnoughOrderLineItemsTest() {
        // given
        Order orderRequest = new Order();
        orderRequest.setOrderLineItems(new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(InvalidTryOrderException.class)
                .hasMessage("주문하기 위해서는 1개 이상의 주문 항목이 필요합니다.");
    }

    @Transactional
    @DisplayName("메뉴에 없는 주문 항목으로 주문할 수 없다.")
    @Test
    void createOrderFailWithNotExistMenuTest() {
        // given
        OrderLineItem notInMenu = new OrderLineItem();
        notInMenu.setMenuId(100000L);

        Order withNotInMenuOrderRequest = new Order();
        withNotInMenuOrderRequest.setOrderTableId(1L);
        withNotInMenuOrderRequest.setOrderLineItems(Collections.singletonList(notInMenu));

        // when, then
        assertThatThrownBy(() -> orderService.create(withNotInMenuOrderRequest))
                .isInstanceOf(MenuEntityNotFoundException.class)
                .hasMessage("메뉴에 없는 주문 항목으로 주문할 수 없습니다.");
    }

    @Transactional
    @DisplayName("존재하지 않는 주문테이블에서 주문할 수 없다.")
    @Test
    void createOrderFailWithNotExistOrderTableTest() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);

        Order withNotExistOrderTable = new Order();
        withNotExistOrderTable.setOrderTableId(100000L);
        withNotExistOrderTable.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(withNotExistOrderTable))
                .isInstanceOf(OrderTableEntityNotFoundException.class)
                .hasMessage("존재하지 않는 주문 테이블에서 주문할 수 없습니다.");
    }

    @Transactional
    @DisplayName("비어있는 주문 테이블에서 주문할 수 없다.")
    @Test
    void createOrderFailWithEmptyOrderTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable emptyTable = tableService.create(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);

        Order withEmptyOrderTable = new Order();
        withEmptyOrderTable.setOrderTableId(emptyTable.getId());
        withEmptyOrderTable.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(withEmptyOrderTable))
                .isInstanceOf(InvalidTryOrderException.class)
                .hasMessage("비어있는 주문 테이블에서 주문할 수 없습니다.");
    }

    @Transactional
    @DisplayName("주문할 수 있다.")
    @Test
    void createOrderTest() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable fullOrderTable = tableService.create(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);

        Order orderRequest = new Order();
        orderRequest.setOrderTableId(fullOrderTable.getId());
        orderRequest.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when
        Order order = orderService.create(orderRequest);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(order.getOrderTableId()).isEqualTo(fullOrderTable.getId());
        assertThat(order.getOrderedTime()).isNotNull();
        assertThat(order.getOrderLineItems()).hasSize(1);
        assertThat(orderLineItem.getOrderId()).isEqualTo(order.getId());
    }

    @Transactional
    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void getOrdersTest() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable fullOrderTable = tableService.create(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);

        Order orderRequest = new Order();
        orderRequest.setOrderTableId(fullOrderTable.getId());
        orderRequest.setOrderLineItems(Collections.singletonList(orderLineItem));

        Order order = orderService.create(orderRequest);

        // when
        List<Order> orders = orderService.list();
        Stream<Long> ids = orders.stream()
                .map(Order::getId);

        // then
        assertThat(ids).contains(order.getId());
    }

    @Transactional
    @DisplayName("존재하지 않는 주문의 주문 상태를 바꿀 수 없다.")
    @Test
    void changeOrderStatusFailWithNotExistOrderTest() {
        // given
        Long notExistOrder = 1L;

        Order changeOrderRequest = new Order();
        changeOrderRequest.setOrderStatus(OrderStatus.MEAL.name());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrder, changeOrderRequest))
                .isInstanceOf(OrderEntityNotFoundException.class)
                .hasMessage("존재하지 않는 주문입니다.");
    }

    @Transactional
    @DisplayName("주문의 주문 상태를 바꿀 수 있다.")
    @Test
    void changeOrderStatusTest() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable fullOrderTable = tableService.create(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);

        Order orderRequest = new Order();
        orderRequest.setOrderTableId(fullOrderTable.getId());
        orderRequest.setOrderLineItems(Collections.singletonList(orderLineItem));

        Order order = orderService.create(orderRequest);

        Order changeOrderRequest = new Order();
        changeOrderRequest.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        Order changedOrder = orderService.changeOrderStatus(order.getId(), changeOrderRequest);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Transactional
    @DisplayName("주문 상태가 계산 완료인 주문의 주문 상태를 바꿀 수 없다.")
    @Test
    void changeOrderStatusFailWithInvalidOrderStatus() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable fullOrderTable = tableService.create(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);

        Order orderRequest = new Order();
        orderRequest.setOrderTableId(fullOrderTable.getId());
        orderRequest.setOrderLineItems(Collections.singletonList(orderLineItem));

        Order order = orderService.create(orderRequest);

        Order changeOrderRequest = new Order();
        changeOrderRequest.setOrderStatus(OrderStatus.COMPLETION.name());

        orderService.changeOrderStatus(order.getId(), changeOrderRequest);

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrderRequest))
                .isInstanceOf(InvalidTryChangeOrderStatusException.class)
                .hasMessage("계산 완료된 주문의 상태를 바꿀 수 없습니다.");
    }
}