package kitchenpos.order.application;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationServiceTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

public class OrderServiceTest extends IntegrationServiceTest {
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

    private static MenuResponse savedMenu;
    private static OrderTableResponse savedTable;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        final ProductRequest product = new ProductRequest("후라이드", new BigDecimal(16000));
        final ProductResponse savedProduct = productService.create(product);

        // given
        final MenuGroupRequest menuGroup = new MenuGroupRequest("한마리메뉴");
        final MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroup);

        // given
        final List<MenuProductRequest> menuProductRequests =
            singletonList(new MenuProductRequest(savedProduct.getId(), 1));
        final MenuRequest menu =
            new MenuRequest("후라이드치킨", new BigDecimal(16000), savedMenuGroup.getId(), menuProductRequests);
        savedMenu = menuService.create(menu);

        final OrderTableRequest orderTable = new OrderTableRequest(1, false);
        savedTable = tableService.create(orderTable);
    }

    @Test
    void create() {
        // given
        final OrderRequest order = makeCookingOrderRequest(savedTable.getId(), savedMenu.getId(), 1);

        // when
        final OrderResponse savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderTableId()).isEqualTo(savedTable.getId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(savedOrder.getOrderLineItems()).isNotEmpty();
        assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull();
        assertThat(savedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(savedMenu.getId());
        assertThat(savedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(1);
    }

    @DisplayName("주문 항목이 비어있을 때 예외 발생")
    @Test
    void createByEmptyOrderLineItems() {
        // given
        final OrderRequest order = new OrderRequest(savedTable.getId(), OrderStatus.COOKING, emptyList());

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("빈 테이블에서 주문 시 예외 발생")
    @Test
    void createByEmptyTable() {
        // given
        final OrderTableRequest request = new OrderTableRequest(1, true);
        final OrderTableResponse emptyTable = tableService.create(request);
        final OrderRequest order = makeCookingOrderRequest(emptyTable.getId(), savedMenu.getId(), 1);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @Test
    void list() {
        // given
        final OrderRequest order = makeCookingOrderRequest(savedTable.getId(), savedMenu.getId(), 1);
        orderService.create(order);

        // when
        final List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getId()).isNotNull();
        assertThat(orders.get(0).getOrderTableId()).isEqualTo(savedTable.getId());
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(orders.get(0).getOrderLineItems()).isNotEmpty();
        assertThat(orders.get(0).getOrderLineItems().get(0).getSeq()).isNotNull();
        assertThat(orders.get(0).getOrderLineItems().get(0).getMenuId()).isEqualTo(savedMenu.getId());
        assertThat(orders.get(0).getOrderLineItems().get(0).getQuantity()).isEqualTo(1);
    }

    @Test
    void changeOrderStatus() {
        // given
        final OrderRequest order = makeCookingOrderRequest(savedTable.getId(), savedMenu.getId(), 1);
        final OrderResponse savedOrder = orderService.create(order);
        final OrderRequest updateRequest = makeOrderRequest(savedTable.getId(), OrderStatus.MEAL, savedMenu.getId(), 1);

        // when
        final OrderResponse changedOrder = orderService.changeOrderStatus(savedOrder.getId(), updateRequest);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("계산 완료된 주문에 대해 주문 상태를 변경하려고 하면 예외 발생")
    @Test
    void changeCompletedOrderStatus() {
        // given
        final OrderRequest order = makeOrderRequest(savedTable.getId(), OrderStatus.COMPLETION, savedMenu.getId(), 1);
        final OrderResponse savedOrder = orderService.create(order);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final OrderRequest request =
                makeOrderRequest(savedTable.getId(), OrderStatus.MEAL, savedMenu.getId(), 1);
            orderService.changeOrderStatus(savedOrder.getId(), request);
        });
    }

    public static OrderRequest makeCookingOrderRequest(final Long tableId, final Long menuId, final int quantity) {
        return makeOrderRequest(tableId, null, menuId, quantity);
    }

    public static OrderRequest makeOrderRequest(
        final Long tableId, final OrderStatus orderStatus, final Long menuId, final int quantity
    ) {
        return new OrderRequest(tableId, orderStatus, singletonList(new OrderLineItemRequest(menuId, quantity)));
    }
}
