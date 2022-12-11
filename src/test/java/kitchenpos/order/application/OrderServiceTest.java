package kitchenpos.order.application;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.generator.BuilderArbitraryGenerator;
import kitchenpos.menu.persistence.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderDao;
import kitchenpos.order.persistence.OrderLineItemDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.persistence.OrderTableDao;
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
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    public static FixtureMonkey fixtureMonkey;

    @BeforeAll
    public static void setup() {
        fixtureMonkey = FixtureMonkey.builder()
                .defaultGenerator(BuilderArbitraryGenerator.INSTANCE)
                .build();
    }

    @DisplayName("주문을 추가할 경우 주문항목이 없으면 예외발생")
    @Test
    public void throwsExceptionWhenEmptyOrderItems() {
        Order order = fixtureMonkey
                .giveMeBuilder(Order.class)
                .set("orderLineItems", Collections.EMPTY_LIST)
                .sample();

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 등록되지 않는 메뉴가 있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsMenu() {
        List<OrderLineItem> orderLineItems = fixtureMonkey
                .giveMeBuilder(OrderLineItem.class)
                .sampleList(Arbitraries.integers().between(1, 100).sample());
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        Order order = fixtureMonkey
                .giveMeBuilder(Order.class)
                .set("orderLineItems", orderLineItems)
                .sample();
        doReturn((long) orderLineItems.size() - 2)
                .when(menuDao).countByIdIn(menuIds);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 주문테이블이 등록안되있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsTable() {
        List<OrderLineItem> orderLineItems = fixtureMonkey
                .giveMeBuilder(OrderLineItem.class)
                .sampleList(Arbitraries.integers().between(1, 100).sample());
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        Order order = fixtureMonkey
                .giveMeBuilder(Order.class)
                .set("orderLineItems", orderLineItems)
                .sample();
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
        List<OrderLineItem> orderLineItems = fixtureMonkey
                .giveMeBuilder(OrderLineItem.class)
                .sampleList(Arbitraries.integers().between(1, 100).sample());
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        Order order = fixtureMonkey
                .giveMeBuilder(Order.class)
                .set("orderLineItems", orderLineItems)
                .sample();
        OrderTable orderTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .set("empty", true)
                .sample();
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
        List<OrderLineItem> orderLineItems = fixtureMonkey
                .giveMeBuilder(OrderLineItem.class)
                .setNull("orderId")
                .sampleList(Arbitraries.integers().between(1, 100).sample());
        OrderLineItem orderLineItem = orderLineItems.stream()
                .findFirst()
                .orElse(OrderLineItem.builder().build());
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        Order order = fixtureMonkey
                .giveMeBuilder(Order.class)
                .set("id", 150l)
                .set("orderLineItems", orderLineItems)
                .sample();
        OrderTable orderTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .set("empty", false)
                .sample();
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
        List<OrderLineItem> orderLineItems = fixtureMonkey
                .giveMeBuilder(OrderLineItem.class)
                .setNull("orderId")
                .sampleList(Arbitraries.integers().between(1, 100).sample());
        List<Order> orders = fixtureMonkey
                .giveMeBuilder(Order.class)
                .set("id", 150l)
                .set("orderLineItems", orderLineItems)
                .sampleList(Arbitraries.integers().between(1, 100).sample());
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        doReturn(orders)
                .when(orderDao).findAll();
        doReturn(orderLineItems)
                .when(orderLineItemDao).findAllByOrderId(anyLong());

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
        Order order = fixtureMonkey
                .giveMeBuilder(Order.class)
                .set("id", Arbitraries.longs().between(1, 1000).sample())
                .set("orderStatus", OrderStatus.COMPLETION.name())
                .sample();
        doReturn(Optional.ofNullable(order)).when(orderDao).findById(order.getId());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), Order.builder().build()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태를 수정할 경우 수정된 주문반환")
    @Test
    public void returnOrderWithChangedStatus() {
        Long orderId = Arbitraries.longs().between(1, 1000).sample();
        Order findOrder = fixtureMonkey
                .giveMeBuilder(Order.class)
                .set("orderStatus", OrderStatus.COOKING.name())
                .sample();
        Order order = fixtureMonkey
                .giveMeBuilder(Order.class)
                .set("orderStatus", OrderStatus.COMPLETION.name())
                .sample();
        List<OrderLineItem> orderLineItems = fixtureMonkey
                .giveMeBuilder(OrderLineItem.class)
                .sampleList(Arbitraries.integers().between(1, 100).sample());
        doReturn(Optional.ofNullable(findOrder)).when(orderDao).findById(orderId);
        doReturn(orderLineItems).when(orderLineItemDao).findAllByOrderId(orderId);

        Order savedOrder = orderService.changeOrderStatus(orderId, order);

        assertAll(
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()),
                () -> assertThat(savedOrder.getOrderLineItems()).containsAll(orderLineItems));
    }
}
