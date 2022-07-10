package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주문 항목이 존재하지 않는 경우 오류가 발생한다")
    @Test
    void createWithNullOrderLineItem() {
        Order order = new Order();
        order.setOrderLineItems(null);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 수와 메뉴의 수가 같지 않으면 오류가 발생한다")
    @Test
    void createWithDifferentCount() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setQuantity(1);

        Order order = new Order();
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        given(menuDao.countByIdIn(anyList())).willReturn(2L);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 오류가 발생한다")
    @Test
    void createWithUnavailableOrderTable() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setQuantity(1);

        Order order = new Order();
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderTableId(1L);

        given(menuDao.countByIdIn(anyList())).willReturn(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 COMPLETION 인 주문의 상태를 변경할 경우 오류가 발생한다")
    @Test
    void changeOrderStatusOfCompletionOrder() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
