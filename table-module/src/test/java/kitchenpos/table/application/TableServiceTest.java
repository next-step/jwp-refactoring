package kitchenpos.table.application;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationServiceTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;

public class TableServiceTest extends IntegrationServiceTest {
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
        final OrderTableRequest orderTable = makeOrderTableRequest(1, false);

        // when
        final OrderTableResponse savedTable = tableService.create(orderTable);

        // then
        assertThat(savedTable.getId()).isNotNull();
        assertThat(savedTable.getTableGroupId()).isNull();
        assertThat(savedTable.getNumberOfGuests()).isEqualTo(1);
        assertThat(savedTable.isEmpty()).isFalse();
    }

    @Test
    void list() {
        // given
        final OrderTableRequest orderTable = makeOrderTableRequest(1, false);
        tableService.create(orderTable);

        // when
        final List<OrderTableResponse> orderTables = tableService.list();

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
        final OrderTableRequest orderTable = makeOrderTableRequest(1, false);
        final OrderTableResponse savedTable = tableService.create(orderTable);
        final OrderTableRequest emptyTable = makeOrderTableRequest(1, true);

        // when
        final OrderTableResponse changedTable = tableService.changeEmpty(savedTable.getId(), emptyTable);

        // then
        assertThat(changedTable.isEmpty()).isTrue();
    }

    @DisplayName("단체 지정이 된 테이블에 대해 상태를 변경하려고 할 때 예외 발생")
    @Test
    void changeTableInGroup() {
        // given
        final OrderTableRequest orderTable1 = makeOrderTableRequest(1, true);
        final OrderTableResponse savedTable1 = tableService.create(orderTable1);
        final OrderTableRequest orderTable2 = makeOrderTableRequest(1, true);
        final OrderTableResponse savedTable2 = tableService.create(orderTable2);
        final List<Long> orderTableIds = Arrays.asList(savedTable1.getId(), savedTable2.getId());
        final TableGroupRequest tableGroupRequest = TableGroupServiceTest.makeTableGroupRequest(orderTableIds);
        tableGroupService.create(tableGroupRequest);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableService.changeEmpty(savedTable1.getId(), makeOrderTableRequest(1, true)));
    }

    @DisplayName("테이블에 속한 주문 상태가 COOKING 이거나 MEAL인 경우에 상태를 변경하려고 하면 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeTableInInvalidStatus(final OrderStatus orderStatus) {
        // given
        final OrderTableRequest orderTable = makeOrderTableRequest(1, false);
        final OrderTableResponse savedTable = tableService.create(orderTable);

        final ProductRequest product = new ProductRequest("후라이드", new BigDecimal(16000));
        final ProductResponse savedProduct = productService.create(product);

        final MenuGroupRequest menuGroup = new MenuGroupRequest("한마리메뉴");
        final MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroup);

        final List<MenuProductRequest> menuProductRequests =
            singletonList(new MenuProductRequest(savedProduct.getId(), 1));
        final MenuRequest menu =
            new MenuRequest("후라이드치킨", new BigDecimal(16000), savedMenuGroup.getId(), menuProductRequests);
        final MenuResponse savedMenu = menuService.create(menu);

        final List<OrderLineItemRequest> orderLineItemRequests =
            singletonList(new OrderLineItemRequest(savedMenu.getId(), 1));
        final OrderRequest order = new OrderRequest(savedTable.getId(), orderStatus, orderLineItemRequests);
        orderService.create(order);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableService.changeEmpty(savedTable.getId(), makeOrderTableRequest(1, true)));
    }

    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTableRequest orderTable = makeOrderTableRequest(1, false);
        final OrderTableResponse savedTable = tableService.create(orderTable);

        // when
        final OrderTableResponse changedTable =
            tableService.changeNumberOfGuests(savedTable.getId(), makeOrderTableRequest(2, false));

        // then
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("음수로 방문한 손님 수를 변경하려고 할 때 예외 발생")
    @Test
    void changeNumberOfGuestsByNegative() {
        // given
        final OrderTableRequest orderTable = makeOrderTableRequest(1, false);
        final OrderTableResponse savedTable = tableService.create(orderTable);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), makeOrderTableRequest(-1, false)));
    }

    @DisplayName("빈 테이블의 방문한 손님 수를 변경하려고 할 때 예외 발생")
    @Test
    void changeNumberOfGuestsInEmptyTable() {
        // given
        final OrderTableRequest orderTable = makeOrderTableRequest(1, true);
        final OrderTableResponse savedTable = tableService.create(orderTable);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), makeOrderTableRequest(2, true)));
    }

    public static OrderTableRequest makeOrderTableRequest(final int numberOfGuests, final boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }
}
