package kitchenpos.table.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static kitchenpos.table.application.TableGroupService.ORDER_STATUS_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.TableGroup.ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.TableGroup.ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.*;

@DisplayName("TableGroupService")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private TableGroup tableGroup;
    private TableGroup tableGroup1;
    private TableGroup unGroup;
    private Order order;
    private List<OrderTable> orderTables1HasTableGroup = new ArrayList<>();
    private List<OrderTable> upGroupOrderTables = new ArrayList<>();
    private List<OrderTable> create = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("a"));
        TableGroup tableGroup2 = tableGroupDao.save(new TableGroup());
        OrderTable orderTable1 = orderTableDao.save(new OrderTable());
        orderTable1.setTableGroupId(tableGroup2.getId());
        orderTable1.empty();
        orderTable1.setTableGroupId(null);
        OrderTable create1 = orderTableDao.save(new OrderTable());
        create1.setTableGroupId(tableGroup2.getId());
        create1.empty();
        create1.setTableGroupId(null);
        orderTableDao.save(create1);
        orderTable1.setTableGroupId(tableGroup2.getId());
        orderTable1.empty();
        orderTable1.setTableGroupId(null);
        orderTableDao.save(orderTable1);
        OrderTable create2 = orderTableDao.save(new OrderTable());
        create2.setTableGroupId(tableGroup2.getId());
        create2.empty();
        create2.setTableGroupId(null);
        orderTableDao.save(create2);
        orderTable1.setTableGroupId(tableGroup2.getId());
        orderTable1.empty();
        orderTable1.setTableGroupId(null);
        orderTableDao.save(orderTable1);
        OrderTable orderTable2 = orderTableDao.save(new OrderTable());
        orderTable2.setTableGroupId(tableGroup2.getId());
        orderTable2.empty();
        orderTable2.setTableGroupId(null);
        orderTableDao.save(orderTable2);
        Menu menu = menuDao.save(new Menu("menu", BigDecimal.ONE, menuGroup.getId()));

        tableGroup = tableGroupDao.save(new TableGroup());
        tableGroup.setOrderTables(orderTables1HasTableGroup);
        unGroup = tableGroupDao.save(new TableGroup());
        unGroup.setOrderTables(upGroupOrderTables);
        unGroup.setOrderTables(orderTables1HasTableGroup);
        orderTable1.setTableGroupId(unGroup.getId());
        orderTable1 = orderTableDao.save(orderTable1);
        orderTable2.setTableGroupId(unGroup.getId());
        orderTable2 = orderTableDao.save(orderTable2);
        orderTables1HasTableGroup.add(orderTable1);
        upGroupOrderTables.add(orderTable1);
        upGroupOrderTables.add(orderTable2);
        create.add(create1);
        create.add(create2);
        tableGroup1 = tableGroupDao.save(new TableGroup());
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 1));
        order = orderDao.save(new Order(orderTable1.getId(), orderLineItems));
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        tableGroup1.setOrderTables(create);
        TableGroup saveTableGroup = tableGroupService.create(tableGroup1);
        assertThat(saveTableGroup.getCreatedDate()).isNotNull();
    }

    @DisplayName("테이블 그룹을 생성한다. / 주문 테이블의 갯수가 2보다 작을 수 없다.")
    @Test
    void create_fail_minimumSize() {
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 생성한다. / 주문 테이블이 비어있을 수 없다.")
    @Test
    void create_fail_orderTableEmpty() {
        assertThatThrownBy(() -> tableGroupService.create(tableGroup1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void unGroup_success() {

        for (OrderTable orderTable : unGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNotNull();
        }

        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());

        tableGroupService.ungroup(unGroup.getId());

        for (OrderTable orderTable : unGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNull();
        }
    }

    @DisplayName("테이블 그룹을 해제한다. / 요리중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_cooking() {

        for (OrderTable orderTable : unGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNotNull();
        }

        Order order1 = orderDao.findById(order.getId()).get();
        assertThat(order1.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

        assertThatThrownBy(() -> tableGroupService.ungroup(unGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다. / 식사중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_meal() {

        for (OrderTable orderTable : unGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNotNull();
        }

        order.setOrderStatus(OrderStatus.MEAL.name());
        orderDao.save(order);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> tableGroupService.ungroup(unGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }
}
