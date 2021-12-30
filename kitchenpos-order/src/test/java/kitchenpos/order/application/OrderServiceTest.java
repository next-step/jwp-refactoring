package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenuValidator;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.OrderTestFixtures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderMenuValidator orderMenuValidator;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

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
        Long orderTableId = 1L;
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(혼술세트.getId(), new Quantity(1L)),
            new OrderLineItem(이달의메뉴.getId(), new Quantity(3L)));

        OrderRequest requestOrder = OrderTestFixtures.convertToOrderRequest(
            new Order(orderTableId, orderLineItems));
        Order expectedOrder = new Order(1L, orderTableId, OrderStatus.COOKING,
            orderLineItems);
        OrderTestFixtures.주문_저장_결과_모킹(orderRepository, expectedOrder);

        // when
        OrderResponse savedOrder = orderService.create(requestOrder);

        // then
        assertThat(savedOrder.getId()).isEqualTo(expectedOrder.getId());
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        Long orderTableId = 1L;
        List<OrderLineItem> orderLineItems1 = Arrays.asList(
            new OrderLineItem(혼술세트.getId(), new Quantity(1L)),
            new OrderLineItem(이달의메뉴.getId(), new Quantity(3L)));
        List<OrderLineItem> orderLineItems2 = Arrays.asList(
            new OrderLineItem(혼술세트.getId(), new Quantity(2L)),
            new OrderLineItem(이달의메뉴.getId(), new Quantity(5L)));
        List<Order> orders = Arrays.asList(
            new Order(1L, orderTableId, OrderStatus.MEAL, orderLineItems1),
            new Order(2L, orderTableId, OrderStatus.COMPLETION, orderLineItems2));

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
        Long orderTableId = 1L;
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(혼술세트.getId(), new Quantity(1L)),
            new OrderLineItem(이달의메뉴.getId(), new Quantity(3L)));
        Order order = new Order(1L, orderTableId, OrderStatus.MEAL, orderLineItems);
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
