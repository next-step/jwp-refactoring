package kitchenpos.tablegroup.application;

import kitchenpos.ServiceTestBase;
import kitchenpos.exception.InvalidTableCountException;
import kitchenpos.exception.TableInUseException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.MenuServiceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.application.MenuGroupServiceTest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.OrderServiceTest;
import kitchenpos.order.dto.OrderMenuRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.ProductServiceTest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.TableServiceTest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
        TableResponse savedTable = tableService.create();
        TableResponse savedTable2 = tableService.create();

        TableGroupResponse savedTableGroup = tableGroupService.create(createTableGroup(savedTable, savedTable2));

        assertThat(savedTableGroup.getId()).isEqualTo(savedTableGroup.getId());
    }

    @DisplayName("테이블 리스트의 크기가 2보다 작은 테이블 그룹 생성")
    @Test
    void createWithUnder2() {
        TableResponse savedTable = tableService.create();

        assertThatExceptionOfType(InvalidTableCountException.class)
                .isThrownBy(() -> tableGroupService.create(createTableGroup(savedTable)));
    }

    @DisplayName("등록되지 않은 테이블이 포함 된 테이블 그룹 생성")
    @Test
    void createWithNotExistsTable() {
        TableResponse savedTable = tableService.create();
        TableResponse newTable = new TableResponse(99L, null, 0);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> tableGroupService.create(createTableGroup(savedTable, newTable)));
    }

    @DisplayName("이용중인 테이블이 포함 된 테이블 그룹 생성")
    @Test
    void createWithUsing() {
        TableResponse savedTable = tableService.create();
        TableResponse savedTable2 = tableService.create();

        tableService.update(savedTable.getId(), TableServiceTest.createRequest(4));

        assertThatExceptionOfType(TableInUseException.class)
                .isThrownBy(() -> tableGroupService.create(createTableGroup(savedTable, savedTable2)));
    }

    @DisplayName("다른 그룹에 속한 테이블이 포함 된 테이블 그룹 생성")
    @Test
    void createWithOtherGroup() {
        TableResponse savedTable = tableService.create();
        TableResponse savedTable2 = tableService.create();

        TableGroupResponse savedTableGroup = tableGroupService.create(createTableGroup(savedTable, savedTable2));

        TableResponse savedTable3 = tableService.create();

        assertThatExceptionOfType(TableInUseException.class)
                .isThrownBy(() -> tableGroupService.create(createTableGroup(savedTable2, savedTable3)));
    }

    @DisplayName("테이블 그룹 삭제")
    @Test
    void ungroup() {
        TableResponse savedTable = tableService.create();
        TableResponse savedTable2 = tableService.create();

        TableGroupResponse savedTableGroup = tableGroupService.create(createTableGroup(savedTable, savedTable2));

        tableGroupService.ungroup(savedTableGroup.getId());

        List<TableResponse> tables =  tableService.list();
        for (TableResponse table : tables) {
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
        TableResponse savedTable = tableService.create();
        TableResponse savedTable2 = tableService.create();
        TableGroupResponse savedTableGroup = tableGroupService.create(createTableGroup(savedTable, savedTable2));
        tableService.update(savedTable.getId(), TableServiceTest.createRequest(4));
        List<OrderMenuRequest> orderMenus = Collections.singletonList(OrderServiceTest.createOrderMenu(menu.getId(), 1L));
        orderService.create(OrderServiceTest.createOrder(savedTable.getId(), orderMenus));

        assertThatExceptionOfType(TableInUseException.class)
                .isThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    public static TableGroupRequest createTableGroup(TableResponse ... orderTables) {
        List<Long> tableIds = Stream.of(orderTables)
                .map(TableResponse::getId)
                .collect(Collectors.toList());
        return new TableGroupRequest(tableIds);
    }
}
