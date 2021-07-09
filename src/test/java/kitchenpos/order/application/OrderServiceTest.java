package kitchenpos.order.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.domain.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private OrderTable orderTable;
    private Order order;
    private OrderLineItem orderLineItem;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(1L, 1L, 0, false);
        orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        given(menuRepository.countByIdIn(Arrays.asList(1L))).willReturn(order.getOrderLineItems().size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);

        // when
        Order createdOrder = orderService.create(order);

        // then
        assertThat(createdOrder.getId()).isEqualTo(order.getId());
        assertThat(createdOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(createdOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @DisplayName("주문 항목이 올바르지 않으면 등록할 수 없다 : 주문 항목은 1개 이상이어야 한다.")
    @Test
    void createTest_emptyOrderLineItem() {
        // given
        order.setOrderLineItems(null);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목이 올바르지 않으면 등록할 수 없다 : 주문 항목은 메뉴에 존재하고 중복되지않는 메뉴이어야 한다.")
    @Test
    void createTest_duplicateMenu() {
        // given
        orderLineItems.add(new OrderLineItem(2L, 1L, 1L, 1L));
        given(menuRepository.countByIdIn(Arrays.asList(1L, 1L))).willReturn(1);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 올바르지 않으면 등록할 수 없다 : 주문 테이블은 등록된 주문 테이블이어야 한다.")
    @Test
    void createTest_unregisteredOrderTable() {
        // given
        order.setOrderTableId(100L);
        given(menuRepository.countByIdIn(Arrays.asList(1L))).willReturn(order.getOrderLineItems().size());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusTest() {
        // given
        order.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(orderLineItems);

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
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        given(orderDao.findAll()).willReturn(Arrays.asList(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(orderLineItems);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(orderLineItems.size());
    }
}
