package kitchenpos.table.application;

import kitchenpos.common.exception.InvalidOrderTableException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }


    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        savedOrderTables.validateOrderTable();
        final TableGroup tableGroup = tableGroupRequest.toTableGroup(savedOrderTables);
        savedOrderTables.initTableGroup(tableGroup);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findByIdElseThrow(tableGroupId);
        checkTablesOrderStatus(tableGroup.getOrderTables());
        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }

    public int countById(final Long tableGroupId) {
        return tableGroupRepository.countById(tableGroupId);
    }

    private void checkTablesOrderStatus(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderTableException();
        }
    }
}
