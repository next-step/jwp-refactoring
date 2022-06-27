package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kitchenpos.order.domain.OrderStatus.UNCOMPLETED_STATUSES;

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
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        OrderTables orderTables = tableGroupRequest.toEntity();
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
