package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static kitchenpos.util.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@SpringBootTest
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

    @BeforeEach
    void setUp() {
        orderTable = orderTable_생성(1L, tableGroup.getId(), 3);
        orderLineItem = orderLineItem_생성(1L, menu.getId(), 2);
        order = order_생성(1L, OrderStatus.COOKING.name(), Collections.singletonList(orderLineItem));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void createOrder() {
        given(menuDao.countByIdIn(anyList())).willReturn((long) order.getOrderLineItems().size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem에_orderId_추가(orderLineItem, order.getId()));

        Order result = orderService.create(order);
        assertThat(result).isEqualTo(order);
    }

    @DisplayName("주문 항목이 하나도 없을 경우 등록할 수 없다.")
    @Test
    void createOrderException1() {
        order.setOrderLineItems(null);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("선택한 주문 항목들이 모두 등록되어 있지 않으면 등록할 수 없다.")
    @Test
    void createOrderException2() {
        given(menuDao.countByIdIn(anyList())).willReturn((long) 2);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("해당 주문 테이블이 등록되어 있지 않으면 등록할 수 없다.")
    @Test
    void createOrderException3() {
        given(menuDao.countByIdIn(anyList())).willReturn((long) order.getOrderLineItems().size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블일 경우 등록할 수 없다.")
    @Test
    void createOrderException4() {
        given(menuDao.countByIdIn(anyList())).willReturn((long) order.getOrderLineItems().size());
        orderTable.setEmpty(true);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(Collections.singletonList(orderLineItem));

        Order result = orderService.changeOrderStatus(order.getId(), order);

        assertThat(result.getOrderLineItems()).containsExactlyElementsOf(Collections.singletonList(orderLineItem));
    }

    @DisplayName("주문이 등록되어 있지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatusException1() {
        given(orderDao.findById(order.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 계산 완료일 경우 변경할 수 없다.")
    @Test
    void changeOrderStatusException2() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
