package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("테이블 그룹 서비스")
public class TableGroupServiceTest extends ServiceTestBase {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuService menuService;
    private final TableService tableService;
    private final TableGroupService tableGroupService;
    private final OrderService orderService;

    @Autowired
    public TableGroupServiceTest(MenuGroupService menuGroupService, ProductService productService, MenuService menuService, TableService tableService, TableGroupService tableGroupService, OrderService orderService) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuService = menuService;
        this.tableService = tableService;
        this.tableGroupService = tableGroupService;
        this.orderService = orderService;
    }

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() {
        OrderTable savedTable = tableService.create(TableServiceTest.createTable());
        OrderTable savedTable2 = tableService.create(TableServiceTest.createTable());

        TableGroup savedTableGroup = tableGroupService.create(createTableGroup(savedTable, savedTable2));

        assertThat(savedTableGroup.getId()).isEqualTo(savedTableGroup.getId());
    }

    @DisplayName("테이블 리스트가 빈 테이블 그룹 생성")
    @Test
    void createWithEmptyList() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(createTableGroup()));
    }

    @DisplayName("테이블 리스트의 크기가 2보다 작은 테이블 그룹 생성")
    @Test
    void createWithUnder2() {
        OrderTable savedTable = tableService.create(TableServiceTest.createTable());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(createTableGroup(savedTable)));
    }

    @DisplayName("등록되지 않은 테이블이 포함 된 테이블 그룹 생성")
    @Test
    void createWithNotExistsTable() {
        OrderTable savedTable = tableService.create(TableServiceTest.createTable());
        OrderTable newTable = TableServiceTest.createTable();
        newTable.setId(99L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(createTableGroup(savedTable, newTable)));
    }

    @DisplayName("이용중인 테이블이 포함 된 테이블 그룹 생성")
    @Test
    void createWithUsing() {
        OrderTable savedTable = tableService.create(TableServiceTest.createTable());
        OrderTable savedTable2 = tableService.create(TableServiceTest.createTable());
        savedTable.setEmpty(false);
        tableService.changeEmpty(savedTable.getId(), savedTable);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(createTableGroup(savedTable, savedTable2)));
    }

    @DisplayName("다른 그룹에 속한 테이블이 포함 된 테이블 그룹 생성")
    @Test
    void createWithOtherGroup() {
        OrderTable savedTable = tableService.create(TableServiceTest.createTable());
        OrderTable savedTable2 = tableService.create(TableServiceTest.createTable());
        OrderTable savedTable3 = tableService.create(TableServiceTest.createTable());
        tableGroupService.create(createTableGroup(savedTable, savedTable2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(createTableGroup(savedTable2, savedTable3)));
    }

    @DisplayName("테이블 그룹 삭제")
    @Test
    void ungroup() {
        OrderTable savedTable = tableService.create(TableServiceTest.createTable());
        OrderTable savedTable2 = tableService.create(TableServiceTest.createTable());
        TableGroup tableGroup = tableGroupService.create(createTableGroup(savedTable, savedTable2));

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> tables =  tableService.list();
        for (OrderTable table : tables) {
            assertThat(table.getTableGroupId()).isNull();
        }
    }

    @DisplayName("이용중인 테이블이 포함 된 테이블 그룹 삭제")
    @Test
    void ungroupWithUse() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProductRequest> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        MenuResponse menu = menuService.create(MenuServiceTest.createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        OrderTable savedTable = tableService.create(TableServiceTest.createTable());
        OrderTable savedTable2 = tableService.create(TableServiceTest.createTable());
        TableGroup tableGroup = tableGroupService.create(createTableGroup(savedTable, savedTable2));
        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderServiceTest.createOrderLineItem(menu.getId(), 1L));
        orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderLineItems));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
    }

    public static TableGroup createTableGroup(OrderTable ... orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTables));

        return tableGroup;
    }
}
