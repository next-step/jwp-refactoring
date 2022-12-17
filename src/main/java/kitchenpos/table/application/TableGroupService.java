package kitchenpos.table.application;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupCreateValidator;
import kitchenpos.table.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupCreateValidator createValidator;

    public TableGroupService(TableService tableService, TableGroupRepository tableGroupRepository,
            TableGroupCreateValidator createValidator) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.createValidator = createValidator;
    }

    @Transactional
    public TableGroup create(final TableGroup requestTableGroup) {
        createValidator.validate(requestTableGroup);
        final TableGroup savedTableGroup = tableGroupRepository.save(requestTableGroup);
        tableService.group(requestTableGroup.orderTables(), savedTableGroup.getId());
        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup savedTableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정 테이블을 찾을 수 없습니다"));
        tableService.unGroupTables(savedTableGroup.orderTables());
        tableGroupRepository.save(savedTableGroup);
    }
}
