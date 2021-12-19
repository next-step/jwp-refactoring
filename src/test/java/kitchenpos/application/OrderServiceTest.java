package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

class OrderServiceTest extends IntegrationServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private TableService tableService;

    private static Menu savedMenu;
    private static OrderTable savedTable;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        final Product product = ProductServiceTest.makeProduct("후라이드", new BigDecimal(16000));
        final Product savedProduct = productService.create(product);

        // given
        final MenuGroup menuGroup = MenuGroupServiceTest.makeMenuGroup("한마리메뉴");
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // given
        final Menu menu =
            MenuServiceTest.makeMenu("후라이드치킨", new BigDecimal(16000), savedMenuGroup.getId(), savedProduct.getId(), 1);
        savedMenu = menuService.create(menu);

        final OrderTable orderTable = TableServiceTest.makeOrderTable(1, false);
        savedTable = tableService.create(orderTable);
    }

    @Test
    void create() {
        // given
        final Order order = makeOrder(savedTable.getId(), savedMenu.getId(), 1);

        // when
        final Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderTableId()).isEqualTo(savedTable.getId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderLineItems()).isNotEmpty();
        assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull();
        assertThat(savedOrder.getOrderLineItems().get(0).getOrderId()).isEqualTo(savedOrder.getId());
        assertThat(savedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(savedMenu.getId());
        assertThat(savedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(1);
    }

    @DisplayName("주문 항목이 비어있을 때 예외 발생")
    @Test
    void createByEmptyOrderLineItems() {
        // given
        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.emptyList());

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("빈 테이블에서 주문 시 예외 발생")
    @Test
    void createByEmptyTable() {
        // given
        final OrderTable emptyTable = TableServiceTest.makeOrderTable(1, true);
        final Order order = makeOrder(emptyTable.getId(), savedMenu.getId(), 1);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @Test
    void list() {
        // given
        final Order order = makeOrder(savedTable.getId(), savedMenu.getId(), 1);
        final Order savedOrder = orderService.create(order);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getId()).isNotNull();
        assertThat(orders.get(0).getOrderTableId()).isEqualTo(savedTable.getId());
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(orders.get(0).getOrderLineItems()).isNotEmpty();
        assertThat(orders.get(0).getOrderLineItems().get(0).getSeq()).isNotNull();
        assertThat(orders.get(0).getOrderLineItems().get(0).getOrderId()).isEqualTo(savedOrder.getId());
        assertThat(orders.get(0).getOrderLineItems().get(0).getMenuId()).isEqualTo(savedMenu.getId());
        assertThat(orders.get(0).getOrderLineItems().get(0).getQuantity()).isEqualTo(1);
    }

    @Test
    void changeOrderStatus() {
        // given
        final Order order = makeOrder(savedTable.getId(), savedMenu.getId(), 1);
        final Order savedOrder = orderService.create(order);

        // when
        savedOrder.setOrderStatus(OrderStatus.MEAL.name());
        final Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("계산 완료된 주문에 대해 주문 상태를 변경하려고 하면 예외 발생")
    @Test
    void changeCompletedOrderStatus() {
        // given
        final Order order = makeOrder(savedTable.getId(), savedMenu.getId(), 1);
        final Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            savedOrder.setOrderStatus(OrderStatus.MEAL.name());
            orderService.changeOrderStatus(savedOrder.getId(), savedOrder);
        });
    }

    public static Order makeOrder(final Long tableId, final Long menuId, final int quantity) {
        final Order order = new Order();
        order.setOrderTableId(tableId);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        return order;
    }
}
