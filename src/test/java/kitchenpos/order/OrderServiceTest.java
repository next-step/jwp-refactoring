package kitchenpos.order;

import kitchenpos.order.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

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

    @DisplayName("주문한다.")
    @Test
    void order() {

        //given

        OrderLineItem itemA = new OrderLineItem();
        itemA.setSeq(1L);
        itemA.setMenuId(1L);

        OrderLineItem itemB = new OrderLineItem();
        itemB.setSeq(2L);
        itemB.setMenuId(2L);

        List<OrderLineItem> orderLineItems = Arrays.asList(itemA, itemB);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(orderLineItems);


        when(menuDao.countByIdIn(anyList())).thenReturn((long)orderLineItems.size());
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.ofNullable(orderTable));
        when(orderDao.save(any())).thenReturn(order);
        when(orderLineItemDao.save(itemA)).thenReturn(itemA);
        when(orderLineItemDao.save(itemB)).thenReturn(itemB);

        //when
        Order savedOrder = orderService.create(order);

        //then
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isGreaterThan(0L);
    }

    @DisplayName("주문 리스트를 조회한다.")
    @Test
    void getOrders() {

        //given
        Order orderA = new Order();
        orderA.setId(1L);

        Order orderB = new Order();
        orderB.setId(2L);

        Order orderC = new Order();
        orderC.setId(3L);

        List<Order> orders = Arrays.asList(orderA, orderB, orderC);
        List<OrderLineItem> items =  Arrays.asList(new OrderLineItem());

        when(orderDao.findAll()).thenReturn(orders);
        when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(items);

        //when
        List<Order> findOrders = orderService.list();

        //then
        assertThat(findOrders).isNotEmpty();
        assertThat(findOrders).contains(orderA, orderB, orderC);
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void changeOrderStatus() {

        //given
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.MEAL.name());

        when(orderDao.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        //when
        Order changeOrder = orderService.changeOrderStatus(order.getId(), order);

        //then
        assertThat(changeOrder).isNotNull();
        assertThat(changeOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태를 수정할 시 주문 상태가 Completion 일 경우")
    @Test
    void changeOrderStatusByOrderStatusCompletion() {

        //given
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        when(orderDao.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order));

    }

}
