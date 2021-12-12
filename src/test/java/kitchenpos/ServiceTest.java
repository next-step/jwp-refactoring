package kitchenpos;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    protected OrderTableService orderTableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderService orderService;

    protected Product 상품_저장() {
        return productService.create(new Product("매운양념치킨", 18_000));
    }

    protected MenuGroupResponse 메뉴_그룹_저장() {
        return menuGroupService.create(new MenuGroupRequest("세마리메뉴"));
    }

    protected MenuResponse 메뉴_저장() {
        Product savedProduct = 상품_저장();
        MenuGroupResponse savedMenuGroupResponse = 메뉴_그룹_저장();
        MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 1);
        MenuRequest menuRequest = new MenuRequest(
                savedProduct.getName(), savedProduct.getPrice(), savedMenuGroupResponse.getId(),
                Collections.singletonList(menuProductRequest));
        return menuService.create(menuRequest);
    }

    protected OrderTable 테이블_저장(boolean empty) {
        return orderTableService.create(new OrderTable(2, empty));
    }

    protected TableGroup 테이블_그룹_저장() {
        OrderTable orderTable1 = 테이블_저장(true);
        OrderTable orderTable2 = 테이블_저장(true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));
        return tableGroupService.create(tableGroup);
    }

    protected Order 주문_저장() {
        OrderTable orderTable = 테이블_저장(false);
        MenuResponse savedMenuResponse = 메뉴_저장();
        OrderLineItem orderLineItem = new OrderLineItem(savedMenuResponse.getId(), 2);
        Order order = new Order(
                orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), Collections.singletonList(orderLineItem));
        return orderService.create(order);
    }

    protected void 주문_상태를_COMPLETION_으로_상태_변경(Long orderId) {
        Order order = new Order(OrderStatus.COMPLETION);
        orderService.changeOrderStatus(orderId, order);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected OrderTable 테이블_조회(Long orderTableId) {
        return orderTableService.list().stream()
                .filter(it -> it.getId().equals(orderTableId))
                .findFirst()
                .get();
    }
}
