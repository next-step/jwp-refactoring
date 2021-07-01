package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    MenuDao menuDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("정상적으로 등록")
    @Test
    void create() {

        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(new OrderTable(1L, 1L, 4, false)));


        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1L);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 2L, 1L);

        Order order = new Order(1L, Arrays.asList(orderLineItem1, orderLineItem2));

        given(orderDao.save(order)).willReturn(new Order(1L, order.getOrderTableId(), OrderStatus.COOKING.name()));

        Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

    }

    @DisplayName("주문 항목이 없을 경우")
    @Test
    void 주문_항목이_없을_경우() {
        Order order = new Order(1L, Arrays.asList());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 수와 메뉴의 수가 맞지 않을 경우")
    @Test
    void 주문_항목_수와_메뉴의_수_불일치() {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1L);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 2L, 1L);

        Order order = new Order(1L, Arrays.asList(orderLineItem1, orderLineItem2));

        given(menuDao.countByIdIn(anyList())).willReturn(1L);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문에 존재하지 않는 주문테이블이 있는 경우")
    @Test
    void 주문에_존재하지_않는_주문테이블이_있는_경우() {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1L);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 2L, 1L);

        Order order = new Order(1L, Arrays.asList(orderLineItem1, orderLineItem2));

        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블에 사람이 없어서 비어있는 경우")
    @Test
    void 주문테이블이_비어있는_경우() {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1L);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 2L, 1L);

        Order order = new Order(1L, Arrays.asList(orderLineItem1, orderLineItem2));

        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(new OrderTable(1L, 1L, 0, true)));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }
}
