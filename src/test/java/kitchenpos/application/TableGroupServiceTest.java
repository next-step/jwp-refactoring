package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
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
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
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
        OrderTable orderTable = tableService.create(OrderTableHelper.create(true));

        TableGroup tableGroup = TableGroupHelper.create(orderTable, orderTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 주문 테이블이 없는 경우")
    @Test
    void tableGroupCreateWithEmptyOrderTablesTest() {
        TableGroup tableGroup = new TableGroup();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 한개의 주문 테이블만 있는 경우")
    @Test
    void tableGroupCreateWithSingleOrderTablesTest() {
        OrderTable orderTable = tableService.create(OrderTableHelper.create(true));

        TableGroup tableGroup = TableGroupHelper.create(orderTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 테이블이 공석이 아닌 경우")
    @Test
    void tableGroupCreateWhenTableIsNotEmptyTest() {
        OrderTable orderTable01 = tableService.create(OrderTableHelper.create(false));
        OrderTable orderTable02 = tableService.create(OrderTableHelper.create(true));

        TableGroup tableGroup = TableGroupHelper.create(orderTable01, orderTable02);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 지정 해제시 주문 테이블중 하나라도 COOKING, MEAL 상태인 경우")
    @Test
    void tableGroupCreateWhenTableNotCompleteStateTest() {
        OrderTable orderTable01 = tableService.create(OrderTableHelper.create(true));
        OrderTable orderTable02 = tableService.create(OrderTableHelper.create(true));

        TableGroup tableGroup = TableGroupHelper.create(orderTable01, orderTable02);
        TableGroup savedGroup = tableGroupService.create(tableGroup);

        Order order = orderService.create(getOrder(orderTable01));

        Order orderForChangeState = new Order();
        orderForChangeState.setOrderStatus(COOKING.name());

        orderService.changeOrderStatus(order.getId(), orderForChangeState);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }





    private Order getOrder(OrderTable orderTable) {
        Menu menu = menuService.create(getMenu());

        OrderLineItem orderLineItem = OrderLineItemHelper.create(menu, 1);

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
