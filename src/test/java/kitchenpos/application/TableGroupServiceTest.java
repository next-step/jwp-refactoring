package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;

    OrderTableResponse createdOrderTable;
    OrderTableResponse createdOrderTable2;

    @BeforeEach
    void setUp() {
        OrderTableRequest orderTable = new OrderTableRequest(true);
        createdOrderTable = tableService.create(orderTable);

        OrderTableRequest orderTable2 = new OrderTableRequest(true);
        createdOrderTable2 = tableService.create(orderTable2);
    }

    @Test
    @DisplayName("table group 생성")
    void table_group_create_test() {
        //given
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(createdOrderTable.getId(),
            createdOrderTable.getTableGroupId(), 3, false);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(createdOrderTable2.getId(),
            createdOrderTable2.getTableGroupId(), 3, false);
        TableGroupRequest tableGroupRequest = TABLE_GROUP_REQUEST_생성(orderTableRequest1, orderTableRequest2);

        //when
        TableGroupResponse createdTableGroup = TABLE_GROUP_생성_테스트(tableGroupRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdTableGroup.getId()).isNotNull();
        });
    }

    @Test
    @DisplayName("table group 삭제")
    void table_group_delete_test() {
        //given
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(createdOrderTable.getId(),
            createdOrderTable.getTableGroupId(), 3, false);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(createdOrderTable2.getId(),
            createdOrderTable2.getTableGroupId(), 3, false);
        TableGroupRequest tableGroupRequest = TABLE_GROUP_REQUEST_생성(orderTableRequest1, orderTableRequest2);

        TableGroupResponse createdTableGroup = TABLE_GROUP_생성_테스트(tableGroupRequest);
        //when
        TABLE_GROUP_삭제(createdTableGroup.getId());

        //then
        OrderTableResponse orderTableResponse = tableService.getOrderTableById(orderTableRequest1.getId());
        assertThat(orderTableResponse.getTableGroupId()).isNull();
    }

    private void TABLE_GROUP_삭제(Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
    }


    private TableGroupResponse TABLE_GROUP_생성_테스트(TableGroupRequest tableGroupRequest) {
        return tableGroupService.create(tableGroupRequest);
    }

    private TableGroupRequest TABLE_GROUP_REQUEST_생성(OrderTableRequest... createdOrderTable) {
        return new TableGroupRequest(Arrays.stream(createdOrderTable)
            .collect(Collectors.toList()));
    }
}
