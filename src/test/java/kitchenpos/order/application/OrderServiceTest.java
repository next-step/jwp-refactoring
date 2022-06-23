package kitchenpos.order.application;

import static kitchenpos.ServiceTestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.menu.dao.FakeMenuDao;
import kitchenpos.order.dao.FakeOrderDao;
import kitchenpos.order.dao.FakeOrderLineItemDao;
import kitchenpos.table.dao.FakeOrderTableDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


public class OrderServiceTest {
    private final OrderService orderService = new OrderService(
            new FakeMenuDao(),
            new FakeOrderDao(),
            new FakeOrderLineItemDao(),
            new FakeOrderTableDao()
    );

    private Order order;
    private Order otherOrder;
    private OrderLineItem orderLineItem;
    private OrderLineItem otherOrderLineItem;

    @BeforeEach
    void setUp() {
        order=  createOrderBy(1L, 1L, OrderStatus.COOKING.name());
        otherOrder =  createOrderBy(2L, 2L, OrderStatus.COOKING.name());
        orderLineItem = createOrderLineItemBy(1L, 1L, 1L, 1L);
        otherOrderLineItem = createOrderLineItemBy(2L, 2L, 1L, 1L);
        otherOrder.setOrderLineItems(Arrays.asList(otherOrderLineItem));
    }

    @Test
    @DisplayName("주문 상품이 없는 경우, 예외를 반환한다.")
    void createFail() {
        //given
        Order order=  new Order();
        //when,then
        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상품의 메뉴가 존재하지 않는 경우, 예외를 반환한다.")
    void createWithNoExistingMenu() {
        //given
        orderLineItem.setMenuId(99L);
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        //when,then
        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않으면 예외를 반환한다.")
    void createWithNoExistingOrderTable() {
        //given
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderTableId(99L);
        //when,then
        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이면 예외를 반환한다.")
    void createWithEmptyOrderTable() {
        //given
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderTableId(4L);
        //when,then
        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        //given
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        //when
        Order saved = orderService.create(order);
        OrderLineItem actual = saved.getOrderLineItems().get(0);
        //then
        assertAll(
                () -> assertThat(saved.getId()).isEqualTo(1L),
                () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getMenuId()).isEqualTo(orderLineItem.getMenuId()),
                () -> assertThat(actual.getOrderId()).isEqualTo(orderLineItem.getOrderId())
        );
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void findAll() {
        //given
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        orderService.create(order);
        //when
        List<Order> actual = orderService.list();
        //then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("주문이 존재하지 않으면 주문 상태를 변경할 수 없다.")
    void changeOrderStatusWithNoExistingOrder() {
        //when, then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(99L, order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문상태가 계산완료인경우, 주문 상태를 변경할 수 없다.")
    void changeOrderStatusWithCompletion() {
        //given
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        orderService.create(order);
        Order saved = orderService.create(otherOrder);

        //when, then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(saved.getId(), this.order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        //given
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        Order saved = orderService.create(this.order);
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.MEAL.name());
        //when
        Order changed = orderService.changeOrderStatus(saved.getId(), newOrder);
        //then
        assertThat(changed.getOrderStatus()).isEqualTo(newOrder.getOrderStatus());
    }
}
