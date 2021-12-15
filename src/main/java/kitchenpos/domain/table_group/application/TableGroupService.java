package kitchenpos.domain.table_group.application;

import kitchenpos.domain.order.domain.OrderRepository;
import kitchenpos.domain.table.domain.OrderTableRepository;
import kitchenpos.domain.table_group.domain.TableGroupRepository;
import kitchenpos.domain.order.domain.OrderStatus;
import kitchenpos.domain.table.domain.OrderTables;
import kitchenpos.domain.table_group.domain.TableGroup;
import kitchenpos.domain.table_group.dto.TableGroupRequest;
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
    public TableGroup create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final OrderTables tables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));

        tables.checkOrderTables(orderTableIds);

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
        tables.group(tableGroup.getId());
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        checkCompleteTable(orderTables.getOrderTableIds());
        orderTables.ungroup();
    }

    private void checkCompleteTable(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
