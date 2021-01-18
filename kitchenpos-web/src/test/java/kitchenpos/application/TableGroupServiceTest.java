package kitchenpos.application;

import static kitchenpos.domain.model.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.application.creator.MenuGroupHelper;
import kitchenpos.application.creator.MenuHelper;
import kitchenpos.application.creator.MenuProductHelper;
import kitchenpos.application.creator.OrderHelper;
import kitchenpos.application.creator.OrderLineItemHelper;
import kitchenpos.application.creator.OrderTableHelper;
import kitchenpos.application.creator.ProductHelper;
import kitchenpos.application.creator.TableGroupHelper;
import kitchenpos.domain.model.MenuGroup;
import kitchenpos.domain.model.OrderTable;
import kitchenpos.domain.model.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemCreateRequest;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;
import kitchenpos.domain.repository.MenuGroupDao;
import kitchenpos.domain.repository.ProductDao;
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
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("단체 지정 생성시 중복되는 주문 테이블이 있는 경우")
    @Test
    void tableGroupCreateWithDuplicateOrderTablesTest() {
        OrderTableDto orderTable = tableService.create(OrderTableHelper.createRequest(true));
        OrderTable table = orderTable.toEntity();
        TableGroupCreateRequest tableGroup = TableGroupHelper.createRequest(table.getId(), table.getId());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 주문 테이블이 없는 경우")
    @Test
    void tableGroupCreateWithEmptyOrderTablesTest() {
        TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 한개의 주문 테이블만 있는 경우")
    @Test
    void tableGroupCreateWithSingleOrderTablesTest() {
        OrderTableDto orderTable = tableService.create(OrderTableHelper.createRequest(true));

        TableGroupCreateRequest tableGroup = TableGroupHelper.createRequest(orderTable.getId());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 테이블이 공석이 아닌 경우")
    @Test
    void tableGroupCreateWhenTableIsNotEmptyTest() {
        OrderTableDto orderTable01 = tableService.create(OrderTableHelper.createRequest(false));
        OrderTableDto orderTable02 = tableService.create(OrderTableHelper.createRequest(true));

        TableGroupCreateRequest tableGroup = TableGroupHelper.createRequest(orderTable01.getId(), orderTable02.getId());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 지정 해제시 주문 테이블중 하나라도 COOKING, MEAL 상태인 경우")
    @Test
    void tableGroupCreateWhenTableNotCompleteStateTest() {
        OrderTableDto orderTable01 = tableService.create(OrderTableHelper.createRequest(true));
        OrderTableDto orderTable02 = tableService.create(OrderTableHelper.createRequest(true));

        TableGroupCreateRequest tableGroup = TableGroupHelper.createRequest(orderTable01.getId(), orderTable02.getId());
        TableGroupDto savedGroup = tableGroupService.create(tableGroup);
        OrderCreateRequest savedOrder = getSavedOrder(orderTable01);

        tableService.changeEmpty(orderTable01.getId(), false);
        tableService.changeEmpty(orderTable02.getId(), false);

        List<OrderLineItemCreateRequest> itemCreateRequests = savedOrder.getOrderLineItems().stream()
                .map(it -> new OrderLineItemCreateRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable01.getId(), itemCreateRequests);
        OrderDto order = orderService.create(orderCreateRequest);

        orderService.changeOrderStatus(order.getId(), COOKING);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderCreateRequest getSavedOrder(OrderTableDto orderTable) {
        MenuDto menu = menuService.create(getMenuRequest());
        OrderLineItemCreateRequest orderLineItem = OrderLineItemHelper.createRequest(menu, 1);
        return OrderHelper.createRequest(orderTable.toEntity(), Collections.singletonList(orderLineItem));
    }

    private MenuCreateRequest getMenuRequest() {
        List<MenuProductRequest> savedMenuProductRequests = getSavedMenuProductRequest();
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupHelper.create ("메뉴 그룹"));
        return MenuHelper.createRequest("메뉴", 50_000, menuGroup.getId(), savedMenuProductRequests);
    }

    private List<MenuProductRequest> getSavedMenuProductRequest() {
        Product savedProduct01 = productDao.save(ProductHelper.createRequest("product01", 10_000).toEntity());
        Product savedProduct02 = productDao.save(ProductHelper.createRequest("product02", 20_000).toEntity());

        MenuProductRequest menuProduct01 = MenuProductHelper.createRequest(savedProduct01.getId(), 1);
        MenuProductRequest menuProduct02 = MenuProductHelper.createRequest(savedProduct02.getId(), 2);

        return Arrays.asList(menuProduct01, menuProduct02);
    }

}
