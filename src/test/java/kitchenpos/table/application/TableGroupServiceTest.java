package kitchenpos.table.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.ProductFixture;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

import static kitchenpos.table.application.TableGroupService.ORDER_STATUS_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.TableGroup.ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.TableGroup.ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TableGroupService")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private TableGroup tableGroup;
    private Order order;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("a"));
        Menu menu = menuRepository.save(new Menu(new Name("menu"), new Price(BigDecimal.ONE), menuGroup.getId(), Arrays.asList(new MenuProduct(null, ProductFixture.product(), 1L))));
        tableGroup = tableGroupDao.save(new TableGroup());
        OrderTable orderTable = createOrderTable(tableGroup);
        tableGroup.setOrderTables(Collections.singletonList(orderTable));
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 1));
        order = orderRepository.save(new Order(orderTable.getId(), orderLineItems));
        tableGroupService = new TableGroupService(orderRepository, orderTableDao, tableGroupDao);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        tableGroup.setOrderTables(Arrays.asList(makeNullTableGroup(createOrderTable(tableGroup)), makeNullTableGroup(createOrderTable(tableGroup))));
        TableGroup saveTableGroup = tableGroupService.create(tableGroup);
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
        TableGroup failTableGroup = tableGroupDao.save(new TableGroup());
        assertThatThrownBy(() -> tableGroupService.create(failTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void unGroup_success() {

        테이블_그룹_존재_검증(tableGroup);

        주문_완료_상태_변경();

        tableGroupService.ungroup(tableGroup.getId());

        테이블_그룹_해제_검증됨(tableGroup);
    }

    @DisplayName("테이블 그룹을 해제한다. / 요리중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_cooking() {

        테이블_그룹_존재_검증(tableGroup);

        주문_요리중_상태_변경();

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다. / 식사중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_meal() {

        테이블_그룹_존재_검증(tableGroup);

        주문_식사중_상태_변경();

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    private OrderTable makeNullTableGroup(OrderTable orderTable) {
        orderTable.setTableGroupId(null);
        return orderTableDao.save(orderTable);
    }

    private OrderTable createOrderTable(TableGroup tableGroup) {
        return orderTableDao.save(new OrderTable(tableGroup.getId(), true));
    }

    private void 주문_식사중_상태_변경() {
        order.setOrderStatus(OrderStatus.MEAL.name());
        orderRepository.save(order);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    private void 주문_요리중_상태_변경() {
        Order order1 = orderRepository.findById(order.getId()).get();
        assertThat(order1.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    private void 주문_완료_상태_변경() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderRepository.save(order);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    private void 테이블_그룹_존재_검증(TableGroup tableGroup) {
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNotNull();
        }
    }

    private void 테이블_그룹_해제_검증됨(TableGroup tableGroup) {
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNull();
        }
    }
}
