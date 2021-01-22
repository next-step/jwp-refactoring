package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.*;
import kitchenpos.exception.AlreadyCompleteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

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

        List<OrderMenuRequest> orderMenus = Collections.singletonList(OrderServiceTest.createOrderMenu(menu.getId(), 1L));
        OrderResponse order = orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderMenus));

        assertThat(order.getId()).isNotNull();
    }

    @DisplayName("메뉴가 등록되지 않은 주문 생성")
    @Test
    void createWithoutMenu() {
        TableResponse savedTable = tableService.create();

        List<OrderMenuRequest> orderMenus = Collections.singletonList(OrderServiceTest.createOrderMenu(1L, 1L));

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderMenus)));
    }

    @DisplayName("테이블이 등록되지 않은 주문 생성")
    @Test
    void createWithoutTable() {
        List<OrderMenuRequest> orderMenus = Collections.singletonList(OrderServiceTest.createOrderMenu(1L, 1L));

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderService.create(OrderServiceTest.createOrder(1L, orderMenus)));
    }

    @DisplayName("주문 조회")
    @Test
    void find() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProductRequest> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        MenuResponse menu = menuService.create(MenuServiceTest.createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        TableResponse savedTable = tableService.create();

        List<OrderMenuRequest> orderMenus = Collections.singletonList(OrderServiceTest.createOrderMenu(menu.getId(), 1L));
        OrderResponse order = orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderMenus));
        List<OrderResponse> orders = orderService.list();

        assertThat(orders.size()).isEqualTo(1);
        List<Long> orderIds = orders.stream()
                .map(OrderResponse::getId)
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

        List<OrderMenuRequest> orderMenus = Collections.singletonList(OrderServiceTest.createOrderMenu(menu.getId(), 1L));
        OrderResponse order = orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderMenus));
        OrderResponse updatedOrder = orderService.changeOrderStatus(order.getId(), OrderStatus.MEAL);

        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("등록되지 않은 주문 상태 변경")
    @Test
    void changeStatusWithoutOrder() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.MEAL));
    }

    @DisplayName("이미 완료 된 주문 상태 변경")
    @Test
    void changeStatusWithCompletion() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProductRequest> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        MenuResponse menu = menuService.create(MenuServiceTest.createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        TableResponse savedTable = tableService.create();

        List<OrderMenuRequest> orderMenus = Collections.singletonList(OrderServiceTest.createOrderMenu(menu.getId(), 1L));
        OrderResponse order = orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderMenus));
        orderService.changeOrderStatus(order.getId(), OrderStatus.COMPLETION);

        assertThatExceptionOfType(AlreadyCompleteException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), OrderStatus.MEAL));
    }

    public static OrderRequest createOrder(Long orderTableId, List<OrderMenuRequest> orderMenus) {
        return new OrderRequest(orderTableId, orderMenus);
    }

    public static OrderMenuRequest createOrderMenu(Long menuId, Long quantity) {
        return new OrderMenuRequest(menuId, quantity);
    }
}
