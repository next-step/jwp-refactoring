package kitchenpos.table.application;

import java.util.List;
import kitchenpos.exception.CannotCreateOrderTableException;
import kitchenpos.exception.NotFoundTableGroupException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableService orderTableService,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.toOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableService.findAllOrderTablesByIdIn(orderTableIds);

        TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);

        validateNotFoundOrderTables(savedOrderTables, orderTableIds);

        tableGroup.groupOrderTables(OrderTables.from(savedOrderTables));

        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NotFoundTableGroupException::new);
        tableGroup.unGroupOrderTables();
    }

    private void validateNotFoundOrderTables(List<OrderTable> orderTables, List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new CannotCreateOrderTableException("모든 테이블은 존재하는 테이블이어야 합니다.");
        }
    }
}
