package kitchenpos.table.service;

import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    private List<OrderTableRequest> orderTables;

    @BeforeEach
    public void setup() {
        orderTables = new ArrayList<>();
    }

    @Test
    @DisplayName("단체를 지정 한다")
    public void createOrderTableGroup() {
       // given
        orderTables.add(new OrderTableRequest(5L, 0, true));
        orderTables.add(new OrderTableRequest(6L, 0, true));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(LocalDateTime.now(), orderTables);

        // when
        TableGroupResponse createTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(createTableGroup.getId()).isNotNull();
        assertThat(createTableGroup.getOrderTables()).isNotEmpty();
    }

    @Test
    @DisplayName("단체 지정 실패 - 주문 테이블이 하나 일 경우")
    public void createOrderTableGroupFailByOneOrderTable() {
        // given
        orderTables.add(new OrderTableRequest(5L, 0, true));
        TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(), orderTables);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 지정 실패 - 주문 테이블이 empty가 아닐 경우")
    public void createOrderTableGroupFailByOnderTableIsNotEmpty() {
        // given
        orderTables.add(new OrderTableRequest(3L, 0, false));
        orderTables.add(new OrderTableRequest(6L, 0, false));
        TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(), orderTables);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 지정 실패 - 이미 단체 지정이 된 테이블")
    public void createOrderTableGroupFailByAlreadyExistsTableGroup() {
        // given
        orderTables.add(new OrderTableRequest(9L, 0, false));
        orderTables.add(new OrderTableRequest(10L, 0, false));
        TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(), orderTables);
        tableGroupService.create(tableGroup);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 지정 실패 - 기존 데이터베이스에 존재하지 않는 테이블을 포함하여 단체 지정 할 경우")
    public void createOrderTableGroupFailByNotExistsTable() {
        // given
        orderTables.add(new OrderTableRequest(10L, 0, false));
        orderTables.add(new OrderTableRequest(11L, 0, false));
        TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(), orderTables);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 지정을 해제 한다")
    public void deleteOrderTableGroup() {
        // given
        OrderTableRequest orderTable = new OrderTableRequest(7L, 0, true);
        orderTables.add(orderTable);
        orderTables.add(new OrderTableRequest(8L, 0, true));
        TableGroupResponse createTableGroup = tableGroupService.create(new TableGroupRequest(LocalDateTime.now(), orderTables));

        // when
        tableGroupService.ungroup(createTableGroup.getId());

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

}
