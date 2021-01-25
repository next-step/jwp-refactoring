package kitchenpos.tablegroup.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.exception.InvalidOrderTablesException;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.InvalidGroupException;
import kitchenpos.tablegroup.exception.InvalidUnGroupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupServiceTest extends BaseServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문 테이블들을 단체 지정할 수 있다.")
    @Test
    void createTableGroup() {
        OrderTableRequest orderTable1 = new OrderTableRequest(빈_orderTable_id1, 0, true);
        OrderTableRequest orderTable2 = new OrderTableRequest(빈_orderTable_id2, 0, true);

        TableGroupResponse result = tableGroupService.create(new TableGroupRequest(1L, Arrays.asList(orderTable1, orderTable2)));

        assertThat(result.getCreatedDate()).isNotNull();
        assertThat(result.getOrderTableResponses().get(0).getTableGroupId()).isEqualTo(1L);
        assertThat(result.getOrderTableResponses().get(0).isEmpty()).isFalse();
        assertThat(result.getOrderTableResponses().get(1).getTableGroupId()).isEqualTo(1L);
        assertThat(result.getOrderTableResponses().get(1).isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 갯수가 2개 미만일 경우 지정할 수 없다.")
    @Test
    void createTableGroupException1() {
        OrderTableRequest orderTable = new OrderTableRequest(빈_orderTable_id1, 0, true);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(1L, Collections.singletonList(orderTable))))
                .isInstanceOf(InvalidOrderTablesException.class)
                .hasMessage("주문 테이블은 2개 이상이어야 합니다.");
    }

    @DisplayName("주문 테이블들의 수와 저장되어 있는 주문 테이블의 수가 일치하지 않으면 지정할 수 없다.")
    @Test
    void createTableGroupException2() {
        OrderTableRequest orderTable1 = new OrderTableRequest(빈_orderTable_id1, 0, true);
        OrderTableRequest orderTable2 = new OrderTableRequest(등록되어_있지_않은_orderTable_id, 0, true);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(1L, Arrays.asList(orderTable1, orderTable2))))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("등록되어 있지 않은 주문 테이블이 있습니다.");
    }

    @DisplayName("주문 테이블이 빈 테이블이 아닌 경우 지정할 수 없다.")
    @Test
    void createTableGroupException3() {
        OrderTableRequest orderTable1 = new OrderTableRequest(빈_orderTable_id1, 0, true);
        OrderTableRequest orderTable2 = new OrderTableRequest(비어있지_않은_orderTable_id, 0, true);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(1L, Arrays.asList(orderTable1, orderTable2))))
                .isInstanceOf(InvalidGroupException.class)
                .hasMessage("빈 테이블이 아니거나 단체 지정일 경우 단체 지정을 할 수 없습니다.");
    }

    @DisplayName("주문 테이블이 이미 단체 지정되어 있는 경우 지정할 수 없다.")
    @Test
    void createTableGroupException4() {
        OrderTableRequest orderTable1 = new OrderTableRequest(빈_orderTable_id1, 0, true);
        OrderTableRequest orderTable2 = new OrderTableRequest(빈_orderTable_id2, 0, true);
        tableGroupService.create(new TableGroupRequest(1L, Arrays.asList(orderTable1, orderTable2)));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(1L, Arrays.asList(orderTable1, orderTable2))))
                .isInstanceOf(InvalidGroupException.class)
                .hasMessage("빈 테이블이 아니거나 단체 지정일 경우 단체 지정을 할 수 없습니다.");
    }

    @Test
    void ungroupTableGroup() {
        OrderTableRequest orderTable1 = new OrderTableRequest(빈_orderTable_id1, 0, true);
        OrderTableRequest orderTable2 = new OrderTableRequest(빈_orderTable_id2, 0, true);
        TableGroupResponse tableGroup = tableGroupService.create(new TableGroupRequest(1L, Arrays.asList(orderTable1, orderTable2)));

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        orderTables.forEach(orderTable -> assertThat(orderTable.getTableGroupId()).isNull());
    }

    @DisplayName("들어간 주문들 중에서 주문 상태가 조리 또는 식사가 하나라도 있을 경우 해제할 수 없다.")
    @Test
    void ungroupTableGroupException() {
        OrderTableRequest orderTable1 = new OrderTableRequest(빈_orderTable_id1, 0, true);
        OrderTableRequest orderTable2 = new OrderTableRequest(빈_orderTable_id2, 0, true);
        TableGroupResponse tableGroup = tableGroupService.create(new TableGroupRequest(1L, Arrays.asList(orderTable1, orderTable2)));

        OrderRequest order = new OrderRequest(1L, orderTable1.getId(), Collections.singletonList(new OrderLineItemRequest(등록된_menu_id, 2)));
        orderService.create(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(InvalidUnGroupException.class)
                .hasMessage("주문 상태가 조리 또는 식사일 경우 해제할 수 없습니다.");
    }
}
