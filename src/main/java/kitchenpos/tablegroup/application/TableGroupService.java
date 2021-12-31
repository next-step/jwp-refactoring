package kitchenpos.tablegroup.application;

import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        tableGroupValidator.validateCreateTableGroup(tableGroupRequest);
        final TableGroup tableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup());

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findByTableGroupId(tableGroupId);
        tableGroupValidator.validateUnGroupTableGroup(tableGroup);
        tableGroup.ungroup();
    }

    public TableGroup findByTableGroupId(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(NotFoundEntityException::new);
    }
}
