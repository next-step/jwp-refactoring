package kitchenpos.ordertable.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private static final String ERROR_MESSAGE_NOT_EXIST_TABLE_GROUP = "테이블 그룹 정보가 없습니다.";

    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;

    public TableGroupService(TableGroupRepository tableGroupRepository,
        TableService tableService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();
        List<OrderTable> orderTables = tableService.findOrderTables(orderTableRequests);

        TableGroup tableGroup = new TableGroup();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        savedTableGroup.groupTables(orderTables);
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroup(tableGroupId);
        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }

    public TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_EXIST_TABLE_GROUP));
    }
}
