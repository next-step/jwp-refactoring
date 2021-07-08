package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.order.TableGroupRequest;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.exception.NotMatchOrderTableException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.TableGroupRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.domain.order.TableGroup.of;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableService orderTableService, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupRequest
                .getOrderTableRequests().stream()
                .map(orderTableRequest -> orderTableService.getOrderTable(orderTableRequest.getId()))
                .collect(Collectors.toList());

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableService.getAllOrderTablesByIds(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new NotMatchOrderTableException("orderTable size: " + orderTables.size() +
                    " savedOrderTables size: " + savedOrderTables.size());
        }

        TableGroup tableGroup = of(orderTables);
        tableGroup.changeOrderTables(savedOrderTables);

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableService.getAllOrderTablesByGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusException("orderTableIds: " + Strings.join(orderTableIds, ','));
        }

        for (final OrderTable orderTable : orderTables) {
            orderTableService.makeTableGroupEmpty(orderTable);
        }
    }
}
