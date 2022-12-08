package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    private Product chip;
    private Product display;
    private MenuProduct chipProduct;
    private MenuProduct displayProduct;
    private Menu macbook;
    private OrderTable orderTable;
    private OrderLineItem macbookLineItem;
    private Order order;
    private List<Long> menuIds;

    @BeforeEach
    void setUp() {
        chip = new Product(1L, "M1", BigDecimal.valueOf(10000));
        display = new Product(2L, "레티나", BigDecimal.valueOf(5000));
        chipProduct = new MenuProduct(1L, 1L, 1L, 10);
        displayProduct = new MenuProduct(2L, 1L, 2L, 10);
        macbook = new Menu(1L, "맥북", new BigDecimal(13000), 1L, Arrays.asList(chipProduct, displayProduct));

        orderTable = new OrderTable(1L, null, 0, false);
        order = new Order(1L, orderTable.getId(), null, null, new ArrayList<>());
        macbookLineItem = new OrderLineItem(1L, null, macbook.getId(), 1);
        order.setOrderLineItems(Collections.singletonList(macbookLineItem));

        menuIds = order.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(macbookLineItem)).willReturn(macbookLineItem);

        Order savedOrder = orderService.create(order);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderLineItems()).contains(macbookLineItem),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("주문 항목이 비어있는 경우 주문을 등록할 수 없다.")
    @Test
    void createWithEmptyOrderLineItem() {
        Order order = new Order(1L, orderTable.getId(), null, null, null);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 항목이 존재 할 경우 주문을 등록할 수 없다.")
    @Test
    void createWithNotExistOrderLineItems() {
        given(menuDao.countByIdIn(menuIds)).willReturn(0L);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않은 경우 주문을 등록할 수 없다.")
    @Test
    void createWithNotExistOrderTable() {
        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있는 경우 주문을 등록할 수 없다.")
    @Test
    void createWithNotEmptyOrderTable() {
        orderTable.setEmpty(true);
        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(Arrays.asList(macbookLineItem));

        List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
        assertThat(orders).contains(order);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        String expectedOrderStatus = OrderStatus.MEAL.name();
        Order expectedOrder = new Order(order.getId(), orderTable.getId(), expectedOrderStatus, order.getOrderedTime(), order.getOrderLineItems());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        orderService.changeOrderStatus(order.getId(), expectedOrder);

        assertThat(order.getOrderStatus()).isEqualTo(expectedOrderStatus);
    }

    @DisplayName("등록되지 않은 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusNotExistOrder() {
        given(orderDao.findById(order.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 완료된 주문은 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusAlreadyCompletion() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
