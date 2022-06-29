package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository,
            final TableValidator tableValidator){
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        OrderTables orderTables = new OrderTables(findOrderTablesByIdIn(orderTableIds));
        final OrderTables savedOrderTables = new OrderTables(findOrderTablesByIdIn(orderTableIds));

        orderTables.validateForCreateTableGroup(savedOrderTables);
        savedOrderTables.updateEmpty(false);

        TableGroup tableGroup = tableGroupRequest.toTableGroup(orderTables);
        tableGroup.addOrderTables(savedOrderTables);

        return TableGroupResponse.of(saveTableGroup(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(findOrderTablesByTableGroupId(tableGroupId));
        tableValidator.validateOrderTablesNotCompletion(orderTables);

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
