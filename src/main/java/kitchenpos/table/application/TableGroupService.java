package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.request.TableGroupRequest;
import kitchenpos.table.domain.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

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
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> orderTables = validateOrderTables(request.getOrderTableIds());

        TableGroup tableGroup = TableGroup.from(orderTables);
        tableGroup.validateTablesEmpty();

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        savedTableGroup.tablesMapIntoGroup();

        return TableGroupResponse.toResponse(savedTableGroup);
    }

    private List<OrderTable> validateOrderTables(List<Long> orderTableIds) {
        validateOrderTableSize(orderTableIds);

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTableEqualsSize(savedOrderTables, orderTableIds);
        return savedOrderTables;
    }

    private void validateOrderTableSize(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableEqualsSize(List<OrderTable> savedOrderTables, List<Long> orderTableIds)  {
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderTablesStatus(orderTables);
        TableGroup tableGroup = TableGroup.from(orderTables);
        tableGroup.unGroup();
    }

    private void validateOrderTablesStatus(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
            orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
