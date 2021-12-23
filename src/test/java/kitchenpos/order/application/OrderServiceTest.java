package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.testfixtures.MenuTestFixtures;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.dao.OrderDao;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.testfixtures.OrderTestFixtures;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.testfixtures.TableTestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private MenuService menuService;

    @Mock
    private TableService tableService;

    @InjectMocks
    private OrderService orderService;

    private Menu 혼술세트;
    private Menu 이달의메뉴;

    @BeforeEach
    void setUp() {
        MenuGroup 신상메뉴그룹 = new MenuGroup("신상메뉴그룹");
        혼술세트 = new Menu(1L, "혼술세트", BigDecimal.valueOf(0), 신상메뉴그룹);
        이달의메뉴 = new Menu(2L, "이달의메뉴", BigDecimal.valueOf(0), 신상메뉴그룹);
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        //given
        OrderTable orderTable = new OrderTable(1L, 6, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(혼술세트, 1),
            new OrderLineItem(이달의메뉴, 3));

        OrderRequest requestOrder = OrderTestFixtures.convertToOrderRequest(
            new Order(orderTable, LocalDateTime.now(), orderLineItems));
        Order expectedOrder = new Order(1L, orderTable, OrderStatus.COOKING, LocalDateTime.now(),
            orderLineItems);

        MenuTestFixtures.특정_메뉴_조회_모킹(menuService, 혼술세트);
        MenuTestFixtures.특정_메뉴_조회_모킹(menuService, 이달의메뉴);
        OrderTestFixtures.주문_저장_결과_모킹(orderDao, expectedOrder);
        TableTestFixtures.특정_주문테이블_조회_모킹(tableService, orderTable);

        // when
        OrderResponse savedOrder = orderService.create(requestOrder);

        // then
        assertThat(savedOrder.getId()).isEqualTo(expectedOrder.getId());
    }

    @DisplayName("주문 항목은 1개 이상이어야 한다.")
    @Test
    void create_exception1() {
        //given
        OrderTable orderTable = new OrderTable(1L, 6, false);
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        OrderRequest order = OrderTestFixtures.convertToOrderRequest(
            new Order(orderTable, LocalDateTime.now(), orderLineItems));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중복된 메뉴가 존재하면 안된다.")
    @Test
    void create_exception2() {
        //given
        OrderTable orderTable = new OrderTable(1L, 6, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(혼술세트, 1),
            new OrderLineItem(이달의메뉴, 3));
        OrderRequest order = OrderTestFixtures.convertToOrderRequest(
            new Order(orderTable, LocalDateTime.now(), orderLineItems));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문종료 상태의 테이블은 주문할 수 없다.")
    @Test
    void create_exception3() {
        //given
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(혼술세트, 1));
        OrderRequest requestOrder = new OrderRequest(
            1L, OrderTestFixtures.convertToOrderLineItemRequests(orderLineItems));

        OrderTable expectedOrderTable = new OrderTable(1L, 6, true);
        TableTestFixtures.특정_주문테이블_조회_모킹(tableService, expectedOrderTable);

        // when, then
        assertThatThrownBy(() -> orderService.create(requestOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        OrderTable orderTable = new OrderTable(1L, 6, false);
        List<OrderLineItem> orderLineItems1 = Arrays.asList(
            new OrderLineItem(혼술세트, 1),
            new OrderLineItem(이달의메뉴, 3));
        List<OrderLineItem> orderLineItems2 = Arrays.asList(
            new OrderLineItem(혼술세트, 2),
            new OrderLineItem(이달의메뉴, 5));
        List<Order> orders = Arrays.asList(
            new Order(1L, orderTable, OrderStatus.MEAL, LocalDateTime.now(),
                orderLineItems1),
            new Order(2L, orderTable, OrderStatus.COMPLETION, LocalDateTime.now(),
                orderLineItems2));

        OrderTestFixtures.주문_전체_조회_모킹(orderDao, orders);

        //when
        List<OrderResponse> findOrders = orderService.list();

        //then
        assertThat(findOrders.size()).isEqualTo(orders.size());
        주문목록_검증(findOrders, orders);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderTable orderTable = new OrderTable(1L, 6, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(혼술세트, 1),
            new OrderLineItem(이달의메뉴, 3));
        Order order = new Order(1L, orderTable, OrderStatus.MEAL,
            LocalDateTime.now(), orderLineItems);
        OrderTestFixtures.특정_주문_조회_모킹(orderDao, order);

        //when
        OrderRequest changeOrder = new OrderRequest(OrderStatus.COMPLETION);
        OrderResponse savedOrder = orderService.changeOrderStatus(order.getId(), changeOrder);

        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(changeOrder.getOrderStatus());
    }

    @DisplayName("계산 완료된 주문 상태는 변경할 수 없다.")
    @Test
    void changeOrderStatus_exception() {
        // given
        OrderTable orderTable = new OrderTable(1L, 6, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(혼술세트, 1));

        Order order = new Order(1L, orderTable, OrderStatus.COMPLETION,
            LocalDateTime.now(), orderLineItems);
        OrderTestFixtures.특정_주문_조회_모킹(orderDao, order);

        //when
        OrderRequest changeOrder = new OrderRequest(OrderStatus.MEAL);
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private void 주문목록_검증(List<OrderResponse> findOrders, List<Order> orders) {
        List<Long> findOrderIds = findOrders.stream()
            .map(OrderResponse::getId)
            .collect(Collectors.toList());
        List<Long> expectOrderIds = orders.stream()
            .map(Order::getId)
            .collect(Collectors.toList());
        assertThat(findOrderIds).containsAll(expectOrderIds);
    }
}
