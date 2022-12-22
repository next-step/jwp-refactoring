package kitchenpos.application;

import static kitchenpos.OrderBuilder.anOrder;
import static kitchenpos.OrderTableBuilder.emptyOrderTableWithGuestNo;
import static kitchenpos.OrderTableBuilder.nonEmptyOrderTableWithGuestNo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.OrderBuilder;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    private OrderBuilder orderBuilder;
    private Menu menu;

    @BeforeEach
    void setup() {
        menu = menu(
            "점심메뉴",
            BigDecimal.ONE,
            persist(menuGroup("양식 메뉴 그룹")),
            Arrays.asList(
                menuProduct(persist(product("피자", BigDecimal.ONE)), 1),
                menuProduct(persist(product("파스타", BigDecimal.ONE)), 1)
            ));
        orderBuilder = anOrder()
            .withOrderTable(persist(nonEmptyOrderTableWithGuestNo(2)))
            .withOrderLineItems(Collections.singletonList(orderLineItem(persist(menu).getId(), 1)));
    }

    @Test
    void 생성시_정상적인주문인경우_조리상태인주문반환() {
        assertThat(orderService.create(orderBuilder.build()).getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void 생성시_주문테이블이존재하지않은경우_예외발생() {
        final Order order = orderBuilder
            .withOrderTable(nonEmptyOrderTableWithGuestNo(2))
            .build();
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성시_비어있는주문테이블일경우_예외발생() {
        final Order order = orderBuilder
            .withOrderTable(persist(emptyOrderTableWithGuestNo(2)))
            .build();
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성시_주문항목이포함되지않은경우_예외발생() {
        final Order order = orderBuilder
            .withOrderLineItems(Collections.emptyList())
            .build();
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성시_주문항목의메뉴개수와_존재하는메뉴개수가_일치하지않은경우_예외발생() {
        final Order order = orderBuilder
            .withOrderLineItems(Collections.singletonList(orderLineItem(menu.getId(), 1)))
            .build();
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상태변경시_주문이존재하지않는경우_예외발생() {
        final Order order = orderBuilder.build();
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상태변경시_완료된주문인경우_예외발생() {
        // ASIS status
        Order savedOrderWithCompletionStatus = orderService.create(orderBuilder.build());
        savedOrderWithCompletionStatus.setOrderStatus(OrderStatus.COMPLETION.name());
        persist(savedOrderWithCompletionStatus);
        // TOBE status
        final Order orderWithMealStatus = orderBuilder
            .withStatus(OrderStatus.MEAL)
            .build();

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderWithCompletionStatus.getId(), orderWithMealStatus))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상태변경시_완료된주문이아닌경우_변경성공() {
        // ASIS status
        final Order savedOrderWithCookingStatus = orderService.create(orderBuilder.build());
        // TOBE status
        final Order orderWithMealStatus = orderBuilder
            .withStatus(OrderStatus.MEAL)
            .build();

        assertThat(orderService.changeOrderStatus(savedOrderWithCookingStatus.getId(), orderWithMealStatus)
            .getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 조회시_존재하는주문목록반환() {
        orderService.create(orderBuilder.build());
        assertThat(orderService.list()).isNotEmpty();
    }

    private Product persist(Product product) {
        return productDao.save(product);
    }

    private Menu persist(Menu menu) {
        return menuService.create(menu);
    }

    private MenuGroup persist(MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    private OrderTable persist(OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    private Order persist(Order order) {
        return orderDao.save(order);
    }

    private OrderLineItem orderLineItem(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    private MenuGroup menuGroup(String name) {
        return new MenuGroup(name);
    }

    private Product product(String name, BigDecimal price) {
        return new Product(name, price);
    }

    private Menu menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup.getId(), menuProducts);
    }

    private MenuProduct menuProduct(Product product, long quantity) {
        return new MenuProduct(product.getId(), quantity);
    }
}
