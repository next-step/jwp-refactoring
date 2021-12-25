package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupManager;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupManager tableGroupManager;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final TableGroupManager tableGroupManager) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupManager = tableGroupManager;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        validateOrderTablesSize(tableGroupRequest);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroupManager.grouping(tableGroup.getId(), tableGroupRequest.getOrderTableIds());
        return TableGroupResponse.of(tableGroup);
    }

    private void validateOrderTablesSize(TableGroupRequest tableGroupRequest) {
        if (tableGroupRequest.isEmptyOrderTables() || tableGroupRequest.getOrderTablesSize() < 2) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupNotFoundException(tableGroupId));
        tableGroupManager.ungrouping(tableGroupId);
    }
}
