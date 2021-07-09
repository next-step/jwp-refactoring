package kitchenpos.order.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private MenuGroup 추천메뉴;
    private Menu 강정치킨plus강정치킨;
    private OrderTable orderTable;
    private Order order;
    private OrderLineItem orderLineItem;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        추천메뉴 = new MenuGroup(1L, "추천메뉴");
        강정치킨plus강정치킨 = new Menu(1L, "강정치킨+강정치킨", BigDecimal.valueOf(20000), 추천메뉴);
        orderTable = new OrderTable(1L, 1L, 0, false);
        order = new Order(1L, orderTable);

        orderLineItem = new OrderLineItem(1L, order, 강정치킨plus강정치킨, 1);
        orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        order.addOrderLineItems(orderLineItems);
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        given(menuRepository.countByIdIn(Arrays.asList(1L))).willReturn(order.getOrderLineItems().size());
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderRepository.save(any())).willReturn(order);
        given(orderLineItemRepository.save(any())).willReturn(orderLineItem);

        // when
        Order createdOrder = orderService.create(order);

        // then
        assertThat(createdOrder.getId()).isEqualTo(order.getId());
        assertThat(createdOrder.getOrderTable().getId()).isEqualTo(order.getOrderTable().getId());
        assertThat(createdOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @DisplayName("주문 항목이 올바르지 않으면 등록할 수 없다 : 주문 항목은 1개 이상이어야 한다.")
    @Test
    void createTest_emptyOrderLineItem() {
        // given
        Order order = new Order(2L, orderTable, new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목이 올바르지 않으면 등록할 수 없다 : 주문 항목은 메뉴에 존재하고 중복되지않는 메뉴이어야 한다.")
    @Test
    void createTest_duplicateMenu() {
        // given
        orderLineItems.add(new OrderLineItem(2L, order, 강정치킨plus강정치킨, 1));
        given(menuRepository.countByIdIn(any())).willReturn(1);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 올바르지 않으면 등록할 수 없다 : 주문 테이블은 등록된 주문 테이블이어야 한다.")
    @Test
    void createTest_unregisteredOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(100L, 1L, 0, false);
        Order order = new Order(2L, orderTable, new ArrayList<>());

        given(menuRepository.countByIdIn(any())).willReturn(order.getOrderLineItems().size());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusTest() {
        // given
        order.setOrderStatus(OrderStatus.MEAL.name());
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        given(orderLineItemRepository.findAllByOrderId(any())).willReturn(orderLineItems);

        // when
        Order changedOrder = orderService.changeOrderStatus(order.getId(), order);

        // then
        assertThat(changedOrder.getId()).isEqualTo(order.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문의 상태가 올바르지 않으면 변경할 수 없다 : 주문의 상태가 ('요리중', '식사중') 이어야 한다.")
    @Test
    void changeOrderStatusTest_orderStatusCompletion() {
        // given
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        given(orderRepository.findAll()).willReturn(Arrays.asList(order));
        given(orderLineItemRepository.findAllByOrderId(any())).willReturn(orderLineItems);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(orderLineItems.size());
    }
}
