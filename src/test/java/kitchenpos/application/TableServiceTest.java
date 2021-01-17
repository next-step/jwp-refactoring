package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("테이블 서비스")
public class TableServiceTest extends ServiceTestBase {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuService menuService;
    private final TableService tableService;
    private final TableGroupService tableGroupService;
    private final OrderService orderService;

    @Autowired
    public TableServiceTest(MenuGroupService menuGroupService, ProductService productService, MenuService menuService, TableService tableService, TableGroupService tableGroupService, OrderService orderService) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuService = menuService;
        this.tableService = tableService;
        this.tableGroupService = tableGroupService;
        this.orderService = orderService;
    }

    @DisplayName("테이블 생성")
    @Test
    void create() {
        OrderTable savedOrderTable = tableService.create(createTable());

        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("착석 상태 변경")
    @Test
    void changeEmpty() {
        OrderTable savedOrderTable = tableService.create(createTable());

        savedOrderTable.setEmpty(false);

        OrderTable updatedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable);
        assertThat(updatedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("등록되지 않은 테이블의 착석 상태 변경")
    @Test
    void changeEmptyWithNotExists() {
        OrderTable newTable = new OrderTable();
        newTable.setId(99L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(newTable.getId(), newTable));
    }

    @DisplayName("그룹이 있는 테이블의 착석 상태 변경")
    @Test
    void changeEmptyWithTableGroup() {
        OrderTable savedTable = tableService.create(createTable());
        OrderTable savedTable2 = tableService.create(createTable());
        tableGroupService.create(TableGroupServiceTest.createTableGroup(savedTable, savedTable2));

        savedTable.setEmpty(false);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(savedTable.getId(), savedTable));
    }

    @DisplayName("테이블을 이용중인 경우의 착석 상태 변경")
    @Test
    void changeEmptyWithUsing() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProduct> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        Menu menu = menuService.create(MenuServiceTest.createMenu("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        OrderTable orderTable = tableService.create(createTable());
        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderServiceTest.createOrderLineItem(menu.getId(), 1L));
        orderTable.setEmpty(false);
        tableService.changeEmpty(orderTable.getId(), orderTable);
        orderService.create(OrderServiceTest.createOrder(orderTable.getId(), orderLineItems));

        orderTable.setEmpty(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable));
    }

    @DisplayName("고객 수 변경")
    @Test
    void changeNumberOfGuest() {
        OrderTable savedOrderTable = tableService.create(createTable());
        savedOrderTable.setEmpty(false);
        tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable);

        savedOrderTable.setNumberOfGuests(4);
        OrderTable updatedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable);

        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("고객 수를 음수로 변경")
    @Test
    void changeNumberOfGuestWithNegative() {
        OrderTable savedOrderTable = tableService.create(createTable());
        savedOrderTable.setEmpty(false);
        tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable);

        savedOrderTable.setNumberOfGuests(-1);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable));
    }

    @DisplayName("등록되지 않은 테이블의 고객 수 변경")
    @Test
    void changeNumberOfGuestWithNotExists() {
        OrderTable table = createTable();
        table.setId(99L);
        table.setEmpty(false);
        table.setNumberOfGuests(4);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), table));
    }

    public static OrderTable createTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        return orderTable;
    }
}
