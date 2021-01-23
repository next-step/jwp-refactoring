package kitchenpos.application;

import kitchenpos.exception.TableInUseException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderMenuRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


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
        TableResponse savedOrderTable = tableService.create();

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("고객 수 변경")
    @Test
    void changeNumberOfGuest() {
        TableResponse savedTable = tableService.create();
        TableResponse updatedTable = tableService.update(savedTable.getId(), createRequest(4));

        assertThat(updatedTable.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("그룹이 지어진 테이블의 고객 수를 0으로 변경")
    @Test
    void changeNumberOfGuestWithGroup() {
        TableResponse savedTable = tableService.create();
        TableResponse savedTable2 = tableService.create();

        tableGroupService.create(TableGroupServiceTest.createTableGroup(savedTable, savedTable2));

        assertThatExceptionOfType(TableInUseException.class)
                .isThrownBy(() -> tableService.update(savedTable.getId(), createRequest(0)));
    }

    @DisplayName("이용중인 테이블의 고객 수를 0으로 변경")
    @Test
    void changeEmptyWithUsing() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProductRequest> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        MenuResponse menu = menuService.create(MenuServiceTest.createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        TableResponse orderTable = tableService.create();
        tableService.update(orderTable.getId(), createRequest(4));
        List<OrderMenuRequest> orderMenus = Collections.singletonList(OrderServiceTest.createOrderMenu(menu.getId(), 1L));
        orderService.create(OrderServiceTest.createOrder(orderTable.getId(), orderMenus));

        assertThatExceptionOfType(TableInUseException.class)
                .isThrownBy(() -> tableService.update(orderTable.getId(), createRequest(0)));
    }

    @DisplayName("등록되지 않은 테이블의 고객 수 변경")
    @Test
    void changeNumberOfGuestWithNotExists() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> tableService.update(99L, createRequest(4)));
    }

    public static TableRequest createRequest(int numberOfGuests) {
        return new TableRequest(numberOfGuests);
    }
}
