package kitchenpos.table.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        final TableGroup persistTableGroup = tableGroupRepository.save(TableGroup.create());

        persistTableGroup.group(tableGroupValidator, orderTableIds);

        return TableGroupResponse.of(tableGroupRepository.save(persistTableGroup));
    }


    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup persistTableGroup = findTableGroupById(tableGroupId);

        persistTableGroup.ungroup(tableGroupValidator);
        tableGroupRepository.delete(persistTableGroup);
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundException("해당 테이블 그룹을 찾을 수 없습니다."));
    }
}
