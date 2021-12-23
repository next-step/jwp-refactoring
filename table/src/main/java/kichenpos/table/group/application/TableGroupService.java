package kichenpos.table.group.application;

import java.util.List;
import java.util.stream.Collectors;
import kichenpos.table.group.domain.TableGroup;
import kichenpos.table.group.domain.TableGroupCommandService;
import kichenpos.table.group.ui.request.TableGroupRequest;
import kichenpos.table.group.ui.request.TableGroupRequest.OrderTableIdRequest;
import kichenpos.table.group.ui.response.TableGroupResponse;
import kichenpos.table.table.domain.OrderTable;
import kichenpos.table.table.domain.TableQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupCommandService groupCommandService;
    private final TableQueryService tableQueryService;

    public TableGroupService(
        TableGroupCommandService groupCommandService,
        TableQueryService tableQueryService) {
        this.groupCommandService = groupCommandService;
        this.tableQueryService = tableQueryService;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        return TableGroupResponse.from(groupCommandService.save(newTableGroup(request)));
    }

    @Transactional
    public void ungroup(long id) {
        groupCommandService.ungroup(id);
    }

    private TableGroup newTableGroup(TableGroupRequest request) {
        return TableGroup.from(orderTables(request.getOrderTables()));
    }

    private List<OrderTable> orderTables(List<OrderTableIdRequest> requests) {
        return requests.stream()
            .map(request -> tableQueryService.table(request.getId()))
            .collect(Collectors.toList());
    }
}
