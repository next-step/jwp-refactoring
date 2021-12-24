package kitchenpos.order.application;

import kitchenpos.order.application.exception.OrderTableNotFoundException;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> tableIds = request.getTableIds();
        final List<OrderTable> orderTables = getTables(tableIds);

        final TableGroup tableGroup = request.toEntity(orderTables);
        final TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(persistTableGroup, persistTableGroup.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> getTables(List<Long> tableIds) {
        return tableIds.stream()
                .map(this::getTable)
                .collect(Collectors.toList());
    }

    private OrderTable getTable(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(OrderTableNotFoundException::new);
    }
}
