package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private static final int MIN_ORDER_TABLE_COUNT = 2;

    private final OrderService orderService;
    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderService orderService,
                             final OrderTableService orderTableService,
                             final TableGroupRepository tableGroupRepository) {
        this.orderService = orderService;
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableService.findOrderTables(tableGroupRequest.getOrderTables());
        validateTableGroupOrderTables(tableGroupRequest.getOrderTables(), orderTables);

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.from(orderTables));
        tableGroup.assignedOrderTables(orderTables);
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = this.findTableGroup(tableGroupId);
        validateUnGroup(tableGroup);

        for (final OrderTable orderTable : tableGroup.findOrderTables()) {
            orderTable.ungroup();
        }
    }

    private void validateUnGroup(TableGroup tableGroup) {
        if (orderService.isExistDontUnGroupState(tableGroup.findOrderTables())) {
            throw new IllegalArgumentException();
        }
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
    }

    private void validateTableGroupOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (isCreateTableGroup(orderTableIds)) {
            throw new IllegalArgumentException();
        }

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.getTableGroup() != null) {
                throw new IllegalArgumentException();
            }
        }
    }

    private boolean isCreateTableGroup(List<Long> orderTableIds) {
        return CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_ORDER_TABLE_COUNT;
    }
}
