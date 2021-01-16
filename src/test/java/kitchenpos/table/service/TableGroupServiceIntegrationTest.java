package kitchenpos.table.service;

import kitchenpos.IntegrationTest;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.util.OrderTableBuilder;
import kitchenpos.table.util.TableGroupRequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private TableGroupServiceJpa tableGroupServiceJpa;
    @Autowired
    private OrderTableServiceJpa orderTableServiceJpa;

    @DisplayName("주문 테이블을 그룹화 할수 있다.")
    @Test
    void createTableGroup() {
        TableGroupRequest tableGroupRequest =
                new TableGroupRequestBuilder()
                        .addOrderTable(1L)
                        .addOrderTable(2L)
                        .build();
        TableGroupResponse tableGroupResponse = tableGroupServiceJpa.create(tableGroupRequest);
        assertThat(tableGroupResponse.getOrderTables()).hasSize(2);
    }

    @DisplayName("테이블 그룹을 2개 이상이어야 한다.")
    @Test
    void tableGroupSizeGreaterThanOne() {
        TableGroupRequest tableGroupRequest =
                new TableGroupRequestBuilder()
                        .addOrderTable(1L)
                        .build();

        assertThatThrownBy(() -> tableGroupServiceJpa.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹은 빈 테이블 상태어야 한다.")
    @Test
    void tableGroupIsNotEmpty() {
        // given
        orderTableServiceJpa.changeEmpty(1L,
                new OrderTableBuilder()
                        .withEmpty(false)
                        .requestBuild());
        TableGroupRequest tableGroupRequest = new TableGroupRequestBuilder()
                .addOrderTable(1L)
                .addOrderTable(2L)
                .build();

        // when then
        assertThatThrownBy(() -> tableGroupServiceJpa.create(tableGroupRequest))
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
        TableGroupResponse tableGroupResponse = tableGroupServiceJpa.create(tableGroupRequest);

        // when
        tableGroupServiceJpa.ungroup(tableGroupResponse.getId());

        // then
        assertThat(orderTableServiceJpa.findById(1L).hasTableGroup()).isFalse();
        assertThat(orderTableServiceJpa.findById(2L).hasTableGroup()).isFalse();
    }
}
