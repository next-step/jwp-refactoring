package kitchenpos.table.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final TableService tableService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;

    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = new TableGroup();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        OrderTables orderTables =
                tableService.createGroupedOrderTables(tableGroupRequest.getOrderTableIds(), savedTableGroup);
        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findById(tableGroupId);
        tableService.ungroupOrderTables(tableGroupId);
        tableGroupRepository.delete(tableGroup);
    }

    private TableGroup findById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_TABLE_GROUP.getMessage()));
    }


}
