package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

class TableGroupServiceTest extends IntegrationServiceTest {
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    private static OrderTable savedEmptyTable1;
    private static OrderTable savedEmptyTable2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        final OrderTable emptyTable1 = TableServiceTest.makeOrderTable(1, true);
        savedEmptyTable1 = tableService.create(emptyTable1);
        final OrderTable emptyTable2 = TableServiceTest.makeOrderTable(2, true);
        savedEmptyTable2 = tableService.create(emptyTable2);
    }

    @Test
    void create() {
        // given
        final TableGroup tableGroup = makeTargetGroup(savedEmptyTable1, savedEmptyTable2);

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables()).allMatch(it -> !it.isEmpty());
    }

    @DisplayName("2개 미만의 테이블에 대해 단체 지정을 하려할 때 예외 발생")
    @ParameterizedTest
    @MethodSource("provideInvalidNumberOfTables")
    void createByInvalidNumberOfTables(final List<OrderTable> orderTables) {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    private static Stream<List<OrderTable>> provideInvalidNumberOfTables() {
        return Stream.of(Collections.emptyList(), Collections.singletonList(savedEmptyTable1));
    }

    @DisplayName("비어있지 않거나, 이미 단체 지정이 된 테이블에 대햏 단체 지정을 하려할 때 예외 발생")
    @Test
    void createByInvalidStateTable() {
        // given
        final OrderTable notEmptyTable = TableServiceTest.makeOrderTable(1, false);
        final OrderTable savedNotEmptyTable = tableService.create(notEmptyTable);

        final TableGroup tableGroup = makeTargetGroup(savedEmptyTable1, savedEmptyTable2);
        tableGroupService.create(tableGroup);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final TableGroup newTableGroup = makeTargetGroup(savedEmptyTable1, savedEmptyTable2);
            tableGroupService.create(newTableGroup);
        });
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final TableGroup newTableGroup = makeTargetGroup(savedNotEmptyTable, savedEmptyTable1);
            tableGroupService.create(newTableGroup);
        });
    }

    @Test
    void ungroup() {
        // given
        final TableGroup tableGroup = makeTargetGroup(savedEmptyTable1, savedEmptyTable2);
        final TableGroup savedTargetGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(savedTargetGroup.getId());

        // then
        assertThat(tableService.list()).allMatch(it -> it.getTableGroupId() == null);
    }

    @DisplayName("테이블에 속한 주문 상태가 COOKING 이거나 MEAL인 경우에 그룹 해제를 하려고 하면 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroupTablesInInvalidStatus(final OrderStatus orderStatus) {
        // given
        final TableGroup tableGroup = makeTargetGroup(savedEmptyTable1, savedEmptyTable2);
        final TableGroup savedTargetGroup = tableGroupService.create(tableGroup);

        final Product product = ProductServiceTest.makeProduct("후라이드", new BigDecimal(16000));
        final Product savedProduct = productService.create(product);

        final MenuGroup menuGroup = MenuGroupServiceTest.makeMenuGroup("한마리메뉴");
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        final Menu menu =
            MenuServiceTest.makeMenu("후라이드치킨", new BigDecimal(16000), savedMenuGroup.getId(), savedProduct.getId(), 1);
        final Menu savedMenu = menuService.create(menu);

        final Order order = OrderServiceTest.makeOrder(savedEmptyTable1.getId(), savedMenu.getId(), 1);
        final Order savedOrder = orderService.create(order);

        savedOrder.setOrderStatus(orderStatus.name());
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(savedTargetGroup.getId()));
    }

    public static TableGroup makeTargetGroup(final OrderTable table1, final OrderTable table2) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(table1, table2));
        return tableGroup;
    }
}
