package kitchenpos.order.applicatioin;

import kitchenpos.order.*;
import kitchenpos.order.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTableIds();
        /*
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        final List<Long> orderTableIds = tableGroup.getOrderTableIds(orderTables);
        */
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        tableGroup.compareOrderTables(orderTableIds, savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        List<Long> saveOrderTableIds = new ArrayList<>();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            saveOrderTableIds.add(savedOrderTable.getId());
            savedOrderTable.changeTableGroup(savedTableGroup);
            savedOrderTable.changeEmpty(false);
            orderTableRepository.save(savedOrderTable);
        }
        savedTableGroup.changeOrderTablesIds(saveOrderTableIds);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
            orderTableRepository.save(orderTable);
        }
    }
}
