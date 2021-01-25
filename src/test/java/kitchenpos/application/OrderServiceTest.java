package kitchenpos.application;

import kitchenpos.domain.*;
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

    private Order 주문;
    private List<OrderTable> orderTables;
    private MenuGroup 후라이드양념반반메뉴;
    private Product 후라이드상품;
    private Product 양념치킨상품;
    private MenuProduct 후라이드;
    private MenuProduct 양념치킨;


    @BeforeEach
    void setUp() {
        후라이드양념반반메뉴 = new MenuGroup("후라이드양념반반메뉴");
        menuGroupService.create(후라이드양념반반메뉴);
        후라이드상품 = productService.findById(1l);
        양념치킨상품 = productService.findById(2l);

        OrderTable 테이블_1번 = 테이블을_생성한다(0, true);
        OrderTable 테이블_2번 = 테이블을_생성한다(0, true);
        orderTables = new ArrayList<>();
        orderTables.add(테이블_1번);
        orderTables.add(테이블_2번);

        TableGroup tableGroup = tableGroupService.create(new TableGroup(orderTables));
        OrderTable orderTable = 테이블을_생성한다(tableGroup, 0, false);
        Menu menu = 메뉴를_생성한다(32000, 후라이드양념반반메뉴);
        menuService.create(menu);

        OrderLineItem orderLineItem = new OrderLineItem(menu, 2l);
        주문 = new Order(orderTable, Arrays.asList(orderLineItem));
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
        order.setOrderStatus(OrderStatus.MEAL);

        Order newOrder = orderService.changeOrderStatus(order.getId(), order);

        assertThat(newOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태를 변경한다 : OrderStatus.COMPLETION이면 익셉션 발생")
    @Test
    void changeOrderStatusException() {
        Order order = 주문을_등록한다(주문);
        order.setOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable 테이블을_생성한다(int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTable(numberOfGuest, empty));
    }

    private OrderTable 테이블을_생성한다(TableGroup tableGroup, int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTable(tableGroup, numberOfGuest, empty));
    }

    private Order 주문을_등록한다(Order order) {
        return orderService.create(order);
    }

    private Menu 메뉴를_생성한다(int price, MenuGroup menuGroup) {
        Menu menu = new Menu("후라이드양념반반", BigDecimal.valueOf(price), menuGroup);
        후라이드 = new MenuProduct(menu, 후라이드상품, 1);
        양념치킨 = new MenuProduct(menu, 양념치킨상품, 1);
        menu.updateMenuProducts(Arrays.asList(후라이드, 양념치킨));
        return menu;
    }

    private MenuGroup 메뉴그룹을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupService.create(menuGroup);
    }
}