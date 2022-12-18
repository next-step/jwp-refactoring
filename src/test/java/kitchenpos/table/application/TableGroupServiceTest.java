package kitchenpos.table.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static java.util.Collections.singletonList;
import static kitchenpos.common.fixture.NameFixture.nameMenuGroupA;
import static kitchenpos.menu.domain.fixture.MenuFixture.menuA;
import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;
import static kitchenpos.table.application.TableGroupService.ORDER_STATUS_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.fixture.OrderTableFixture.notEmptyOrderTable;
import static kitchenpos.table.domain.TableGroup.ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.TableGroup.ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.fixture.TableGroupFixture.tableGroupA;
import static kitchenpos.table.domain.fixture.TableGroupFixture.tableGroupB;
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
        tableGroupA = tableGroupRepository.save(tableGroupA());
        tableGroupB = tableGroupRepository.save(tableGroupB());
        orderTableA = createNotEmptyOrderTable(tableGroupA);
        orderTableB = createEmptyOrderTable(tableGroupB);
        tableGroupA.setOrderTables(singletonList(orderTableA));
        tableGroupB.setOrderTables(singletonList(orderTableB));
        order = orderRepository.save(new Orders(orderTableA, orderLineItemsA()));
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        tableGroupA.setOrderTables(Arrays.asList(makeNullTableGroupOrderTable(changeEmptyOrder()), makeNullTableGroupOrderTable((changeEmptyOrder()))));
        TableGroup saveTableGroup = tableGroupService.create(tableGroupA);
        assertThat(saveTableGroup.getCreatedDate()).isNotNull();
    }

    @DisplayName("테이블 그룹을 생성한다. / 주문 테이블의 갯수가 2보다 작을 수 없다.")
    @Test
    void create_fail_minimumSize() {
        assertThatThrownBy(() -> tableGroupService.create(tableGroupA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 생성한다. / 주문 테이블이 비어있을 수 없다.")
    @Test
    void create_fail_orderTableEmpty() {
        TableGroup failTableGroup = tableGroupRepository.save(new TableGroup());
        assertThatThrownBy(() -> tableGroupService.create(failTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void unGroup_success() {

        테이블_그룹_존재_검증(tableGroupB);

        Orders order = new Orders(orderTableB, orderLineItemsA);
        order.setOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);

        tableGroupService.ungroup(tableGroupB.getId());

        테이블_그룹_해제_검증됨(tableGroupB);
    }

    @DisplayName("테이블 그룹을 해제한다. / 요리중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_cooking() {

        테이블_그룹_존재_검증(tableGroupA);

        주문_요리중_상태_변경();

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupA.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다. / 식사중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_meal() {

        테이블_그룹_존재_검증(tableGroupA);

        주문_식사중_상태_변경();

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupA.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    private OrderTable makeNullTableGroupOrderTable(OrderTable orderTable) {
        orderTable.setTableGroup(null);
        return orderTableRepository.save(orderTable);
    }

    private OrderTable createNotEmptyOrderTable(TableGroup tableGroup) {
        OrderTable orderTable = orderTableRepository.save(notEmptyOrderTable());
        orderTable.setTableGroup(tableGroup);
        return orderTableRepository.save(orderTable);
    }

    private OrderTable createEmptyOrderTable(TableGroup tableGroup) {
        OrderTable orderTable = orderTableRepository.save(notEmptyOrderTable());
        orderTable.setTableGroup(tableGroup);
        return orderTableRepository.save(orderTable);
    }

    private OrderTable changeEmptyOrder() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(true));
        return orderTableRepository.save(orderTable1);
    }

    private void 주문_식사중_상태_변경() {
        order.setOrderStatus(OrderStatus.MEAL);
        orderRepository.save(order);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    private void 주문_요리중_상태_변경() {
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
