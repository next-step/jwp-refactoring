package kitchenpos.order.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("주문 서비스 테스트")
class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        // given
        OrderTable orderTable = 테이블_저장(false);
        MenuResponse savedMenuResponse = 메뉴_저장();

        OrderLineItem orderLineItem = new OrderLineItem(savedMenuResponse.getId(), 2);
        Order order = new Order(
                orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), Collections.singletonList(orderLineItem));

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus()),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull(),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getOrderId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(savedMenuResponse.getId()),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(orderLineItem.getQuantity())
        );
    }

    @Test
    @DisplayName("주문 항목 없이 주문을 등록하면 예외를 발생한다.")
    void createThrowException1() {
        // given
        OrderTable orderTable = 테이블_저장(false);
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), null);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderService.create(order));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴로 주문을 등록하면 예외를 발생한다.")
    void createThrowException2() {
        // given
        OrderTable orderTable = 테이블_저장(false);
        OrderLineItem orderLineItem = new OrderLineItem(0L, 2);
        Order order = new Order(
                orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), Collections.singletonList(orderLineItem));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderService.create(order));
    }

    @Test
    @DisplayName("존재하지 않는 테이블로 주문을 등록하면 예외를 발생한다.")
    void createThrowException3() {
        // given
        MenuResponse savedMenuResponse = 메뉴_저장();
        OrderLineItem orderLineItem = new OrderLineItem(savedMenuResponse.getId(), 2);
        Order order = new Order(0L, OrderStatus.COOKING, LocalDateTime.now(), Collections.singletonList(orderLineItem));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderService.create(order));
    }

    @Test
    @DisplayName("비어있는 테이블로 주문을 등록하면 예외를 발생한다.")
    void createThrowException4() {
        // given
        OrderTable orderTable = 테이블_저장(true);
        MenuResponse savedMenuResponse = 메뉴_저장();
        OrderLineItem orderLineItem = new OrderLineItem(savedMenuResponse.getId(), 2);
        Order order = new Order(
                orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), Collections.singletonList(orderLineItem));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderService.create(order));
    }

    @Test
    @DisplayName("주문의 목록을 조회한다.")
    void list() {
        // given
        주문_저장();

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders.size()).isOne();
    }

    @Test
    @DisplayName("주문의 주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        Order savedOrder = 주문_저장();
        Order order = new Order(OrderStatus.MEAL);

        // when
        Order modifiedOrder = orderService.changeOrderStatus(savedOrder.getId(), order);

        // then
        assertThat(modifiedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @Test
    @DisplayName("존재하지 않는 주문 ID로 주문의 주문 상태를 변경하면 예외를 발생한다.")
    void changeOrderStatusThrowException1() {
        // given
        Order order = new Order(OrderStatus.MEAL);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(0L, order));
    }

    @Test
    @DisplayName("주문 완료 상태로 주문의 주문 상태를 변경하면 예외를 발생한다.")
    void changeOrderStatusThrowException2() {
        // given
        Order savedOrder = 주문_저장();
        주문_상태를_COMPLETION_으로_상태_변경(savedOrder.getId());

        Order order = new Order(OrderStatus.MEAL);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), order));
    }
}
