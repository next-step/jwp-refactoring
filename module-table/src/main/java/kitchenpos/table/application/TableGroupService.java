package kitchenpos.table.application;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NotFoundTableGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
        tableGroup.group(tableGroupRequest.getOrderTableIds());

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = this.findById(tableGroupId);
        tableGroup.ungroup();
    }

    private TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundTableGroupException(tableGroupId));
    }
}
