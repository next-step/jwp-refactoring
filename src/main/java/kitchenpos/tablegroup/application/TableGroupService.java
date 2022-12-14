package kitchenpos.tablegroup.application;

import java.util.List;
import kitchenpos.tablegroup.domain.GroupTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    public static final String NOT_EXIST_TABLE_GROUP = "테이블 그룹이 존재하지 않습니다.";
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(List<Long> tableIds) {
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        List<GroupTable> tables = tableGroupValidator.groupingTable(tableIds, savedTableGroup.getId());
        return TableGroupResponse.from(savedTableGroup, tables);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_TABLE_GROUP));
        tableGroupValidator.ungroup(tableGroup.getId());
        tableGroupRepository.deleteById(tableGroupId);
    }
}
