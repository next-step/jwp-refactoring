package kitchenpos.application;

import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private TableGroupRepository tableGroupRepository;

    public TableGroupService(
            TableGroupRepository tableGroupRepository
    ) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup());

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findByTableGroupId(tableGroupId);
        tableGroup.ungroup();
    }

    private TableGroup findByTableGroupId(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
