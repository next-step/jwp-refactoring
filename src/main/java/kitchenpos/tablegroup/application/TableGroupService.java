package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.mapper.TableGroupMapper;
import kitchenpos.tablegroup.repository.TableGroupRepository;
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
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        tableGroupValidator.createValidator(request);

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        return TableGroupMapper.toTableGroupResponse(savedTableGroup, request);
    }

/*    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableService.findAllByTableGroupId(tableGroupId)
                .forEach(OrderTable::ungroup);
    }*/
}
