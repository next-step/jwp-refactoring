package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setup() {
        this.orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("1개 미만의 주문 항목으로 주문할 수 없다.")
    @Test
    void createOrderFailWithNotEnoughOrderLineItemsTest() {
        // given
        Order orderRequest = new Order();
        orderRequest.setOrderLineItems(new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 없는 주문 항목으로 주문할 수 없다.")
    @Test
    void createOrderFailWithNotExistMenuTest() {
        // given
        Order orderRequest = new Order();
        OrderLineItem orderLineItem = new OrderLineItem();
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        orderRequest.setOrderLineItems(orderLineItems);

        given(menuDao.countByIdIn(any())).willReturn(100L);

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 주문테이블에서 주문할 수 없다.")
    @Test
    void createOrderFailWithNotExistOrderTableTest() {
        // given
        Order orderRequest = new Order();
        OrderLineItem orderLineItem = new OrderLineItem();
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        orderRequest.setOrderLineItems(orderLineItems);

        given(menuDao.countByIdIn(any())).willReturn((long) orderLineItems.size());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }
}