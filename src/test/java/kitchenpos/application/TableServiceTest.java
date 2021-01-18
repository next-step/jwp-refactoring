package kitchenpos.application;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.*;
import kitchenpos.exception.TableInUseException;
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

    @DisplayName("그룹이 지어진 테이블의 고객 수 변경")
    @Test
    void changeNumberOfGuestWithGroup() {
        TableResponse savedTable = tableService.create();
        TableResponse savedTable2 = tableService.create();

        tableGroupService.create(savedTable.getId(), savedTable2.getId());

        assertThatExceptionOfType(TableInUseException.class)
                .isThrownBy(() -> tableService.update(savedTable.getId(), createRequest(4)));
    }

    @DisplayName("테이블을 이용중인 경우의 착석 상태 변경")
    @Test
    void changeEmptyWithUsing() {
        MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        ProductResponse product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
        List<MenuProductRequest> menuProducts = Collections.singletonList(MenuServiceTest.createMenuProduct(product.getId(), 2L));
        MenuResponse menu = menuService.create(MenuServiceTest.createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        TableResponse orderTable = tableService.create();
        tableService.update(orderTable.getId(), createRequest(4));
        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderServiceTest.createOrderLineItem(menu.getId(), 1L));
        orderService.create(OrderServiceTest.createOrder(orderTable.getId(), orderLineItems));

        assertThatExceptionOfType(TableInUseException.class)
                .isThrownBy(() -> tableService.update(orderTable.getId(), createRequest(0)));
    }


    @DisplayName("등록되지 않은 테이블의 고객 수 변경")
    @Test
    void changeNumberOfGuestWithNotExists() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> tableService.update(99L, createRequest(4)));
    }

    private TableRequest createRequest(int numberOfGuests) {
        return TableRequest.builder()
                .numberOfGuests(numberOfGuests)
                .build();
    }
}
