package kitchenpos.tablegroup.application;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            TableGroupRepository tableGroupRepository,
            TableGroupValidator tableGroupValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        tableGroupValidator.validateCreateTableGroup(request);

        TableGroup savedTableGroup = tableGroupRepository.save(request.createTableGroup());
        savedTableGroup.setOrderTableIds(request.getOrderTableIds());
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        tableGroupValidator.validateUnGroup(tableGroup);

        tableGroup.ungroup();
    }

    private TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.TABLE_GROUP_IS_NOT_EXIST.getMessage()));
    }
}
