package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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

class TableServiceTest extends IntegrationServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @Test
    void create() {
        // given
        final OrderTable orderTable = makeOrderTable(1, false);

        // when
        final OrderTable savedTable = tableService.create(orderTable);

        // then
        assertThat(savedTable.getId()).isNotNull();
        assertThat(savedTable.getTableGroupId()).isNull();
        assertThat(savedTable.getNumberOfGuests()).isEqualTo(1);
        assertThat(savedTable.isEmpty()).isFalse();
    }

    @Test
    void list() {
        // given
        final OrderTable orderTable = makeOrderTable(1, false);
        tableService.create(orderTable);

        // when
        final List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).isNotEmpty();
        assertThat(orderTables.get(0).getId()).isNotNull();
        assertThat(orderTables.get(0).getTableGroupId()).isNull();
        assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(1);
        assertThat(orderTables.get(0).isEmpty()).isFalse();
    }

    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTable = makeOrderTable(1, false);
        final OrderTable savedTable = tableService.create(orderTable);

        // when
        savedTable.setEmpty(true);
        final OrderTable changedTable = tableService.changeEmpty(savedTable.getId(), savedTable);

        // then
        assertThat(changedTable.isEmpty()).isTrue();
    }

    @DisplayName("단체 지정이 된 테이블에 대해 상태를 변경하려고 할 때 예외 발생")
    @Test
    void changeTableInGroup() {
        // given
        final OrderTable orderTable1 = makeOrderTable(1, true);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable orderTable2 = makeOrderTable(1, true);
        final OrderTable savedTable2 = tableService.create(orderTable2);
        final TableGroup tableGroup = TableGroupServiceTest.makeTargetGroup(savedTable1, savedTable2);
        tableGroupService.create(tableGroup);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            savedTable1.setEmpty(true);
            tableService.changeEmpty(savedTable1.getId(), savedTable1);
        });
    }

    @DisplayName("테이블에 속한 주문 상태가 COOKING 이거나 MEAL인 경우에 상태를 변경하려고 하면 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeTableInInvalidStatus(final OrderStatus orderStatus) {
        // given
        final OrderTable orderTable = makeOrderTable(1, false);
        final OrderTable savedTable = tableService.create(orderTable);

        final Product product = ProductServiceTest.makeProduct("후라이드", new BigDecimal(16000));
        final Product savedProduct = productService.create(product);

        final MenuGroup menuGroup = MenuGroupServiceTest.makeMenuGroup("한마리메뉴");
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        final Menu menu =
            MenuServiceTest.makeMenu("후라이드치킨", new BigDecimal(16000), savedMenuGroup.getId(), savedProduct.getId(), 1);
        final Menu savedMenu = menuService.create(menu);

        final Order order = OrderServiceTest.makeOrder(savedTable.getId(), savedMenu.getId(), 1);
        final Order savedOrder = orderService.create(order);

        savedOrder.setOrderStatus(orderStatus.name());
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            savedTable.setEmpty(true);
            tableService.changeEmpty(savedTable.getId(), savedTable);
        });
    }

    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = makeOrderTable(1, false);
        final OrderTable savedTable = tableService.create(orderTable);

        // when
        savedTable.setNumberOfGuests(2);
        final OrderTable changedTable = tableService.changeNumberOfGuests(savedTable.getId(), savedTable);

        // then
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("음수로 방문한 손님 수를 변경하려고 할 때 예외 발생")
    @Test
    void changeNumberOfGuestsByNegative() {
        // given
        final OrderTable orderTable = makeOrderTable(1, false);
        final OrderTable savedTable = tableService.create(orderTable);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            savedTable.setNumberOfGuests(-1);
            tableService.changeNumberOfGuests(savedTable.getId(), savedTable);
        });
    }

    @DisplayName("빈 테이블의 방문한 손님 수를 변경하려고 할 때 예외 발생")
    @Test
    void changeNumberOfGuestsInEmptyTable() {
        // given
        final OrderTable orderTable = makeOrderTable(1, true);
        final OrderTable savedTable = tableService.create(orderTable);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            savedTable.setNumberOfGuests(2);
            tableService.changeNumberOfGuests(savedTable.getId(), savedTable);
        });
    }

    public static OrderTable makeOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
