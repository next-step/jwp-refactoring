package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("단체 지정 서비스에 관련한 기능")
@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuService menuService;

    @DisplayName("`단체 지정`을 생성한다.")
    @Test
    void createTableGroup() {
        // Given
        OrderTableResponse savedOrderTable1 = tableService.create(new OrderTableRequest(3, true));
        OrderTableResponse savedOrderTable2 = tableService.create(new OrderTableRequest(5, true));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.toOrderTable(), savedOrderTable2.toOrderTable()));

        // When
        TableGroupResponse actual = tableGroupService.create(request);
        OrderTable orderTable1 = tableService.findById(savedOrderTable1.getId());
        OrderTable orderTable2 = tableService.findById(savedOrderTable2.getId());

        // Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTables()).containsAnyElementsOf(Arrays.asList(orderTable1, orderTable2)),
                () -> assertThat(actual.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("`단체 지정`으로 등록할 `주문 테이블`이 2개 미만이면 생성할 수 없다.")
    @Test
    void exceptionToCreateTableGroupWithZeroOrOneOrderTable() {
        // Given
        OrderTableResponse savedOrderTable = tableService.create(new OrderTableRequest(3, true));
        TableGroupRequest request = new TableGroupRequest(Collections.singletonList(savedOrderTable.toOrderTable()));

        // When & Then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`단체 지정`할 `주문 테이블`은 비어있어야한다.")
    @Test
    void exceptionToCreateTableGroupWithNonemptyOrderTable() {
        // Given
        OrderTableResponse savedOrderTable1 = tableService.create(new OrderTableRequest(3, false));
        OrderTableResponse savedOrderTable2 = tableService.create(new OrderTableRequest(5, false));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.toOrderTable(), savedOrderTable2.toOrderTable()));

        // When & Then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`단체 지정`할 `주문 테이블`이 `단체 지정`되어있으면 생성할 수 없다.")
    @Test
    void exceptionToCreateTableGroupWithRegisteredOrderTable() {
        // Given
        OrderTableResponse savedOrderTable1 = tableService.create(new OrderTableRequest(3, true));
        OrderTableResponse savedOrderTable2 = tableService.create(new OrderTableRequest(5, true));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.toOrderTable(), savedOrderTable2.toOrderTable()));
        tableGroupService.create(request);

        // When & Then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`단체 지정`을 해제한다.")
    @Test
    void ungroupTableGroup() {
        // Given
        OrderTableResponse savedOrderTable1 = tableService.create(new OrderTableRequest(3, true));
        OrderTableResponse savedOrderTable2 = tableService.create(new OrderTableRequest(5, true));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.toOrderTable(), savedOrderTable2.toOrderTable()));
        TableGroupResponse tableGroup = tableGroupService.create(request);

        // When
        tableGroupService.ungroup(tableGroup.getId());
        OrderTable orderTable1 = tableService.findById(savedOrderTable1.getId());
        OrderTable orderTable2 = tableService.findById(savedOrderTable2.getId());

        // Then
        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("`단체 지정`된 `주문 테이블`에서 `주문 상태`가 'COOKING' 이나 'MEAL' 이면 해제할 수 없다.")
    @Test
    void exceptionToUngroupTableGroup() {
        // Given
        OrderTableResponse savedOrderTable1 = tableService.create(new OrderTableRequest(0, true));
        OrderTableResponse savedOrderTable2 = tableService.create(new OrderTableRequest(0, true));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.toOrderTable(), savedOrderTable2.toOrderTable()));
        TableGroupResponse savedTableGroup = tableGroupService.create(request);

        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderLineItem menuParams = new OrderLineItem();
        menuParams.setMenuId(추천메뉴.getId());
        menuParams.setQuantity(1);
        OrderRequest orderRequest = new OrderRequest(savedOrderTable1.getId(), Collections.singletonList(menuParams));
        orderService.create(orderRequest);

        // When & Then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
