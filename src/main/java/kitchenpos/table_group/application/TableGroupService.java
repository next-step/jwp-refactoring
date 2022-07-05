package kitchenpos.table_group.application;

import kitchenpos.table.application.TableService;
import kitchenpos.table_group.domain.GroupTable;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.domain.TableGroupValidator;
import kitchenpos.table_group.dto.TableGroupResponse;
import kitchenpos.table_group.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private static final String TABLE_GROUP_IS_NOT_EXIST = "지정된 단체를 찾을 수 없습니다";
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final TableService tableService;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator, TableService tableService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.tableService = tableService;
    }

    @Transactional
    public TableGroupResponse create(final List<Long> orderTableIds) {
        final List<GroupTable> groupTables = tableGroupValidator.checkOrderTableIds(orderTableIds);
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.group(groupTables));
        tableService.groupBy(savedTableGroup.getId(), orderTableIds);
        return new TableGroupResponse(savedTableGroup, groupTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(TABLE_GROUP_IS_NOT_EXIST));
        tableGroupValidator.checkUngroupTables(tableGroup);
        tableService.ungroup(tableGroupId);
    }
}
