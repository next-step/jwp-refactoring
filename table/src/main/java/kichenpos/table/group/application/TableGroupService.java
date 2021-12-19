package kichenpos.table.group.application;

import java.util.List;
import java.util.stream.Collectors;
import kichenpos.common.exception.NotFoundException;
import kichenpos.table.group.domain.TableGroup;
import kichenpos.table.group.domain.TableGroupRepository;
import kichenpos.table.group.ui.request.TableGroupRequest;
import kichenpos.table.group.ui.request.TableGroupRequest.OrderTableIdRequest;
import kichenpos.table.group.ui.response.TableGroupResponse;
import kichenpos.table.table.domain.OrderTable;
import kichenpos.table.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository tableRepository;

    public TableGroupService(TableGroupRepository tableGroupRepository,
        OrderTableRepository tableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        return TableGroupResponse.from(tableGroupRepository.save(newTableGroup(request)));
    }

    @Transactional
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
            .map(request -> tableRepository.table(request.getId()))
            .collect(Collectors.toList());
    }
}
