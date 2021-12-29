package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        // given
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = new ArrayList<>();
        orderLineItemCreateRequests.add(new OrderLineItemCreateRequest(1L, 1));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, orderLineItemCreateRequests);

        OrderTable orderTable = OrderTable.of(4, false);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(Product.of("후라이드", new BigDecimal(16_000)), 1));
        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), MenuGroup.of("두마리메뉴"), menuProducts);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        Order order = Order.of(orderLineItems, orderTable);

        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));
        given(menuDao.findById(any())).willReturn(Optional.ofNullable(menu));
        given(orderDao.save(any())).willReturn(order);

        // when
        OrderResponse result = orderService.create(orderCreateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderTable()).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(result.getOrderedTime()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블이면 등록에 실패한다.")
    void create_not_exist_order_table() {
        // given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문 테이블입니다.");
    }

    @Test
    @DisplayName("주문 항목의 메뉴들이 존재하지 않는 메뉴가 포함되면 등록에 실패한다.")
    void create_not_equals_order_line_items_count() {
        // given
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = new ArrayList<>();
        orderLineItemCreateRequests.add(new OrderLineItemCreateRequest(1L, 1));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, orderLineItemCreateRequests);

        OrderTable orderTable = OrderTable.of(4, false);

        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));
        given(menuDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 메뉴가 포함되어 있습니다.");
    }

    @Test
    @DisplayName("주문 항목이 비어있으면 등록에 실패한다.")
    void create_empty_order_line_items() {
        // given
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = new ArrayList<>();

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, orderLineItemCreateRequests);

        OrderTable orderTable = OrderTable.of(4, false);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(Product.of("후라이드", new BigDecimal(16_000)), 1));

        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이면 등록에 실패한다.")
    void create_order_table_is_empty() {
        // given
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = new ArrayList<>();
        orderLineItemCreateRequests.add(new OrderLineItemCreateRequest(1L, 1));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, orderLineItemCreateRequests);

        OrderTable orderTable = OrderTable.of(4, true);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(Product.of("후라이드", new BigDecimal(16_000)), 1));
        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), MenuGroup.of("두마리메뉴"), menuProducts);


        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));
        given(menuDao.findById(any())).willReturn(Optional.ofNullable(menu));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블에는 주문을 등록할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        // given
        OrderTable orderTable = OrderTable.of(4, false);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(Product.of("후라이드", new BigDecimal(16_000)), 1));
        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), MenuGroup.of("두마리메뉴"), menuProducts);


        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        Order order = Order.of(orderLineItems, orderTable);
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        given(orderDao.findAll()).willReturn(orders);

        // when
        List<OrderResponse> result = orderService.list();

        // then
        assertThat(result).isNotNull();
        assertThat(order.getOrderLineItems().size()).isEqualTo(orderLineItems.size());
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        OrderTable orderTable = OrderTable.of(4, false);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(Product.of("후라이드", new BigDecimal(16_000)), 1));
        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), MenuGroup.of("두마리메뉴"), menuProducts);


        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        Order order = Order.of(orderLineItems, orderTable);
        given(orderDao.findById(any())).willReturn(Optional.ofNullable(order));

        OrderStatus orderStatus = OrderStatus.MEAL;

        // when
        OrderResponse result = orderService.changeOrderStatus(1L, orderStatus);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(orderStatus);
        assertThat(result.getOrderLineItems()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 주문이면 주문 상태를 변경할 수 없다.")
    void changeOrderStatus_not_exist_order() {
        // given
        given(orderDao.findById(any())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문입니다.");
    }

    @Test
    @DisplayName("이미 계산 완료된 주문이면 주문 상태를 변경할 수 없다.")
    void changeOrderStatus_order_status_already_complete() {
        // given
        OrderTable orderTable = OrderTable.of(4, false);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(Product.of("후라이드", new BigDecimal(16_000)), 1));
        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), MenuGroup.of("두마리메뉴"), menuProducts);


        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        Order order = Order.of(orderLineItems, orderTable, OrderStatus.COMPLETION);
        given(orderDao.findById(any())).willReturn(Optional.ofNullable(order));

        OrderStatus orderStatus = OrderStatus.MEAL;

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 계산 완료된 주문입니다.");
    }
}
