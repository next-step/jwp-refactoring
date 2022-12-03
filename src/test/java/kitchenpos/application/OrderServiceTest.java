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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("OrderService 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final Long MAIN_MENU_ID = 1L;
    private static final Long SUB_MENU_ID = 2L;

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

    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 주문_메뉴1;
    private OrderLineItem 주문_메뉴2;
    private List<OrderLineItem> 주문_메뉴_목록;

    @BeforeEach
    void setUp() {
        주문테이블 = new OrderTable(1L, null, 2, false);
        주문 = new Order(1L, 주문테이블.getId(), null);
        주문_메뉴1 = new OrderLineItem(1L, 주문.getId(), MAIN_MENU_ID, 1);
        주문_메뉴2 = new OrderLineItem(2L, 주문.getId(), SUB_MENU_ID, 1);

        주문_메뉴_목록 = Arrays.asList(주문_메뉴1, 주문_메뉴2);
        주문.setOrderLineItems(주문_메뉴_목록);
    }

    @Test
    void 주문_메뉴가_하나도_없으면_주문할_수_없음() {
        주문.setOrderLineItems(null);

        assertThatThrownBy(() -> {
            orderService.create(주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_메뉴가_등록된_메뉴가_아니면_주문할_수_없음() {
        given(menuDao.countByIdIn(anyList())).willReturn(1L);

        assertThatThrownBy(() -> {
            orderService.create(주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_등록된_주문_테이블이_아니면_주문할_수_없음() {
        given(menuDao.countByIdIn(anyList())).willReturn((long) 주문_메뉴_목록.size());
        given(orderTableDao.findById(주문.getOrderTableId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderService.create(주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블이면_주문할_수_없음() {
        주문테이블.setEmpty(true);
        주문테이블.setNumberOfGuests(0);

        given(menuDao.countByIdIn(anyList())).willReturn((long) 주문_메뉴_목록.size());
        given(orderTableDao.findById(주문.getOrderTableId())).willReturn(Optional.of(주문테이블));

        assertThatThrownBy(() -> {
            orderService.create(주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성() {
        given(menuDao.countByIdIn(anyList())).willReturn((long) 주문_메뉴_목록.size());
        given(orderTableDao.findById(주문.getOrderTableId())).willReturn(Optional.of(주문테이블));
        given(orderDao.save(주문)).willReturn(주문);
        given(orderLineItemDao.save(주문_메뉴1)).willReturn(주문_메뉴1);
        given(orderLineItemDao.save(주문_메뉴2)).willReturn(주문_메뉴2);

        orderService.create(주문);
    }

    @Test
    void 주문_목록_조회() {
        given(orderDao.findAll()).willReturn(Arrays.asList(주문));
        given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(Arrays.asList(주문_메뉴1, 주문_메뉴2));

        List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders).containsExactly(주문)
        );
    }

    @Test
    void 등록된_주문이_아니면_주문_상태를_변경할_수_없음() {
        Order 상태_변경_주문 = new Order();
        상태_변경_주문.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(주문.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), 상태_변경_주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문이_완료이면_주문_상태를_변경할_수_없음() {
        주문.setOrderStatus(OrderStatus.COMPLETION.name());

        Order 상태_변경_주문 = new Order();
        상태_변경_주문.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), 상태_변경_주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태_변경() {
        주문.setOrderStatus(OrderStatus.COOKING.name());

        Order 상태_변경_주문 = new Order();
        상태_변경_주문.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));
        given(orderDao.save(주문)).willReturn(주문);
        given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(주문_메뉴_목록);

        orderService.changeOrderStatus(주문.getId(), 상태_변경_주문);

        assertEquals(OrderStatus.MEAL.name(), 주문.getOrderStatus());
    }
}
