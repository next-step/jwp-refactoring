package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.request.*;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private OrderRepository orderRepository;

    private OrderRequest 주문;
    private MenuProduct 후라이드;
    private MenuProduct 양념치킨;
    private MenuGroup 후라이드양념반반메뉴;
    private Product 후라이드상품;
    private Product 양념치킨상품;

    private List<OrderTable> orderTables;
    private OrderTable 테이블_1번;
    private OrderTable 테이블_2번;


    @BeforeEach
    void setUp() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("후라이드양념반반메뉴");
        후라이드양념반반메뉴 = menuGroupService.create(menuGroupRequest);
        후라이드상품 = productService.findById(1l);
        양념치킨상품 = productService.findById(2l);
        MenuRequest menuRequest = 메뉴를_생성한다(32000, 후라이드양념반반메뉴);
        Menu menu = menuService.create(menuRequest);

        테이블_1번 = 테이블을_생성한다(0, true);
        테이블_2번 = 테이블을_생성한다(0, true);
        orderTables = new ArrayList<>();
        orderTables.add(테이블_1번);
        orderTables.add(테이블_2번);

        TableGroup tableGroup = 테이블_그룹을_생성한다(new TableGroup(orderTables));
        OrderTable orderTable = 테이블을_생성한다(tableGroup, 0, false);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2l);
        주문 = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest));
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
        Order order = 주문을_등록한다(주문);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderLineItems().size()).isGreaterThanOrEqualTo(1);
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        주문을_등록한다(주문);
        assertThat(orderService.list().size()).isGreaterThanOrEqualTo(1);
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        Order order = 주문을_등록한다(주문);
        주문.setOrderStatus(OrderStatus.MEAL);

        Order newOrder = orderService.changeOrderStatus(order.getId(), 주문);

        assertThat(newOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태를 변경한다 : OrderStatus.COMPLETION이면 익셉션 발생")
    @Test
    void changeOrderStatusException() {
        Order order = 주문을_등록한다(주문);
        order.setOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), 주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable 테이블을_생성한다(int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTableRequest(numberOfGuest, empty));
    }

    private OrderTable 테이블을_생성한다(TableGroup tableGroup, int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTableRequest(tableGroup.getId(), numberOfGuest, empty));
    }

    private Order 주문을_등록한다(OrderRequest orderRequest) {
        return orderService.create(orderRequest);
    }

    private MenuRequest 메뉴를_생성한다(int price, MenuGroup menuGroup) {
        Menu menu = new Menu("후라이드양념반반", BigDecimal.valueOf(price), menuGroup);
        후라이드 = new MenuProduct(menu, 후라이드상품, 1);
        양념치킨 = new MenuProduct(menu, 양념치킨상품, 1);
        menu.updateMenuProducts(Arrays.asList(후라이드, 양념치킨));
        return MenuRequest.of(menu);
    }

    private TableGroup 테이블_그룹을_생성한다(TableGroup tableGroup) {
        return tableGroupService.create(TableGroupRequest.of(tableGroup));
    }

}