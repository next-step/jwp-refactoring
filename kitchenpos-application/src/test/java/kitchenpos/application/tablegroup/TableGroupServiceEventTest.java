package kitchenpos.application.tablegroup;

import kitchenpos.core.ordertable.domain.OrderTable;
import kitchenpos.core.ordertable.domain.OrderTableRepository;
import kitchenpos.application.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.application.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TableGroupServiceEventTest {
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupService tableGroupService;
    private List<Long> orderTableIds;
    private Long tableGroupId;

    @BeforeEach
    void setUp() {
        final List<OrderTable> orderTables = orderTableRepository.saveAll(Arrays.asList(
                OrderTable.of(13, true),
                OrderTable.of(13, true)
        ));
        orderTableIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
    }

    @DisplayName("단체지정시 주문 테이블 단제 지정의 아이디가 된다.")
    @Test
    void createAfter() {
        //given
        final TableGroupResponse tableGroupResponse = tableGroupService.create(new TableGroupCreateRequest(orderTableIds));
        tableGroupId = tableGroupResponse.getId();
        // when
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        // then
        assertThat(orderTables).extracting("tableGroupId").containsExactly(tableGroupId, tableGroupId);
    }

    @DisplayName("단체지정 해산시 주문 테이블 단제 지정의 아이디가 Null 이 된다.")
    @Test
    void ungroupAfter() {
        //given
        final TableGroupResponse tableGroupResponse = tableGroupService.create(new TableGroupCreateRequest(orderTableIds));
        tableGroupId = tableGroupResponse.getId();
        tableGroupService.ungroup(tableGroupId);
        // when
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        // then
        assertThat(orderTables).extracting("tableGroupId").containsExactly(null, null);
    }
}
