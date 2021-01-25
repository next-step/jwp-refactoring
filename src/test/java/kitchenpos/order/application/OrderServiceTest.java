package kitchenpos.order.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends BaseServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    private List<OrderLineItemRequest> orderLineItemRequests;

//    @Test
//    void name() {
//        Long orderTableId = getSavedOrderTable().getId();
//        OrderLineItemRequest savedOrderLineItem = getSavedOrderLineItem();
//
//        OrderRequest orderCreateRequest = new OrderRequest(orderTableId, Collections.singletonList(savedOrderLineItem));
//
//        O savedOrder = orderService.create(orderCreateRequest);
//
//        assertThat(savedOrder.getId()).isNotNull();
//        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
//        assertThat(savedOrder.getOrderTableId()).isEqualTo(orderCreateRequest.getOrderTableId());
//        assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull();
//    }
//
//    private OrderTable getSavedOrderTable() {
//        return orderTableDao.save(new OrderTableRequest(null,  0, false).toOrderTable());
//    }
//
//    private OrderLineItemRequest getSavedOrderLineItem() {
//        MenuDto menu = menuService.create(getMenu());
//        return OrderLineItemHelper.createRequest(menu, 1);
//    }

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        ProductResponse productResponse = productService.create(등록된_product);
        ProductRequest p = new ProductRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice());
        List<MenuProductRequest> menuProducts = Collections.singletonList(new MenuProductRequest(productResponse.getId(), 1));
        MenuRequest menuRequest = new MenuRequest(등록되어_있지_않은_menu_id, "후라이드치킨", BigDecimal.valueOf(16000),
                등록된_menuGroup_id, menuProducts);

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        orderLineItemRequests = Collections.singletonList(new OrderLineItemRequest(menuResponse.getId(), 2));

        orderService.create(new OrderRequest(비어있지_않은_orderTable_id, orderLineItemRequests));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void createOrder() {
        OrderRequest orderRequest = new OrderRequest(비어있지_않은_orderTable_id, orderLineItemRequests);
        OrderResponse orderResponse = orderService.create(orderRequest);

        assertThat(orderResponse.getOrderTableId()).isEqualTo(비어있지_않은_orderTable_id);
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(orderResponse.getOrderedTime()).isNotNull();
        assertThat(orderResponse.getOrderLineItemResponses().size()).isEqualTo(orderLineItemRequests.size());
    }
//
//    @DisplayName("주문 항목이 하나도 없을 경우 등록할 수 없다.")
//    @Test
//    void createOrderException1() {
//        assertThatThrownBy(() -> orderService.create(Order.of(비어있지_않은_orderTable_id, null)))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("선택한 주문 항목의 메뉴가 등록되어 있지 않으면 등록할 수 없다.")
//    @Test
//    void createOrderException2() {
//        OrderLineItem orderLineItem = OrderLineItem.of(1L, 등록되어_있지_않은_menu_id, 2);
//        Order order = Order.of(비어있지_않은_orderTable_id, Collections.singletonList(orderLineItem));
//
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("해당 주문 테이블이 등록되어 있지 않으면 등록할 수 없다.")
//    @Test
//    void createOrderException3() {
//        Order order = Order.of(등록되어_있지_않은_orderTable_id, orderLineItems);
//
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("빈 테이블일 경우 등록할 수 없다.")
//    @Test
//    void createOrderException4() {
//        Order order = Order.of(빈_orderTable_id1, orderLineItems);
//
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("주문 상태를 변경할 수 있다.")
//    @Test
//    void changeOrderStatus() {
//        Order changeOrder = Order.of(비어있지_않은_orderTable_id, orderLineItems);
//        changeOrder.setOrderStatus(OrderStatus.MEAL.name());
//
//        Order result = orderService.changeOrderStatus(1L, changeOrder);
//
//        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
//    }
//
//    @DisplayName("주문이 등록되어 있지 않으면 변경할 수 없다.")
//    @Test
//    void changeOrderStatusException1() {
//        Order changeOrder = Order.of(비어있지_않은_orderTable_id, orderLineItems);
//        changeOrder.setOrderStatus(OrderStatus.COOKING.name());
//
//        assertThatThrownBy(() -> orderService.changeOrderStatus(2L, changeOrder))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("주문 상태가 계산 완료일 경우 변경할 수 없다.")
//    @Test
//    void changeOrderStatusException2() {
//        Order order = orderService.list().get(0);
//        order.setOrderStatus(OrderStatus.COMPLETION.name());
//        orderDao.save(order);
//
//        Order changeOrder = Order.of(비어있지_않은_orderTable_id, orderLineItems);
//        changeOrder.setOrderStatus(OrderStatus.MEAL.name());
//
//        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeOrder))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
}
