package kitchenpos.application;

import static kitchenpos.dto.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.application.creator.MenuGroupHelper;
import kitchenpos.application.creator.MenuHelper;
import kitchenpos.application.creator.MenuProductHelper;
import kitchenpos.application.creator.OrderHelper;
import kitchenpos.application.creator.OrderLineItemHelper;
import kitchenpos.application.creator.OrderTableHelper;
import kitchenpos.application.creator.ProductHelper;
import kitchenpos.application.creator.TableGroupHelper;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuGroupDto;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.ProductDto;
import kitchenpos.dto.TableGroupDto;
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
        OrderTableDto orderTable = tableService.create(OrderTableHelper.create(true));

        TableGroupDto tableGroup = TableGroupHelper.create(orderTable, orderTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 주문 테이블이 없는 경우")
    @Test
    void tableGroupCreateWithEmptyOrderTablesTest() {
        TableGroupDto tableGroup = new TableGroupDto();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 한개의 주문 테이블만 있는 경우")
    @Test
    void tableGroupCreateWithSingleOrderTablesTest() {
        OrderTableDto orderTable = tableService.create(OrderTableHelper.create(true));

        TableGroupDto tableGroup = TableGroupHelper.create(orderTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 테이블이 공석이 아닌 경우")
    @Test
    void tableGroupCreateWhenTableIsNotEmptyTest() {
        OrderTableDto orderTable01 = tableService.create(OrderTableHelper.create(false));
        OrderTableDto orderTable02 = tableService.create(OrderTableHelper.create(true));

        TableGroupDto tableGroup = TableGroupHelper.create(orderTable01, orderTable02);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 지정 해제시 주문 테이블중 하나라도 COOKING, MEAL 상태인 경우")
    @Test
    void tableGroupCreateWhenTableNotCompleteStateTest() {
        OrderTableDto orderTable01 = tableService.create(OrderTableHelper.create(true));
        OrderTableDto orderTable02 = tableService.create(OrderTableHelper.create(true));

        TableGroupDto tableGroup = TableGroupHelper.create(orderTable01, orderTable02);
        TableGroupDto savedGroup = tableGroupService.create(tableGroup);

        OrderDto order = orderService.create(getOrder(orderTable01));

        OrderDto orderForChangeState = new OrderDto();
        orderForChangeState.setOrderStatus(COOKING.name());

        orderService.changeOrderStatus(order.getId(), orderForChangeState);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }





    private OrderDto getOrder(OrderTableDto orderTable) {
        MenuDto menu = menuService.create(getMenu());

        OrderLineItemDto orderLineItem = OrderLineItemHelper.create(menu, 1);

        TableGroupHelper.create(orderTable);

        return OrderHelper.create(orderTable, orderLineItem);
    }

    private MenuDto getMenu() {
        ProductDto savedProduct01 = productDao.save(
                ProductHelper.create("product01", 10_000));
        ProductDto savedProduct02 = productDao.save(
                ProductHelper.create("product02", 20_000));

        MenuProductDto menuProduct01 = MenuProductHelper.create(savedProduct01, 1);
        MenuProductDto menuProduct02 = MenuProductHelper.create(savedProduct02, 2);

        MenuGroupDto menuGroup = menuGroupDao.save(MenuGroupHelper.create("메뉴 그룹"));

        return MenuHelper.create("메뉴", 50_000, menuGroup, menuProduct01, menuProduct02);
    }

}
