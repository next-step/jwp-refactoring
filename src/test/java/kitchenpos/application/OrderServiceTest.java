package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

@DisplayName("주문 관련")
@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @MockBean
    MenuDao menuDao;
    @MockBean
    OrderDao orderDao;
    @MockBean
    OrderLineItemDao orderLineItemDao;
    @MockBean
    OrderTableDao orderTableDao;

    Long orderTableId = 1L;
    OrderTable orderTable = new OrderTable(orderTableId, null, 0, false);
    Long orderId = 1L;
    Long menuId = 1L;
    OrderLineItem orderLineItem = new OrderLineItem(1L, orderId, menuId, 1);
    Order order = new Order(orderId, orderTableId, "COOKING", LocalDateTime.now(), null);

    @BeforeEach
    void setUp() {
        when(menuDao.countByIdIn(singletonList(menuId))).thenReturn(1L);
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable));
        when(orderDao.findById(orderId)).thenReturn(Optional.of(order));
        when(orderDao.findAll()).thenReturn(singletonList(order));
        when(orderLineItemDao.findAllByOrderId(orderId)).thenReturn(singletonList(orderLineItem));
    }

    @DisplayName("주문을 생성할 수 있다")
    @Test
    void create() {
        // given
        OrderLineItem createOrderLineItem = new OrderLineItem(1L, null, menuId, 1);
        Order createOrder = new Order(null, orderTableId, null, null, singletonList(createOrderLineItem));
        when(orderDao.save(createOrder)).thenReturn(order);
        when(orderLineItemDao.save(createOrderLineItem)).thenReturn(orderLineItem);

        // when
        Order actual = orderService.create(createOrder);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isNotNull();
            softAssertions.assertThat(actual.getOrderStatus()).isEqualTo("COOKING");
            softAssertions.assertThat(actual.getOrderedTime()).isNotNull();
            softAssertions.assertThat(actual.getOrderLineItems()).hasSize(1);
            softAssertions.assertThat(actual.getOrderLineItems()).allSatisfy(orderLineItem1 -> assertThat(orderLineItem1.getOrderId()).isNotNull());
        });
    }

    @DisplayName("주문 항목이 없으면 안된다")
    @Test
    void orderLineItem_is_not_empty() {
        // given
        Order createOrder = new Order(null, orderTableId, null, null, null);

        // when then
        assertThatThrownBy(() -> orderService.create(createOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 메뉴를 주문할 수 없다")
    @Test
    void menu_is_exists() {
        // given
        Long notExistsMenuId = 1000L;
        OrderLineItem createOrderLineItem = new OrderLineItem(1L, null, notExistsMenuId, 1);
        Order createOrder = new Order(null, orderTableId, null, null, singletonList(createOrderLineItem));

        // when then
        assertThatThrownBy(() -> orderService.create(createOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 지정되어야 한다")
    @Test
    void orderTable_is_not_null() {
        // given
        OrderLineItem createOrderLineItem = new OrderLineItem(1L, null, menuId, 1);
        Order createOrder = new Order(null, null, null, null, singletonList(createOrderLineItem));

        // when then
        assertThatThrownBy(() -> orderService.create(createOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 주문 테이블에 주문할 수 없다")
    @Test
    void orderTable_is_exists() {
        // given
        Long notExistsOrderTableId = 1000L;
        OrderLineItem createOrderLineItem = new OrderLineItem(1L, null, menuId, 1);
        Order createOrder = new Order(null, notExistsOrderTableId, null, null, singletonList(createOrderLineItem));

        // when then
        assertThatThrownBy(() -> orderService.create(createOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블에 주문할 수 없다")
    @Test
    void orderTable_is_not_empty() {
        // given
        Long emptyOrderTableId = 1001L;
        OrderTable emptyOrderTable = new OrderTable(emptyOrderTableId, null, 0, true);
        when(orderTableDao.findById(emptyOrderTableId)).thenReturn(Optional.of(emptyOrderTable));
        OrderLineItem createOrderLineItem = new OrderLineItem(1L, null, menuId, 1);
        Order createOrder = new Order(null, emptyOrderTableId, null, null, singletonList(createOrderLineItem));

        // when then
        assertThatThrownBy(() -> orderService.create(createOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        List<Order> actual = orderService.list();

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual).allSatisfy(order1 -> assertThat(order1.getOrderLineItems()).hasSize(1));
        });
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void changeOrderStatus() {
        // given
        Order changeStatus = new Order(null, null, "MEAL", null, null);

        // when
        Order actual = orderService.changeOrderStatus(orderId, changeStatus);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo("MEAL");
    }

    @DisplayName("없는 주문은 상태를 변경할 수 없다")
    @Test
    void order_is_exists() {
        // given
        Long notExistsOrderId = 1000L;
        Order changeStatus = new Order(null, null, "MEAL", null, null);

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(notExistsOrderId, changeStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료 된 주문은 상태를 변경할 수 없다")
    @Test
    void completion_order_cannot_change() {
        // given
        Long completionOrderId = 1000L;
        Order completionOrder = new Order(completionOrderId, orderTableId, "COMPLETION", LocalDateTime.now(), null);
        when(orderDao.findById(completionOrderId)).thenReturn(Optional.of(completionOrder));
        Order changeStatus = new Order(null, null, "MEAL", null, null);

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrderId, changeStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
