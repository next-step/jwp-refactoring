package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kitchenpos.common.domain.OrderStatus.UNCOMPLETED_STATUSES;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final OrderTables orderTables) {
        final OrderTables savedOrderTables = OrderTables.of(
                orderTableRepository.findAllByIdIn(orderTables.getIds()));
        savedOrderTables.checkGroupable();

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
        savedOrderTables.groupBy(savedTableGroup);
        orderTableRepository.saveAll(savedOrderTables.getValues());

        return TableGroupResponse.from(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = OrderTables.of(orderTableRepository.findAllByTableGroupId(
                tableGroupId));

        validateOfUngroup(orderTables);
        orderTables.ungroup();

        orderTableRepository.saveAll(orderTables.getValues());
    }

    private void validateOfUngroup(OrderTables orderTables) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTables.getIds(), UNCOMPLETED_STATUSES)) {
            throw new IllegalArgumentException("조리중 또는 식사중 상태에서는 그룹 해제가 불가능합니다");
        }
    }
}
