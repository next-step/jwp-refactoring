package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import kitchenpos.domain.Order;

@DisplayName("주문 관련 테스트")
class OrderServiceTest {
    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private OrderTableDao orderTableDao;

    private OrderService orderService;

    private MenuGroup menuGroup;
    private Product product;
    private Menu menu;
    private List<MenuProduct> menuProducts;

    private TableGroup tableGroup;
    private OrderTable orderTable;
    private List<OrderTable> orderTables;
    private Order order;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        menuDao = mock(MenuDao.class);
        orderDao = mock(OrderDao.class);
        orderLineItemDao = mock(OrderLineItemDao.class);
        orderTableDao = mock(OrderTableDao.class);
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        menuGroup = MenuGroup.of(2L, "한마리메뉴");
        product = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(16000L));
        menu = Menu.of(1L, product.getName(), product.getPrice(), menuGroup.getId(), null);
        menuProducts = Lists.newArrayList(MenuProduct.of(1L, menu.getId(), product.getId(), 1L));

        tableGroup = TableGroup.of(1L, LocalDateTime.now(), null);
        orderTable = OrderTable.of(1L, tableGroup.getId(), 4, false);
        orderTables = Lists.newArrayList(orderTable);
        tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTables);

        order = Order.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        orderLineItems = Lists.newArrayList(OrderLineItem.of(1L, order.getId(), menu.getId(), 1L));
        order.setOrderLineItems(orderLineItems);
    }

    @DisplayName("주문 생성하기")
    @Test
    void createTest() {
        when(menuDao.countByIdIn(Lists.newArrayList(menu.getId()))).thenReturn(1L);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));
        when(orderDao.save(order)).thenReturn(order);

        assertThat(orderService.create(order)).isEqualTo(order);
    }

    @DisplayName("주문 생성시 주문 항목 없으면 예외 던지기")
    @Test
    void exceptionTest1() {
        order.setOrderLineItems(new ArrayList<>());
        assertThatThrownBy(
            () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 개수와 해당하는 메뉴의 개수가 다르면 예외 발생")
    @Test
    void exceptionTest2() {
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());

        when(menuDao.countByIdIn(menuIds)).thenReturn(orderLineItems.size() + 1L);

        assertThatThrownBy(
            () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문에 등록된 주문 테이블이 없다면 예외 발생")
    @Test
    void exceptionTest3() {
        Long wrongOrderTableId = 100L;
        order.setOrderTableId(wrongOrderTableId);

        assertThatThrownBy(
            () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }



    @DisplayName("주문 목록 조회시 저장된 주문 목록 얻기")
    @Test
    void listTest() {
        when(orderDao.findAll()).thenReturn(Lists.newArrayList(order));
        assertThat(orderService.list()).isEqualTo(Lists.newArrayList(order));
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatusTest() {
        when(orderDao.findById(order.getId())).thenReturn(Optional.of(order));

        Order targetOrder = Order.of(2L, order.getOrderTableId(), OrderStatus.MEAL.name(), LocalDateTime.now(), order.getOrderLineItems());
        assertThat(orderService.changeOrderStatus(order.getId(), targetOrder).getOrderStatus()).isEqualTo(
            targetOrder.getOrderStatus()
        );
    }

    @DisplayName("주문 상태 변경시, 계산 완료이면 예외 발생")
    @Test
    void exceptionTest4() {
        Order targetOrder = Order.of(2L, order.getOrderTableId(), OrderStatus.MEAL.name(), LocalDateTime.now(), order.getOrderLineItems());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(order.getId(), targetOrder)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
