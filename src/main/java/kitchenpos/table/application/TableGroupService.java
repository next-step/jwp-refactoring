package kitchenpos.table.application;

import kitchenpos.exception.NotExistEntityException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableService tableService, final TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = tableGroupRequest.toEntity();

        final List<OrderTable> savedOrderTables = tableService.findAllByIds(tableGroupRequest.toOrderTableIds());

        tableGroup.addAllOrderTables(savedOrderTables);
        TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(saveTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {

        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotExistEntityException("지정된 단체를 찾을 수 없습니다."));

        tableGroup.unGrouping();
    }
}
