package kitchenpos.table.service;

import kitchenpos.IntegrationTest;
import kitchenpos.order.service.OrderService;
import kitchenpos.order.util.OrderRequestBuilder;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.util.TableGroupRequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderTableService orderTableService;
    @Autowired
    private OrderService orderService;

    @DisplayName("주문 테이블을 그룹화 할수 있다.")
    @Test
    void createTableGroup() {
        TableGroupRequest tableGroupRequest =
                new TableGroupRequestBuilder()
                        .addOrderTable(1L)
                        .addOrderTable(2L)
                        .build();
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
        assertThat(tableGroupResponse.getOrderTables()).hasSize(2);
    }

    @DisplayName("테이블 그룹을 2개 이상이어야 한다.")
    @Test
    void tableGroupSizeGreaterThanOne() {
        TableGroupRequest tableGroupRequest =
                new TableGroupRequestBuilder()
                        .addOrderTable(1L)
                        .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹은 빈 테이블 상태어야 한다.")
    @Test
    void tableGroupIsNotEmpty() {
        // given
        orderTableService.changeEmpty(1L, false);
        TableGroupRequest tableGroupRequest = new TableGroupRequestBuilder()
                .addOrderTable(1L)
                .addOrderTable(2L)
                .build();

        // when then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 삭제 할 수 있다.")
    @Test
    void deleteTableGroup() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequestBuilder()
                .addOrderTable(1L)
                .addOrderTable(2L)
                .build();
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // when
        tableGroupService.ungroup(tableGroupResponse.getId());

        // then
        assertThat(orderTableService.findById(1L).hasTableGroup()).isFalse();
        assertThat(orderTableService.findById(2L).hasTableGroup()).isFalse();
    }

    @DisplayName("주문 상태가 조리, 식사중일때 단체 지정을 해제 할 수 없다.")
    @Test
    void expectedExceptionOrderStatus() {
        // given
        orderService.create(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build());

        TableGroupResponse tableGroupResponse = tableGroupService.create(new TableGroupRequestBuilder()
                .addOrderTable(1L)
                .addOrderTable(2L)
                .build());

        // when then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 완료되지 않아 그룹 해제가 불가능합니다.");

    }
}
