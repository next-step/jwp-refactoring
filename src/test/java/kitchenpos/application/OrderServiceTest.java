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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

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

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void saveOrder() {
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, null, 5);
        final OrderLineItem orderLineItem2 = new OrderLineItem(1L, 1L, null, 5);
        final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        final Order order = new Order(1L, 1L, null, null, orderLineItems);
        final OrderTable orderTable = new OrderTable(1L, null, 0, false);
        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.save(any())).willReturn(orderLineItem1);
        given(orderLineItemDao.save(any())).willReturn(orderLineItem2);

        final Order actual = orderService.create(order);

        assertAll(
                () -> assertThat(actual).isEqualTo(order),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderLineItems()).hasSize(2)
        );
    }

    @DisplayName("등록된 주문을 조회한다.")
    @Test
    void findOrders() {
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, null, 5);
        final OrderLineItem orderLineItem2 = new OrderLineItem(1L, 1L, null, 5);
        final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        final Order order = new Order(1L, 1L, null, null, orderLineItems);

        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(orderLineItems);

        final List<Order> actual = orderService.list();

        assertAll(
                () -> assertThat(actual).isNotEmpty(),
                () -> assertThat(actual.get(0)).isEqualTo(order),
                () -> assertThat(actual.get(0).getOrderLineItems()).hasSize(2)
        );
    }

    @DisplayName("주문 상태를 완료로 변경한다.")
    @Test
    void changeOrderStatus() {
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, null, 5);
        final OrderLineItem orderLineItem2 = new OrderLineItem(1L, 1L, null, 5);
        final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        final Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        final Order saveOrder = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems);

        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
        given(orderDao.save(any())).willReturn(saveOrder);
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(orderLineItems);

        final Order changeOrderStatus = new Order(null, null, OrderStatus.COMPLETION.name(), null, null);
        final Order actual = orderService.changeOrderStatus(1L, changeOrderStatus);

        assertThat(actual.getOrderStatus()).isEqualTo(changeOrderStatus.getOrderStatus());
    }
}
