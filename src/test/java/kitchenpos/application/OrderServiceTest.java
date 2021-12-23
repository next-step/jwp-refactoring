package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        List<OrderLineItem> orderLineItems = new ArrayList<>(Arrays.asList(orderLineItem));
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);

        OrderTable orderTable = new OrderTable();

        given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));
        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.save(any())).willReturn(orderLineItem);

        // when
        Order result = orderService.create(order);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderedTime()).isNotNull();
        assertThat(result.getOrderLineItems().size()).isEqualTo(orderLineItems.size());
    }

    @Test
    @DisplayName("주문 항목이 비어있으면 등록에 실패한다.")
    void create_empty_order_line_items() {
        // given
        Order order = new Order();

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴들이 존재하지 않는 메뉴가 포함되면 등록에 실패한다.")
    void create_not_equals_order_line_items_count() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        List<OrderLineItem> orderLineItems = new ArrayList<>(Arrays.asList(orderLineItem));
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);


        given(menuDao.countByIdIn(any())).willReturn((long)(orderLineItems.size() + 1));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블이면 등록에 실패한다.")
    void create_not_exist_order_table() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        List<OrderLineItem> orderLineItems = new ArrayList<>(Arrays.asList(orderLineItem));
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);

        given(menuDao.countByIdIn(any())).willReturn((long)(orderLineItems.size()));
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이면 등록에 실패한다.")
    void create_order_table_is_empty() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        List<OrderLineItem> orderLineItems = new ArrayList<>(Arrays.asList(orderLineItem));
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        given(menuDao.countByIdIn(any())).willReturn((long)(orderLineItems.size()));
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        // given
        Order order = new Order();
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        given(orderDao.findAll()).willReturn(new ArrayList<>(Arrays.asList(order)));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(orderLineItems);

        // when
        List<Order> result = orderService.list();

        // then
        assertThat(result).isNotNull();
        assertThat(order.getOrderLineItems().size()).isEqualTo(orderLineItems.size());
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());

        given(orderDao.findById(any())).willReturn(Optional.ofNullable(order));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(new ArrayList<>());

        Order paramOrder = new Order();
        paramOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when
        Order result = orderService.changeOrderStatus(1L, paramOrder);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(paramOrder.getOrderStatus());
        assertThat(result.getOrderLineItems()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 주문이면 주문 상태 변경이 실패한다.")
    void changeOrderStatus_not_exist_order() {
        // given
        given(orderDao.findById(any())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 계산 완료된 주문이면 주문 상태 변경이 실패한다.")
    void changeOrderStatus_order_status_already_complete() {
        // given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderDao.findById(any())).willReturn(Optional.ofNullable(order));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
