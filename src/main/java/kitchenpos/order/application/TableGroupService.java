package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.ui.request.TableGroupRequest;
import kitchenpos.order.ui.request.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.order.ui.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;

    public TableGroupService(TableGroupRepository tableGroupRepository,
        TableService tableService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        return TableGroupResponse.from(tableGroupRepository.save(newTableGroup(request)));
    }

    public void ungroup(long id) {
        tableGroup(id).ungroup();
    }

    private TableGroup tableGroup(long id) {
        return tableGroupRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(
                String.format("단체 지정된 그룹 id(%d)를 찾을 수 없습니다.", id)));
    }

    private TableGroup newTableGroup(TableGroupRequest request) {
        return TableGroup.from(orderTables(request.getOrderTables()));
    }

    private List<OrderTable> orderTables(List<OrderTableIdRequest> requests) {
        return requests.stream()
            .map(request -> orderTable(request.getId()))
            .collect(Collectors.toList());
    }

    private OrderTable orderTable(long id) {
        return tableService.findById(id);
    }
}
