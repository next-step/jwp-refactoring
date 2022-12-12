package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        validateOrderTablesSameSize(orderTableIds, savedOrderTables);

        TableGroup tableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup(savedOrderTables));
        changeOrderTablesEmpty(savedOrderTables);
        return TableGroupResponse.from(tableGroup);
    }

    private void validateOrderTablesSameSize(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void changeOrderTablesEmpty(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setEmpty(false);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderStatusComplete(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.unTableGroup();
        }
    }

    private void validateOrderStatusComplete(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
