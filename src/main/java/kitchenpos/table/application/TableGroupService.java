package kitchenpos.table.application;

import static kitchenpos.order.domain.OrderStatus.UNGROUP_DISABLE_ORDER_STATUS;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroupResponse create(final OrderTables orderTables) {
        orderTables.validateForCreatableGroup();
        final OrderTables savedOrderTables =
            OrderTables.of(orderTableRepository.findAllById(orderTables.extractIds()));
        savedOrderTables.validateForCreatableGroup(orderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
        savedOrderTables.groupBy(savedTableGroup);
        orderTableRepository.saveAll(savedOrderTables.getValues());

        return TableGroupResponse.from(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = OrderTables.of(orderTableRepository.findAllByTableGroupId(
            tableGroupId));

        validateForUngroup(orderTables);

        orderTables.ungroup();
        orderTableRepository.saveAll(orderTables.getValues());
    }

    private void validateForUngroup(OrderTables orderTables) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTables.extractIds(),
            UNGROUP_DISABLE_ORDER_STATUS)) {
            throw new IllegalArgumentException("주문 상태가 요리중 또는 식사 상태여서 그룹 해제가 불가능합니다");
        }
    }
}
