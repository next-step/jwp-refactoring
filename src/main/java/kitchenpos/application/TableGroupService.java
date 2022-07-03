package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
//        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        OrderTables orderTables = tableGroup.getOrderTables();
        orderTables.validateCreate();
        //

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable savedOrderTable : orderTables.getOrderTables()) {
            savedOrderTable.setTableGroup(savedTableGroup);
            savedOrderTable.setEmpty(false);
            orderTableRepository.save(savedOrderTable);
        }
//        for (final OrderTable savedOrderTable : savedOrderTables) {
//            savedOrderTable.setTableGroup(savedTableGroup);
//            savedOrderTable.setEmpty(false);
//            orderTableRepository.save(savedOrderTable);
//        }
//        savedTableGroup.setOrderTables(savedOrderTables);

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
            orderTable.setTableGroup(null);
            orderTableRepository.save(orderTable);
        }
    }
}
