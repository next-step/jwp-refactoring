package kitchenpos.table.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.CreateTableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import static kitchenpos.common.fixture.NameFixture.nameMenuGroupA;
import static kitchenpos.menu.domain.fixture.MenuFixture.menuA;
import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;
import static kitchenpos.table.application.TableGroupService.ORDER_STATUS_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.OrderTables.ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.OrderTables.ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.fixture.OrderTableFixture.emptyOrderTable;
import static kitchenpos.table.domain.fixture.OrderTableFixture.notEmptyOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TableGroupService")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private TableGroup tableGroupA;
    private TableGroup tableGroupB;
    private Orders order;
    private OrderTable orderTableA;
    private OrderTable orderTableB;
    private MenuGroup menuGroup;
    private Menu menu;
    private OrderLineItems orderLineItemsA;
    private OrderLineItems orderLineItemsB;

    @BeforeEach
    public void setUp() {
        super.setUp();
        menuGroup = menuGroupRepository.save(new MenuGroup(nameMenuGroupA()));
        menu = menuRepository.save(menuA());
//        orderTableA = createEmptyOrderTable(tableGroupB);
//        orderTableB = createEmptyOrderTable(tableGroupB);
//        tableGroupA = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(orderTableA, orderTableB))));
//        tableGroupB = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(orderTableA, orderTableB))));
//        orderTableA.setEmpty(false);
//        orderTableB.setEmpty(false);
//        order = orderRepository.save(new Orders(orderTableA, orderLineItemsA()));
        orderLineItemsB = new OrderLineItems();
        orderLineItemsA = orderLineItemsA();
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        OrderTable orderTableA = emptyTableEmptyNotEmptyTableGroup(tableGroupB);
        OrderTable orderTableB = emptyTableEmptyNotEmptyTableGroup(tableGroupB);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(orderTableA, orderTableB))));
        TableGroupResponse saveTableGroup = tableGroupService.create(new CreateTableGroupRequest(tableGroup.getOrderTableIds()));
        assertThat(saveTableGroup.getCreatedDate()).isNotNull();
    }

    @DisplayName("테이블 그룹을 생성한다. / 주문 테이블의 갯수가 2보다 작을 수 없다.")
    @Test
    void create_fail_minimumSize() {

        assertThatThrownBy(() -> {
            TableGroup tableGroupC = tableGroupRepository.save(new TableGroup(new OrderTables(Collections.singletonList(emptyTableEmptyTableGroup()))));
            tableGroupService.create(new CreateTableGroupRequest(tableGroupC.getOrderTableIds()));
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 생성한다. / 주문 테이블이 비어있을 수 없다.")
    @Test
    void create_fail_orderTableEmpty() {
        TableGroup failTableGroup = tableGroupRepository.save(new TableGroup());
        assertThatThrownBy(() -> tableGroupService.create(new CreateTableGroupRequest(failTableGroup.getOrderTableIds())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void unGroup_success() {

        OrderTable orderTableA = emptyTableEmptyTableGroup();
        OrderTable orderTableB = emptyTableEmptyTableGroup();

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(orderTableA, orderTableB))));

        orderTableA.setTableGroup(tableGroup);
        orderTableB.setTableGroup(tableGroup);

        orderTableRepository.saveAll(Arrays.asList(orderTableA, orderTableB));

        테이블_그룹_존재_검증(tableGroup);
        orderTableA.setEmpty(false);

        Orders order = new Orders(orderTableA, orderLineItemsA);
        order.setOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);

        tableGroupService.ungroup(tableGroup.getId());

        테이블_그룹_해제_검증됨(tableGroup);
    }

    @DisplayName("테이블 그룹을 해제한다. / 요리중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_cooking() {
        OrderTable orderTableA = emptyTableEmptyTableGroup();
        OrderTable orderTableB = emptyTableEmptyTableGroup();

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(orderTableA, orderTableB))));

        orderTableA.setTableGroup(tableGroup);
        orderTableB.setTableGroup(tableGroup);

        orderTableRepository.saveAll(Arrays.asList(orderTableA, orderTableB));

        테이블_그룹_존재_검증(tableGroup);

        orderTableA.setEmpty(false);

        Orders order = new Orders(orderTableA, orderLineItemsA);
        order.setOrderStatus(OrderStatus.COOKING);
        orderRepository.save(order);

        주문_요리중_상태_변경(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다. / 식사중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_meal() {

        OrderTable orderTableA = emptyTableEmptyTableGroup();
        OrderTable orderTableB = emptyTableEmptyTableGroup();

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(orderTableA, orderTableB))));

        orderTableA.setTableGroup(tableGroup);
        orderTableB.setTableGroup(tableGroup);

        orderTableRepository.saveAll(Arrays.asList(orderTableA, orderTableB));

        테이블_그룹_존재_검증(tableGroup);

        orderTableA.setEmpty(false);

        Orders order = new Orders(orderTableA, orderLineItemsA);
        order.setOrderStatus(OrderStatus.MEAL);
        orderRepository.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    private OrderTable notEmptyTableNotEmptyTableGroup(TableGroup tableGroup) {
        OrderTable orderTable = orderTableRepository.save(notEmptyOrderTable());
        orderTable.setTableGroup(tableGroup);
        return orderTableRepository.save(orderTable);
    }

    private OrderTable emptyTableEmptyTableGroup() {
        return orderTableRepository.save(emptyOrderTable());
    }

    private OrderTable emptyTableEmptyNotEmptyTableGroup(TableGroup tableGroup) {
        return orderTableRepository.save(new OrderTable(tableGroup, true));
    }

    private OrderTable notEmptyTableEmptyNotEmptyTableGroup(TableGroup tableGroup) {
        return orderTableRepository.save(new OrderTable(tableGroup, false));
    }

    private void 주문_식사중_상태_변경() {
        order.setOrderStatus(OrderStatus.MEAL);
        orderRepository.save(order);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    private void 주문_요리중_상태_변경(Orders order) {
        Orders order1 = orderRepository.findById(order.getId()).get();
        assertThat(order1.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    private void 주문_완료_상태_변경() {
        order.setOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    private void 테이블_그룹_존재_검증(TableGroup tableGroup) {
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableRepository.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroup()).isNotNull();
        }
    }

    private void 테이블_그룹_해제_검증됨(TableGroup tableGroup) {
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableRepository.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroup()).isNull();
        }
    }
}
