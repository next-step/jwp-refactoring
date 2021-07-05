package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderService orderService;
    private Order order;
    private OrderTable orderTable;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;

    @BeforeEach
    void setUp() {

        orderLineItem1 = new OrderLineItem();
        orderLineItem1.setSeq(1L);

        orderLineItem2 = new OrderLineItem();
        orderLineItem2.setSeq(2L);


        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);

        order = new Order();
        order.setId(1L);
        order.setOrderStatus(COOKING.name());
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @Test
    @DisplayName("주문 항목이 없는 경우 예외가 발생한다")
    void notExistOrderItemTest() {

        // given
        order.setOrderLineItems(null);

        // then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목 개수가 각 주문 항목이 포함된 메뉴의 개수의 합과 다른 경우 예외가 발생한다")
    void notValidOrderLineItemsLengthTest() {

        // given
        when(menuDao.countByIdIn(any())).thenReturn(3L);

        // then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문이 발생한 주문 테이블이 없는 경우 예외가 발생한다")
    void notExistOrderTableTest() {

        // given
        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어 있는 경우 예외가 발생한다")
    void notOccupiedOrderTableTest() {

        // given
        orderTable.setEmpty(true);

        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        // then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성한다")
    void createTest() {

        // given
        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderDao.save(any())).thenReturn(order);
        when(orderLineItemDao.save(any())).thenReturn(orderLineItem1);

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name());
        assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId());
        assertThat(savedOrder.getOrderLineItems()).hasSize(2);
        assertThat(savedOrder.getOrderLineItems()).contains(orderLineItem1, orderLineItem1);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void listTest() {

        // given
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);

        when(orderDao.findAll()).thenReturn(Collections.singletonList(order));
        when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(orderLineItems);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders).containsExactly(order);
    }

    @Test
    @DisplayName("존재하지 않는 주문인 경우 예외가 발생한다")
    void notExistOrderTest() {

        // given
        when(orderDao.findById(any())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(2L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태가 완료인 경우 예외가 발생한다")
    void notValidOrderStatusTest() {

        // given
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(any())).thenReturn(Optional.of(order));

        // then
        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(2L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatusTest() {
        // given
        when(orderDao.findById(any())).thenReturn(Optional.of(order));
        when(orderDao.save(any())).thenReturn(order);
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        // then
        Order savedOrder = orderService.changeOrderStatus(2L, this.order);

        // then
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(savedOrder.getOrderLineItems()).hasSize(2);
        assertThat(savedOrder.getOrderLineItems()).hasSize(2);
        assertThat(savedOrder.getOrderLineItems()).contains(orderLineItem1, orderLineItem2);
    }
}