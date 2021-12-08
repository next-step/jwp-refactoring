package kitchenpos;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@Transactional
public abstract class ServiceTest {

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;
    
    @Autowired
    protected MenuService menuService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderService orderService;

    protected Product 상품_저장() {
        Product product = new Product();
        product.setName("매운양념치킨");
        product.setPrice(new BigDecimal(18_000));
        return productService.create(product);
    }

    protected MenuGroup 메뉴_그룹_저장() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("세마리메뉴");
        return menuGroupService.create(menuGroup);
    }

    protected Menu 메뉴_저장() {
        Product savedProduct = 상품_저장();
        MenuGroup savedMenuGroup = 메뉴_그룹_저장();

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);

        Menu menu = new Menu();
        menu.setName(savedProduct.getName());
        menu.setPrice(savedProduct.getPrice());
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(Collections.singletonList(menuProduct));
        return menuService.create(menu);
    }

    protected OrderTable 테이블_저장(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(empty);
        return tableService.create(orderTable);
    }

    protected TableGroup 테이블_그룹_저장() {
        OrderTable orderTable1 = 테이블_저장(true);
        OrderTable orderTable2 = 테이블_저장(true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        return tableGroupService.create(tableGroup);
    }

    protected Order 주문_저장() {
        OrderTable orderTable = 테이블_저장(false);
        Menu savedMenu = 메뉴_저장();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        return orderService.create(order);
    }

    protected void 주문_상태를_COMPLETION_으로_상태_변경(Long orderId) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(orderId, order);
    }

    protected OrderTable 테이블_조회(Long orderTableId) {
        return tableService.list().stream()
                .filter(it -> it.getId().equals(orderTableId))
                .findFirst()
                .get();
    }
}
