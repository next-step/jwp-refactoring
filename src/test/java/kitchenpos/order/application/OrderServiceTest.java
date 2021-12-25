package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.testfixtures.MenuTestFixtures;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.testfixtures.OrderTestFixtures;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.testfixtures.TableTestFixtures;
import kitchenpos.ordertable.vo.NumberOfGuests;
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
    private OrderRepository orderRepository;

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
        혼술세트 = new Menu(1L, "혼술세트", Price.valueOf(BigDecimal.ZERO), 신상메뉴그룹);
        이달의메뉴 = new Menu(2L, "이달의메뉴", Price.valueOf(BigDecimal.ZERO), 신상메뉴그룹);
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(혼술세트, new Quantity(1L)),
            new OrderLineItem(이달의메뉴, new Quantity(3L)));

        OrderRequest requestOrder = OrderTestFixtures.convertToOrderRequest(
            new Order(orderTable, orderLineItems));
        Order expectedOrder = new Order(1L, orderTable, OrderStatus.COOKING, orderLineItems);

        MenuTestFixtures.특정_메뉴_조회_모킹(menuService, 혼술세트);
        MenuTestFixtures.특정_메뉴_조회_모킹(menuService, 이달의메뉴);
        OrderTestFixtures.주문_저장_결과_모킹(orderRepository, expectedOrder);
        TableTestFixtures.특정_주문테이블_조회_모킹(tableService, orderTable);

        // when
        OrderResponse savedOrder = orderService.create(requestOrder);

        // then
        assertThat(savedOrder.getId()).isEqualTo(expectedOrder.getId());
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), false);
        List<OrderLineItem> orderLineItems1 = Arrays.asList(
            new OrderLineItem(혼술세트, new Quantity(1L)),
            new OrderLineItem(이달의메뉴, new Quantity(3L)));
        List<OrderLineItem> orderLineItems2 = Arrays.asList(
            new OrderLineItem(혼술세트, new Quantity(2L)),
            new OrderLineItem(이달의메뉴, new Quantity(5L)));
        List<Order> orders = Arrays.asList(
            new Order(1L, orderTable, OrderStatus.MEAL, orderLineItems1),
            new Order(2L, orderTable, OrderStatus.COMPLETION, orderLineItems2));

        OrderTestFixtures.주문_전체_조회_모킹(orderRepository, orders);

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
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(혼술세트, new Quantity(1L)),
            new OrderLineItem(이달의메뉴, new Quantity(3L)));
        Order order = new Order(1L, orderTable, OrderStatus.MEAL, orderLineItems);
        OrderTestFixtures.특정_주문_조회_모킹(orderRepository, order);

        //when
        OrderRequest changeOrder = OrderTestFixtures.convertToChangeOrderStatusRequest(
            OrderStatus.COMPLETION);
        OrderResponse savedOrder = orderService.changeOrderStatus(order.getId(), changeOrder);

        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(changeOrder.getOrderStatus());
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
