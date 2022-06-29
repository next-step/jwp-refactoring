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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.ServiceTestFactory.주문생성;
import static kitchenpos.application.ServiceTestFactory.주문항목생성;
import static kitchenpos.application.ServiceTestFactory.테이블생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@DisplayName("주문 서비스 테스트")
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
    @InjectMocks
    private OrderService orderService;
    private OrderLineItem orderLineItemRequest;
    private Order orderRequest;
    private Order order;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        orderLineItemRequest = new OrderLineItem();
        orderRequest = 주문생성(1L, 1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItemRequest));
        order = 주문생성(1L, 1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItemRequest));
        orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(order.getId());
    }

    @Test
    void 주문_생성() {
        long menuIds = 1L;
        OrderTable ordertable = 테이블생성(1L, 1L, 5, false);
        given(menuDao.countByIdIn(any())).willReturn(menuIds);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(ordertable));
        given(orderDao.save(orderRequest)).willReturn(order);
        given(orderLineItemDao.save(orderLineItemRequest)).willReturn(orderLineItem);

        Order result = orderService.create(orderRequest);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void 주문_생성_실패_빈_주문항목() {
        Order 빈주문 = 주문생성(1L, 1L, OrderStatus.COOKING.name(), Arrays.asList());

        assertThatThrownBy(() -> orderService.create(빈주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_실패_존재하지않는_메뉴() {
        Order 존재하지않는메뉴_주문 = 주문생성(1L, 1L, OrderStatus.COOKING.name(), Arrays.asList(new OrderLineItem()));

        assertThatThrownBy(() -> orderService.create(존재하지않는메뉴_주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_실패_존재하지않는_주문항목() {
        given(menuDao.countByIdIn(any())).willReturn(99L);

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_실패_존재하지않는_주문테이블() {
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_실패_빈_주문테이블() {
        OrderTable 빈_주문테이블 = 테이블생성(1L, 1L, 5, true);
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(빈_주문테이블));

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_조회() {
        Order 주문1 = 주문생성(1L, 1L, OrderStatus.MEAL.name(), null);
        List<Order> orders = Arrays.asList(주문1);
        OrderLineItem orderLineItem1 = 주문항목생성(1L, 주문1.getId(), null, 1);
        given(orderDao.findAll()).willReturn(orders);
        given(orderLineItemDao.findAllByOrderId(주문1.getId())).willReturn(Arrays.asList(orderLineItem1));

        List<Order> result = orderService.list();

        assertThat(result).hasSize(1);
    }

    @Test
    void 주문상태변경() {
        OrderLineItem orderLineItem = 주문항목생성(1L, order.getId(), null, 1);
        given(orderDao.findById(any())).willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(Arrays.asList(orderLineItem));
        orderRequest = 주문생성(null, null, OrderStatus.COMPLETION.name(), null);
        Order result = orderService.changeOrderStatus(1L, orderRequest);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    void 주문상태변경_실패_존재하지않는_주문() {
        given(orderDao.findById(any())).willReturn(Optional.empty());
        orderRequest = 주문생성(null, null, OrderStatus.COMPLETION.name(), null);

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태변경_실패_완료된주문() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderRequest = 주문생성(null, null, OrderStatus.COMPLETION.name(), null);
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }
}
