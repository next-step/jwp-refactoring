package kitchenpos.table.application;

import static java.util.Collections.*;
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
import kitchenpos.table.dto.TableGroupResponse;

public class TableGroupServiceTest extends IntegrationServiceTest {
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

    private static OrderTableResponse savedEmptyTable1;
    private static OrderTableResponse savedEmptyTable2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        final OrderTableRequest emptyTable1 = TableServiceTest.makeOrderTableRequest(1, true);
        savedEmptyTable1 = tableService.create(emptyTable1);
        final OrderTableRequest emptyTable2 = TableServiceTest.makeOrderTableRequest(2, true);
        savedEmptyTable2 = tableService.create(emptyTable2);
    }

    @Test
    void create() {
        // given
        final List<Long> orderTableIds = Arrays.asList(savedEmptyTable1.getId(), savedEmptyTable2.getId());
        final TableGroupRequest tableGroupRequest = makeTableGroupRequest(orderTableIds);

        // when
        final TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables()).allMatch(it -> !it.isEmpty());
    }

    @DisplayName("2개 미만의 테이블에 대해 단체 지정을 하려할 때 예외 발생")
    @ParameterizedTest
    @MethodSource("provideInvalidNumberOfTableIds")
    void createByInvalidNumberOfTables(final List<Long> orderTableIds) {
        // given
        final TableGroupRequest tableGroupRequest = makeTableGroupRequest(orderTableIds);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    private static Stream<List<Long>> provideInvalidNumberOfTableIds() {
        return Stream.of(Collections.emptyList(), Collections.singletonList(savedEmptyTable1.getId()));
    }

    @DisplayName("비어있지 않거나, 이미 단체 지정이 된 테이블에 대햏 단체 지정을 하려할 때 예외 발생")
    @Test
    void createByInvalidStateTable() {
        // given
        final OrderTableRequest notEmptyTable = TableServiceTest.makeOrderTableRequest(1, false);
        final OrderTableResponse savedNotEmptyTable = tableService.create(notEmptyTable);
        final List<Long> orderTableIds = Arrays.asList(savedEmptyTable1.getId(), savedEmptyTable2.getId());
        final TableGroupRequest tableGroupRequest = makeTableGroupRequest(orderTableIds);
        tableGroupService.create(tableGroupRequest);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final List<Long> tableIds = Arrays.asList(savedEmptyTable1.getId(), savedEmptyTable2.getId());
            tableGroupService.create(makeTableGroupRequest(tableIds));
        });
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final List<Long> tableIds = Arrays.asList(savedNotEmptyTable.getId(), savedEmptyTable2.getId());
            tableGroupService.create(makeTableGroupRequest(tableIds));
        });
    }

    @Test
    void ungroup() {
        // given
        final List<Long> orderTableIds = Arrays.asList(savedEmptyTable1.getId(), savedEmptyTable2.getId());
        final TableGroupRequest tableGroupRequest = makeTableGroupRequest(orderTableIds);
        final TableGroupResponse savedTargetGroup = tableGroupService.create(tableGroupRequest);

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
        final List<Long> orderTableIds = Arrays.asList(savedEmptyTable1.getId(), savedEmptyTable2.getId());
        final TableGroupRequest tableGroupRequest = makeTableGroupRequest(orderTableIds);
        final TableGroupResponse savedTargetGroup = tableGroupService.create(tableGroupRequest);

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
        final OrderRequest order = new OrderRequest(savedEmptyTable1.getId(), orderStatus, orderLineItemRequests);
        orderService.create(order);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(savedTargetGroup.getId()));
    }

    public static TableGroupRequest makeTableGroupRequest(final List<Long> orderTableIds) {
        return new TableGroupRequest(orderTableIds);
    }
}
