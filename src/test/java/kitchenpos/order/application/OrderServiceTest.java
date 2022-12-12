package kitchenpos.order.application;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.generator.BuilderArbitraryGenerator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderLineItemRepository;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.persistence.OrderTableRepository;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private MenuRepository menuDao;
    @Mock
    private OrderRepository orderDao;
    @Mock
    private OrderLineItemRepository orderLineItemDao;
    @Mock
    private OrderTableRepository orderTableDao;

    @DisplayName("주문을 추가할 경우 주문항목이 없으면 예외발생")
    @Test
    public void throwsExceptionWhenEmptyOrderItems() {
        Order order = Order.builder().orderLineItems(Collections.EMPTY_LIST).build();

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 등록되지 않는 메뉴가 있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsMenu() {
        List<OrderLineItem> orderLineItems = getOrderLineItems();
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        Order order = Order.builder().orderLineItems(orderLineItems).build();
        doReturn((long) orderLineItems.size() - 2)
                .when(menuDao).countByIdIn(menuIds);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 주문테이블이 등록안되있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsTable() {
        List<OrderLineItem> orderLineItems = getOrderLineItems();
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        Order order = Order.builder().orderLineItems(orderLineItems).build();
        doReturn((long) menuIds.size())
                .when(menuDao).countByIdIn(menuIds);
        doReturn(Optional.empty())
                .when(orderTableDao).findById(order.getOrderTableId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 주문테이블이 공석이면 예외발생")
    @Test
    public void throwsExceptionWhenEmptyTable() {
        List<OrderLineItem> orderLineItems = getOrderLineItems();
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        Order order = Order.builder().orderLineItems(orderLineItems).build();
        OrderTable orderTable = OrderTable.builder()
                .empty(true)
                .build();
        doReturn((long) menuIds.size())
                .when(menuDao).countByIdIn(menuIds);
        doReturn(Optional.ofNullable(orderTable))
                .when(orderTableDao).findById(order.getOrderTableId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 주문을 반환")
    @Test
    public void returnOrder() {
        List<OrderLineItem> orderLineItems = getOrderLineItems();
        OrderLineItem orderLineItem = orderLineItems.stream()
                .findFirst()
                .orElse(OrderLineItem.builder().build());
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        Order order = Order.builder().id(150l).orderLineItems(orderLineItems).build();

        OrderTable orderTable = OrderTable.builder()
                .empty(false)
                .build();
        doReturn((long) menuIds.size())
                .when(menuDao).countByIdIn(menuIds);
        doReturn(Optional.ofNullable(orderTable))
                .when(orderTableDao).findById(order.getOrderTableId());
        doReturn(order)
                .when(orderDao).save(order);
        doReturn(orderLineItem)
                .when(orderLineItemDao).save(any(OrderLineItem.class));

        Order savedOrder = orderService.create(order);

        assertAll(
                () -> assertThat(savedOrder).isNotNull(),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString()),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull());
    }

    @DisplayName("주문목록을 조회할경우 주문목록 반환")
    @Test
    public void returnOrders() {
        List<OrderLineItem> orderLineItems = getOrderLineItems();
        List<Order> orders = getOrders(Order.builder().id(150l).orderLineItems(orderLineItems).build(), 30);
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        doReturn(orders)
                .when(orderDao).findAll();
        doReturn(orderLineItems)
                .when(orderLineItemDao).findAllByOrder(any(Order.class));

        List<Order> findOrders = orderService.list();

        List<Long> findOrderIds = findOrders.stream().map(Order::getId).collect(Collectors.toList());
        assertThat(findOrderIds).containsAll(orderIds);

    }

    @DisplayName("주문상태를 수정할 경우 등록된 주문이 아니면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsOrder() {
        Long orderId = Arbitraries.longs().between(1, 1000).sample();
        doReturn(Optional.empty()).when(orderDao).findById(orderId);

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, Order.builder().build()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태를 수정할 경우 계산완료된 주문이면 예외발생")
    @Test
    public void throwsExceptionWhenCompleteOrder() {
        Order order = Order.builder()
                .id(Arbitraries.longs().between(1, 1000).sample())
                .orderStatus(OrderStatus.COMPLETION.name())
                .build();
        doReturn(Optional.ofNullable(order)).when(orderDao).findById(order.getId());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), Order.builder().build()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태를 수정할 경우 수정된 주문반환")
    @Test
    public void returnOrderWithChangedStatus() {
        Long orderId = Arbitraries.longs().between(1, 1000).sample();
        Order findOrder = Order.builder()
                .id(Arbitraries.longs().between(1, 1000).sample())
                .orderStatus(OrderStatus.COOKING.name())
                .build();
        Order order = Order.builder()
                .id(Arbitraries.longs().between(1, 1000).sample())
                .orderStatus(OrderStatus.COMPLETION.name())
                .build();
        List<OrderLineItem> orderLineItems = getOrderLineItems();
        doReturn(Optional.ofNullable(findOrder)).when(orderDao).findById(orderId);
        doReturn(orderLineItems).when(orderLineItemDao).findAllByOrder(any(Order.class));

        Order savedOrder = orderService.changeOrderStatus(orderId, order);

        assertAll(
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()),
                () -> assertThat(savedOrder.getOrderLineItems()).containsAll(orderLineItems));
    }

    private List<Order> getOrders(Order order, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> Order.builder()
                        .id(order.getId())
                        .orderTable(order.getOrderTable())
                        .orderStatus(order.getOrderStatus())
                        .orderLineItems(order.getOrderLineItems())
                        .build())
                .collect(Collectors.toList());
    }

    private List<OrderLineItem> getOrderLineItems() {
        return IntStream.rangeClosed(1, 20)
                .mapToObj(value -> OrderLineItem.builder()
                        .seq(Arbitraries.longs().between(1, 20).sample())
                        .menu(Menu.builder().id(Arbitraries.longs().between(1, 100).sample()).build())
                        .build())
                .collect(Collectors.toList());
    }
}
