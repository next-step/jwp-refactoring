package kitchenpos.order;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableService;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @DisplayName("주문 등록")
    @Test
    void createOrder() {
        Order order = orderService.create(setUpOrder(false));

        assertThat(order.getId()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(order.getOrderLineItems()).isNotEmpty();
    }

    @DisplayName("주문 등록 실패")
    @Test
    void createOrderFail() {
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(setUpOrder(true)));
    }

    @DisplayName("주문 조회")
    @Test
    void findOrder() {
        orderService.create(setUpOrder(false));
        List<Order> orders = orderService.list();

        assertThat(orders).isNotEmpty();
        assertThat(orders).extracting("orderLineItems").isNotEmpty();
    }

    @DisplayName("주문 상태 변경")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, mode = EnumSource.Mode.EXCLUDE, names = {"COMPLETION"})
    void changeOrderStatus(OrderStatus status) {
        Order order = orderService.create(setUpOrder(false));
        Order changedOrder = orderService.changeOrderStatus(order.getId(), new Order() {
            {
                setOrderStatus(status.name());
            }
        });

        assertThat(changedOrder.getOrderStatus()).isEqualTo(status.name());
    }

    @DisplayName("주문 상태 변경 실패")
    @Test
    void changeOrderStatusFail() {
        Order order = orderService.create(setUpOrder(false));
        orderService.changeOrderStatus(order.getId(), new Order() {
            {
                setOrderStatus(OrderStatus.COMPLETION.name());
            }
        });

        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order() {
            {
                setOrderStatus(OrderStatus.MEAL.name());
            }
        }));
    }

    private Order setUpOrder(boolean flag) {
        OrderTable savedOrderTable = setUpOrderTable(flag);
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        Menu menu = setUpMenu("순대국 + 치킨", new BigDecimal(25000));
        order.setOrderLineItems(Arrays.asList(setUpOrderLineItem(menu.getId(), 1L)));
        return order;
    }

    private OrderLineItem setUpOrderLineItem(Long menuId, Long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    private OrderTable setUpOrderTable(boolean flag) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(flag);
        return tableService.create(orderTable);
    }

    private Menu setUpMenu(String name, BigDecimal price) {
        MenuGroup menuGroup = menuGroupService.list().get(0);
        Product product = productService.list().get(0);
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroup.getId());
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2L);
        menu.setMenuProducts(Arrays.asList(menuProduct));
        return menuService.create(menu);
    }
}
