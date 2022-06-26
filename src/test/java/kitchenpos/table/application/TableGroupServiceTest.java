package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.util.dto.SaveMenuDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderTableTestFixture orderTableTestFixture;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTable1 = this.orderTableRepository.save(new OrderTable(0, true));
        orderTable2 = this.orderTableRepository.save(new OrderTable(0, true));
    }

    @Test
    @DisplayName("테이블 그룹이 정상적으로 생성된다.")
    void createTableGroup() {
        TableGroupResponse tableGroup = this.tableGroupService.create(new TableGroupRequest(OrderTableRequest.of(orderTable1, orderTable2)));

        assertThat(tableGroup.getId()).isNotNull();
        assertThat(tableGroup.getOrderTables()).hasSize(2);
        assertTrue(tableGroup.getOrderTables().stream().anyMatch(OrderTableResponse::isEmpty));
    }

    @Test
    @DisplayName("테이블 정보가 존재하지 않을 경우 예외를 던진다.")
    void createFail_orderTableNullOrEmpty() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(100L, null, 0, true);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroupRequest(Collections.emptyList())));
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroupRequest(Collections.singletonList(orderTableRequest))));
    }

    @Test
    @DisplayName("테이블 중 비어있지 않은 테이블이 존재할 경우 생성될 수 없다.")
    void createFail_orderTableEmptyStatus() {
        OrderTable orderTable = this.orderTableRepository.save(new OrderTable(4, false));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroupRequest(OrderTableRequest.of(orderTable))));
    }

    @Test
    @DisplayName("테이블 중 이미 테이블 그룹에 속한 테이블이 존재할 경우 생성될 수 없다.")
    void createFail_alreadyContainTableGroup() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroupRequest(OrderTableRequest.of(this.orderTable1))));
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void ungroup() {
        TableGroupResponse tableGroupResponse = this.tableGroupService.create(new TableGroupRequest(OrderTableRequest.of(orderTable1, orderTable2)));
        this.tableGroupService.ungroup(tableGroupResponse.getId());

        List<OrderTable> orderTables = this.orderTableRepository.findAll();
        assertTrue(orderTables.stream().anyMatch(orderTable -> orderTable.getTableGroup() == null));
    }

    @Test
    @DisplayName("테이블 중 식사중이거나 조리중인 테이블이 있다면 해제할 수 없다.")
    void ungroupFail() {
        TableGroupResponse tableGroupResponse = this.tableGroupService.create(new TableGroupRequest(OrderTableRequest.of(orderTable1, orderTable2)));

        MenuProduct menuProduct1 = new MenuProduct(new Product("후라이드", BigDecimal.valueOf(16000)), 1);
        MenuProduct menuProduct2 = new MenuProduct(new Product("양념치킨", BigDecimal.valueOf(16000)), 1);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        SaveMenuDto saveMenuDto = new SaveMenuDto(menuProducts, new MenuGroup("메뉴 그룹"), "메뉴", 32000);
        orderTableTestFixture.메뉴_만들고_주문하기(saveMenuDto, 1, orderTable1);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()));
    }

}
