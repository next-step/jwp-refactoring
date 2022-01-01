package kitchenpos.application;

import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableService tableService, final TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.from());
        OrderTables orderTables = tableService.findOrderTables(tableGroupRequest);
        savedTableGroup.group(orderTables);
        return TableGroupResponse.from(savedTableGroup, orderTables.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정된 주문 테이블만 해제할 수 있습니다."));
        OrderTables orderTables = tableService.findOrderTables(tableGroupId);
        tableGroup.ungroup(orderTables);
    }
}
