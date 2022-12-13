package kitchenpos.order.applicaiton;

import kitchenpos.order.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Service
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
    public TableGroup create(final TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTableIds();
        if(CollectionUtils.isEmpty(orderTableIds)){
            throw new IllegalArgumentException();
        }
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        tableGroup.throwIfOrderTableSizeWrong(savedOrderTables.size());

        return tableGroupRepository.save(TableGroup.of(OrderTables.ofSaved(savedOrderTables)));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = OrderTables.of(
                orderTableRepository.findAllByTableGroupId(tableGroupId)
        );
        throwIfSomeOrderInProgress(orderTables.getTableIds());

        orderTables.unGroup();
    }

    private void throwIfSomeOrderInProgress(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTable_IdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
