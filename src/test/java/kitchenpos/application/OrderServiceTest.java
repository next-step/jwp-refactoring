package kitchenpos.application;

import static kitchenpos.domain.MenuFixture.createMenu;
import static kitchenpos.domain.MenuProductFixture.createMenuProduct;
import static kitchenpos.domain.OrderFixture.createOrder;
import static kitchenpos.domain.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.domain.OrderTableFixture.createTable;
import static kitchenpos.domain.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuProduct 후라이드치킨상품;
    private MenuProduct 양념치킨상품;
    private Menu 두마리치킨;
    private OrderTable 주문테이블_A;
    private OrderLineItem 주문항목_A;
    private Order 주문_A;
    private List<Long> menuIds;

    @BeforeEach
    void setUp() {
        후라이드치킨 = createProduct(1L, "후라이드치킨", BigDecimal.valueOf(10000));
        양념치킨 = createProduct(2L, "양념치킨", BigDecimal.valueOf(5000));

        후라이드치킨상품 = createMenuProduct(1L, 1L, 1L, 10);
        양념치킨상품 = createMenuProduct(2L, 1L, 2L, 10);

        두마리치킨 = createMenu(1L, "두마리치킨", new BigDecimal(13000), 1L, Arrays.asList(후라이드치킨상품, 양념치킨상품));

        주문테이블_A = createTable(1L, null, 0, false);

        주문항목_A = createOrderLineItem(1L, null, 두마리치킨.getId(), 1);

        주문_A = createOrder(1L, 주문테이블_A.getId(), null, null, Collections.singletonList(주문항목_A));

        menuIds = 주문_A.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(주문_A.getOrderTableId())).willReturn(Optional.of(주문테이블_A));
        given(orderDao.save(주문_A)).willReturn(주문_A);
        given(orderLineItemDao.save(주문항목_A)).willReturn(주문항목_A);

        Order savedOrder = orderService.create(주문_A);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderLineItems()).contains(주문항목_A),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("주문 항목이 비어있는 경우 주문을 등록할 수 없다.")
    @Test
    void createWithEmptyOrderLineItem() {
        Order order = createOrder(1L, 주문테이블_A.getId(), null, null, null);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 항목이 존재 할 경우 주문을 등록할 수 없다.")
    @Test
    void createWithNotExistOrderLineItems() {
        given(menuDao.countByIdIn(menuIds)).willReturn(0L);

        assertThatThrownBy(() -> orderService.create(주문_A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않은 경우 주문을 등록할 수 없다.")
    @Test
    void createWithNotExistOrderTable() {
        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(주문_A.getOrderTableId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(주문_A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있는 경우 주문을 등록할 수 없다.")
    @Test
    void createWithNotEmptyOrderTable() {
        주문테이블_A.setEmpty(true);
        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(주문_A.getOrderTableId())).willReturn(Optional.of(주문테이블_A));

        assertThatThrownBy(() -> orderService.create(주문_A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        given(orderDao.findAll()).willReturn(Collections.singletonList(주문_A));
        given(orderLineItemDao.findAllByOrderId(주문_A.getId())).willReturn(Arrays.asList(주문항목_A));

        List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
        assertThat(orders).contains(주문_A);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        String expectedOrderStatus = OrderStatus.MEAL.name();
        Order expectedOrder = createOrder(주문_A.getId(), 주문테이블_A.getId(), expectedOrderStatus, 주문_A.getOrderedTime(), 주문_A.getOrderLineItems());
        given(orderDao.findById(주문_A.getId())).willReturn(Optional.of(주문_A));

        orderService.changeOrderStatus(주문_A.getId(), expectedOrder);

        assertThat(주문_A.getOrderStatus()).isEqualTo(expectedOrderStatus);
    }

    @DisplayName("등록되지 않은 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusNotExistOrder() {
        given(orderDao.findById(주문_A.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문_A.getId(), new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 완료된 주문은 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusAlreadyCompletion() {
        주문_A.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(주문_A.getId())).willReturn(Optional.of(주문_A));

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문_A.getId(), new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
