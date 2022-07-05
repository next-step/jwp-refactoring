package kitchenpos.table.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.order.exception.OrderStatusException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());

        if (savedOrderTables.size() != tableGroupRequest.getOrderTableIds().size()) {
            throw new IllegalArgumentException();
        }

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.addTableGroup(tableGroup);
        }

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTables().orderTableIds(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderStatusException(OrderStatusException.ORDER_STATUS_CAN_NOT_UNGROUP_MSG);
        }

        tableGroup.unGroup();
    }
}
