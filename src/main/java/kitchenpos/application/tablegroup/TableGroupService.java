package kitchenpos.application.tablegroup;

import static kitchenpos.domain.tablegroup.TableGroup.CREATE_TABLE_GROUP_ORDER_TABLE_MISMATCH;

import java.util.List;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.table.OrderTableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.CreateTableGroupException;
import kitchenpos.exception.DontUnGroupException;
import kitchenpos.exception.NotFoundTableGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

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
        validateTableGroup(tableGroupRequest.getOrderTables(), orderTables);

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
            throw new DontUnGroupException();
        }
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundTableGroupException(tableGroupId));
    }

    private void validateTableGroup(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new CreateTableGroupException(CREATE_TABLE_GROUP_ORDER_TABLE_MISMATCH);
        }
    }
}
