package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
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
        List<OrderTable> orderTables = this.findOrderTables(tableGroupRequest.getOrderTables());
        validateTableGroupOrderTables(tableGroupRequest.getOrderTables(), orderTables);

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.from(orderTables));
        tableGroup.assignedOrderTables(orderTables);
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = this.findTableGroup(tableGroupId);
        validateUnGroup(tableGroup);

        for (final OrderTable orderTable : tableGroup.getOrderTables().getReadOnlyValues()) {
            orderTable.ungroup();
        }
    }

    private void validateUnGroup(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables().getIds();

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
    }

    private void validateTableGroupOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
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

    private List<OrderTable> findOrderTables(List<Long> orderTables) {
        return orderTableRepository.findAllByIdIn(orderTables);
    }
}
