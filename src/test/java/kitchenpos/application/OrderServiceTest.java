package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.creator.MenuGroupHelper;
import kitchenpos.application.creator.MenuHelper;
import kitchenpos.application.creator.MenuProductHelper;
import kitchenpos.application.creator.OrderHelper;
import kitchenpos.application.creator.OrderLineItemHelper;
import kitchenpos.application.creator.OrderTableHelper;
import kitchenpos.application.creator.ProductHelper;
import kitchenpos.application.creator.TableGroupHelper;
import kitchenpos.dao.MenuGroupDao;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-10
 */
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("주문 생성 테스트")
    @Test
    void orderCreateTest() {
        Order order = getOrder();

        Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull();
        assertThat(savedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(order.getOrderLineItems().get(0).getMenuId());
        assertThat(savedOrder.getOrderLineItems().get(0).getOrderId()).isEqualTo(order.getOrderLineItems().get(0).getOrderId());
        assertThat(savedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(order.getOrderLineItems().get(0).getQuantity());
    }

    @DisplayName("주문 생성시 주문항목이 없는 경우")
    @Test
    void orderCreateWithOrderLineItemsTest() {
        Order order = getOrder();
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 중복되는 주문항목이 있는 경우")
    @Test
    void orderCreateWithDuplicateOrderLineItemsTest() {
        Order order = getOrder();

        List<OrderLineItem> list = new ArrayList<>();
        list.add(order.getOrderLineItems().get(0));
        list.add(order.getOrderLineItems().get(0));
        order.setOrderLineItems(list);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 주문항목이 비어있는 경우")
    @Test
    void orderCreateWithEmptyOrderLineItemsTest() {
        Order order = getOrder();
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 테이블이 공석인 경우")
    @Test
    void orderCreateWithNotEmptyTableTest() {
        Order order = getOrder();

        OrderTable orderTable = new OrderTable();
        orderTable.setId(order.getOrderTableId());
        orderTable.setEmpty(true);
        orderTableDao.save(orderTable);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 테이블이 존재하지 않는 경우")
    @Test
    void orderCreateWithNotRegisterTable() {
        Order order = getOrder();
        order.setOrderTableId(9999L);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 상태 변경")
    @Test
    void orderStateChange() {
        Order order = orderService.create(getOrder());
        Order orderForStateChange = new Order();
        orderForStateChange.setOrderStatus(OrderStatus.COMPLETION.name());

        Order savedOrder = orderService.changeOrderStatus(order.getId(), orderForStateChange);
        
        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(savedOrder.getOrderedTime()).isEqualTo(order.getOrderedTime());
        assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isEqualTo(order.getOrderLineItems().get(0).getSeq());
        assertThat(savedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(order.getOrderLineItems().get(0).getMenuId());
        assertThat(savedOrder.getOrderLineItems().get(0).getOrderId()).isEqualTo(order.getOrderLineItems().get(0).getOrderId());
        assertThat(savedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(order.getOrderLineItems().get(0).getQuantity());
    }


    @DisplayName("주문 생성시 상태가 COMPLETION 상태인 경우")
    @Test
    void orderStateChangeWithCompletionState() {
        Order order = orderService.create(getOrder());
        Order orderForStateChange = new Order();
        orderForStateChange.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(order.getId(), orderForStateChange);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderForStateChange))
                .isInstanceOf(IllegalArgumentException.class);
    }


    private Order getOrder() {
        Menu menu = menuService.create(getMenu());

        OrderLineItem orderLineItem = OrderLineItemHelper.create(menu, 1);

        OrderTable orderTable = orderTableDao.save(OrderTableHelper.create(false));

        TableGroupHelper.create(orderTable);

        return OrderHelper.create(orderTable, orderLineItem);
    }

    private Menu getMenu() {
        Product savedProduct01 = productDao.save(
                ProductHelper.create("product01", 10_000));
        Product savedProduct02 = productDao.save(
                ProductHelper.create("product02", 20_000));

        MenuProduct menuProduct01 = MenuProductHelper.create(savedProduct01, 1);
        MenuProduct menuProduct02 = MenuProductHelper.create(savedProduct02, 2);

        MenuGroup menuGroup = menuGroupDao.save(MenuGroupHelper.create("메뉴 그룹"));

        return MenuHelper.create("메뉴", 50_000, menuGroup, menuProduct01, menuProduct02);
    }

}
