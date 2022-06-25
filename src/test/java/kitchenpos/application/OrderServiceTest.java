package kitchenpos.application;

import static kitchenpos.fixture.DomainFactory.createMenu;
import static kitchenpos.fixture.DomainFactory.createMenuGroup;
import static kitchenpos.fixture.DomainFactory.createMenuProduct;
import static kitchenpos.fixture.DomainFactory.createOrder;
import static kitchenpos.fixture.DomainFactory.createOrderLineItem;
import static kitchenpos.fixture.DomainFactory.createOrderTable;
import static kitchenpos.fixture.DomainFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private Order 주문;
    private OrderTable 주문테이블;
    private Menu 빅맥버거;

    @BeforeEach
    void setUp() {
        MenuGroup 빅맥세트 = createMenuGroup(1L, "빅맥세트");
        Product 토마토 = createProduct(2L, "토마토", 1000);
        Product 양상추 = createProduct(3L, "양상추", 500);

        빅맥버거 = createMenu(1L, "빅맥버거", 3000, 1L,
                Arrays.asList(createMenuProduct(1L, 1L, 토마토.getId(), 1), createMenuProduct(2L, 1L, 양상추.getId(), 4)));
        주문테이블 = createOrderTable(1L, null, 5, false);

        주문 = createOrder(주문테이블.getId(), null, null, Arrays.asList(createOrderLineItem(1L, 1L, 빅맥버거.getId(), 1)));

    }

    @Test
    void 주문_생성_주문_항목_없음_예외() {
        Order 주문항목없는주문 = createOrder(주문테이블.getId(), null, null, null);

        assertThatThrownBy(
                () -> orderService.create(주문항목없는주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_없는_메뉴_예외() {
        when(menuDao.countByIdIn(Arrays.asList(100L))).thenReturn(0L);
        Order 없는메뉴주문 = createOrder(주문테이블.getId(), null, null, Arrays.asList(createOrderLineItem(1L, 1L, 100L, 1)));

        assertThatThrownBy(
                () -> orderService.create(없는메뉴주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_존재하지_않는_주문_테이블_예외() {
        when(menuDao.countByIdIn(Arrays.asList(빅맥버거.getId()))).thenReturn(1L);
        when(orderTableDao.findById(100L)).thenThrow(IllegalArgumentException.class);
        Order 없는_테이블_주문 = createOrder(100L, null, null, Arrays.asList(createOrderLineItem(1L, 1L, 빅맥버거.getId(), 1)));
        assertThatThrownBy(
                () -> orderService.create(없는_테이블_주문)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 주문_생성_빈_테이블_예외() {
        when(menuDao.countByIdIn(Arrays.asList(빅맥버거.getId()))).thenReturn(1L);
        주문테이블.setEmpty(true);
        when(orderTableDao.findById(주문테이블.getId())).thenReturn(Optional.ofNullable(주문테이블));

        assertThatThrownBy(
                () -> orderService.create(주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성() {
        when(menuDao.countByIdIn(Arrays.asList(빅맥버거.getId()))).thenReturn(1L);
        when(orderTableDao.findById(주문테이블.getId())).thenReturn(Optional.ofNullable(주문테이블));
        when(orderDao.save(주문)).thenReturn(주문);

        assertThat(orderService.create(주문)).isEqualTo(주문);
    }

    @Test
    void 주문_목록_조회() {
        when(orderDao.findAll()).thenReturn(Arrays.asList(주문));

        assertThat(toIdList(orderService.list())).containsExactlyElementsOf(toIdList(Arrays.asList(주문)));
    }

    @Test
    void 주문_상태_변경_존재하지_않는_주문_예외() {
        when(orderDao.findById(주문.getId())).thenThrow(IllegalArgumentException.class);

        주문.setOrderStatus(OrderStatus.MEAL.name());
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(주문.getId(), 주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태_변경_계산_완료_상태_예외() {
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(주문.getId())).thenReturn(Optional.ofNullable(주문));

        Order ChangedOrder = createOrder(주문테이블.getId(), OrderStatus.COOKING.name(), null, null);
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(주문.getId(), ChangedOrder)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태_변경() {
        when(orderDao.findById(주문.getId())).thenReturn(Optional.ofNullable(주문));
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문.getOrderLineItems());

        Order ChangedOrder = createOrder(주문테이블.getId(), OrderStatus.COMPLETION.name(), null, null);
        assertThat(orderService.changeOrderStatus(주문.getId(), ChangedOrder)).isEqualTo(주문);

    }

    private List<Long> toIdList(List<Order> orders) {
        return orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
    }
}