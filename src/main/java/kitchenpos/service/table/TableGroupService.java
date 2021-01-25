package kitchenpos.service.table;

import kitchenpos.domain.table.OrderTableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final TableService tableService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
    }

    public TableGroupResponse applyGroup(TableGroupRequest tableGroupRequest) {
        List<Long> ids = tableGroupRequest.getTableIds();
        OrderTableGroup orderTableGroup = new OrderTableGroup(tableService.findOrderTables(ids));
        orderTableGroup.applyGroup(ids.size());
        return TableGroupResponse.of(tableGroupRepository.save(orderTableGroup));
    }

    public void applyUnGroup(Long id) {
        OrderTableGroup orderTableGroup = tableGroupRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        orderTableGroup.applyUnGroup();
    }
}
