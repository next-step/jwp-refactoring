package kitchenpos.table.application;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupMapper;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupMapper tableGroupMapper;

    public TableGroupService(
            TableGroupRepository tableGroupRepository,
            TableGroupMapper tableGroupMapper) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupMapper = tableGroupMapper;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        TableGroup tableGroup = tableGroupMapper.mapFrom(request);
        final TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(persistTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        tableGroup.ungroup();
        tableGroupRepository.save(tableGroup);
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
    }

}
