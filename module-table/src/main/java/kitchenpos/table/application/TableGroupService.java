package kitchenpos.table.application;

import kitchenpos.order.application.OrderStatusValidator;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final StatusValidator statusValidator;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository,
            OrderStatusValidator statusValidator){
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.statusValidator = statusValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        OrderTables orderTables = new OrderTables(findOrderTablesByIdIn(orderTableIds));
        orderTables.validateForCreateTableGroup(orderTables);
        orderTables.updateEmpty(false);

        TableGroup tableGroup = tableGroupRequest.toTableGroup(orderTables);

        return TableGroupResponse.of(saveTableGroup(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(findOrderTablesByTableGroupId(tableGroupId));
        statusValidator.validateOrderTablesNotCompletion(orderTables);

        orderTables.updateTableGroup(null);
    }

    private List<OrderTable> findOrderTablesByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    private TableGroup saveTableGroup(TableGroup tableGroup) {
        return tableGroupRepository.save(tableGroup);
    }

    private List<OrderTable> findOrderTablesByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }
}
