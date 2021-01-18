package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("주문 서비스")
public class OrderServiceTest extends ServiceTestBase {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuService menuService;
    private final TableService tableService;
    private final OrderService orderService;

    @Autowired
    public OrderServiceTest(MenuGroupService menuGroupService, ProductService productService, MenuService menuService, TableService tableService, OrderService orderService) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuService = menuService;
        this.tableService = tableService;
        this.orderService = orderService;
    }

    @DisplayName("주문 생성")
    @Test
    void create() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProductRequest> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        MenuResponse menu = menuService.create(MenuServiceTest.createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        TableResponse savedTable = tableService.create();

        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderServiceTest.createOrderLineItem(menu.getId(), 1L));
        Order order = orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderLineItems));

        assertThat(order.getId()).isNotNull();
    }

    @DisplayName("메뉴가 비어있는 주문 생성")
    @Test
    void createWithEmptyMenu() {
        TableResponse savedTable = tableService.create();

        List<OrderLineItem> orderLineItems = Collections.emptyList();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderLineItems)));
    }

    @DisplayName("메뉴가 등록되지 않은 주문 생성")
    @Test
    void createWithoutMenu() {
        TableResponse savedTable = tableService.create();

        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderServiceTest.createOrderLineItem(1L, 1L));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderLineItems)));
    }

    @DisplayName("테이블이 등록되지 않은 주문 생성")
    @Test
    void createWithoutTable() {
        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderServiceTest.createOrderLineItem(1L, 1L));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(OrderServiceTest.createOrder(1L, orderLineItems)));
    }

    @DisplayName("주문 조회")
    @Test
    void find() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProductRequest> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        MenuResponse menu = menuService.create(MenuServiceTest.createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        TableResponse savedTable = tableService.create();

        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderServiceTest.createOrderLineItem(menu.getId(), 1L));
        Order order = orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderLineItems));
        List<Order> orders = orderService.list();

        assertThat(orders.size()).isEqualTo(1);
        List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        assertThat(orderIds).contains(order.getId());
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeStatus() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProductRequest> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        MenuResponse menu = menuService.create(MenuServiceTest.createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        TableResponse savedTable = tableService.create();

        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderServiceTest.createOrderLineItem(menu.getId(), 1L));
        Order order = orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderLineItems));
        order.setOrderStatus(OrderStatus.MEAL.name());
        Order updatedOrder = orderService.changeOrderStatus(order.getId(), order);

        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("등록되지 않은 주문 상태 변경")
    @Test
    void changeStatusWithoutOrder() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProductRequest> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        MenuResponse menu = menuService.create(MenuServiceTest.createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        TableResponse savedTable = tableService.create();

        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderServiceTest.createOrderLineItem(menu.getId(), 1L));
        Order order = OrderServiceTest.createOrder(savedTable.getId(), orderLineItems);
        order.setOrderStatus(OrderStatus.MEAL.name());

        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(order.getId(), order));
    }

    @DisplayName("이미 완료 된 주문 상태 변경")
    @Test
    void changeStatusWithCompletion() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProductRequest> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        MenuResponse menu = menuService.create(MenuServiceTest.createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        TableResponse savedTable = tableService.create();

        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderServiceTest.createOrderLineItem(menu.getId(), 1L));
        Order order = orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderLineItems));
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(order.getId(), order);

        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(order.getId(), order));
    }

    public static Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public static OrderLineItem createOrderLineItem(Long menuId, Long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }
}
