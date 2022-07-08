package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.table.infra.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.infra.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        tableGroup.checkOrderTablesEmptyOrSizeOne();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(tableGroup.getOrderTableIds());
        tableGroup.checkAllOrderSavedOrderTables(savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        savedTableGroup.updateOrderTables();

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = findAllByTableGroupId(tableGroupId);
        checkOrderStatusInCookingOrMeal(orderTables.getOrderTableIds());

        orderTables.upgroupAll();
        orderTableRepository.saveAll(orderTables.getOrderTables());
    }

    private OrderTables findAllByTableGroupId(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalAccessError::new);
        return OrderTables.of(orderTableRepository.findAllByTableGroup(tableGroup));
    }

    private void checkOrderStatusInCookingOrMeal(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
