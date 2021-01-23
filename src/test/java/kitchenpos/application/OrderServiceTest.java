package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private OrderDao orderDao;

    private Order 주문;
    private MenuProduct 메뉴상품_후라이드;
    private MenuProduct 메뉴상품_양념치킨;
    private MenuGroup 후라이드양념반반메뉴;

    @BeforeEach
    void setUp() {
        Product 후라이드 = productService.findById(1l);
        Product 양념치킨 = productService.findById(2l);
        메뉴상품_후라이드 = new MenuProduct(후라이드.getId(), 1);
        메뉴상품_양념치킨 = new MenuProduct(양념치킨.getId(), 1);
        후라이드양념반반메뉴 = 메뉴그룹을_생성한다("후라이드양념반반메뉴");

        Menu menu = 메뉴를_생성한다(후라이드양념반반메뉴, "후라이드양념반반", 32000, 메뉴상품_후라이드, 메뉴상품_양념치킨);
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(2l);
        OrderTable orderTable = 테이블을_생성한다(1l, 0, false);

        주문 = new Order(orderTable.getId(), Arrays.asList(orderLineItem));
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
        Order order = 주문을_등록한다(주문);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
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
        order.setOrderStatus(OrderStatus.MEAL.name());

        Order newOrder = orderService.changeOrderStatus(order.getId(), order);

        assertThat(newOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태를 변경한다 : OrderStatus.COMPLETION이면 익셉션 발생")
    @Test
    void changeOrderStatusException() {

        Order order = 주문을_등록한다(주문);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable 테이블을_생성한다(Long id, int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTable(id, numberOfGuest, empty));
    }

    private Order 주문을_등록한다(Order order) {
        return orderService.create(order);
    }

    private Menu 메뉴를_생성한다(MenuGroup menuGroup, String name, int price, MenuProduct... products) {
        Menu menu = new Menu(name, BigDecimal.valueOf(price), menuGroup.getId(), Arrays.asList(products));
        return menuService.create(menu);
    }

    private MenuGroup 메뉴그룹을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupService.create(menuGroup);
    }
}