package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.TableServiceTest.일번_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    public static final Order 주문 = new Order();
    public static final OrderLineItem 불고기버거 = new OrderLineItem();

    static {
        불고기버거.setMenuId(불고기버거.getMenuId());
        불고기버거.setQuantity(1L);

        주문.setId(1L);
        주문.setOrderStatus(OrderStatus.COOKING.name());
        주문.setOrderTableId(일번_테이블.getId());
        주문.setOrderLineItems(Arrays.asList(불고기버거));
    }

    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    OrderService orderService;

    @AfterEach
    void afterEach() {
        일번_테이블.setEmpty(true);
        주문.setOrderStatus(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 추가")
    void create() {
        // given
        given(menuDao.countByIdIn(any()))
                .willReturn(1L);
        일번_테이블.setEmpty(false);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(일번_테이블));
        given(orderDao.save(any()))
                .willReturn(주문);
        given(orderLineItemDao.save(any()))
                .willReturn(불고기버거);
        // when
        final Order order = orderService.create(주문);
        // then
        assertThat(order).isInstanceOf(Order.class);
    }

    @Test
    @DisplayName("메뉴 조회")
    void list() {
        // given
        given(orderDao.findAll())
                .willReturn(Arrays.asList(주문));
        given(orderLineItemDao.findAllByOrderId(주문.getId()))
                .willReturn(Arrays.asList(불고기버거));
        // when
        final List<Order> list = orderService.list();
        // then
        assertThat(list).hasSize(1);
    }

    @Test
    @DisplayName("메뉴 주문 상태 변경")
    void changeOrderStatus() {
        // given
        given(orderDao.findById(any()))
                .willReturn(Optional.of(주문));
        // when
        주문.setOrderStatus(OrderStatus.MEAL.name());
        final Order order = orderService.changeOrderStatus(주문.getId(), 주문);
        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
