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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    private Order order;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("a"));

        List<MenuProduct> menuProducts = new ArrayList<>();
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        Menu menu = menuDao.save(new Menu("menu", BigDecimal.ONE, menuGroup.getId()));

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable);
        orderTables.add(orderTable);
        tableGroup = tableGroupDao.save(new TableGroup(orderTables));
        orderTable.setTableGroupId(tableGroup.getId());
        orderTableDao.save(orderTable);
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 1));
        order = orderDao.save(new Order(orderTable.getId(), orderLineItems));
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
    }

    @DisplayName("테이블 그룹을 생성한다. / 주문 테이블의 갯수가 2보다 작을 수 없다.")
    @Test
    void create_fail_minimumSize() {
    }

    @DisplayName("테이블 그룹을 생성한다. / 주문 테이블이 비어있을 수 없다.")
    @Test
    void create_fail_orderTableEmpty() {
    }

    @DisplayName("테이블 그룹을 조회한다.")
    @Test
    void list() {
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void unGroup_success() {

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNotNull();
        }

        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());

        tableGroupService.ungroup(tableGroup.getId());

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNull();
        }
    }

    @DisplayName("테이블 그룹을 해제한다. / 요리중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_cooking() {

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNotNull();
        }

        Order order1 = orderDao.findById(order.getId()).get();
        assertThat(order1.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다. / 식사중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_meal() {

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNotNull();
        }

        order.setOrderStatus(OrderStatus.MEAL.name());
        orderDao.save(order);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }
}
