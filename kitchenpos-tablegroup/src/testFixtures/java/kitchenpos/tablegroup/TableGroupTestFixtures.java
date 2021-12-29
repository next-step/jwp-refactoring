package kitchenpos.tablegroup;

import static org.mockito.BDDMockito.given;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import java.util.List;
import java.util.stream.Collectors;

import org.mockito.ArgumentMatchers;

public class TableGroupTestFixtures {

    public static void 테이블그룹_저장_결과_모킹(TableGroupRepository tableGroupRepository,
        TableGroup tableGroup) {
        given(tableGroupRepository.save(ArgumentMatchers.any()))
            .willReturn(tableGroup);
    }

    public static TableGroupRequest convertToTableGroupRequest(List<OrderTable> orderTables) {
        List<OrderTableRequest> orderTableRequests = orderTables.stream()
            .map(orderTable -> new OrderTableRequest(orderTable.getId()))
            .collect(Collectors.toList());
        return new TableGroupRequest(orderTableRequests);
    }
}
