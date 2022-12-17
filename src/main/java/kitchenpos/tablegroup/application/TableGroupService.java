package kitchenpos.tablegroup.application;

import java.util.List;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    public static final String NOT_EXIST_TABLE_GROUP = "테이블 그룹이 존재하지 않습니다.";
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(List<Long> tableIds) {
        TableGroup group = new TableGroup(tableIds);
        TableGroup savedTableGroup = tableGroupRepository.save(group);
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_TABLE_GROUP));
        tableGroupRepository.delete(tableGroup);
    }
}
