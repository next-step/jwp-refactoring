package kitchenpos.tablegroup;

import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTables;
import kitchenpos.table.TableService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.InvalidUnGroupException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {
    private final TableService tableService;
    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final TableService tableService,
            final OrderRepository orderRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.tableService = tableService;
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        OrderTables orderTables = tableService.findOrderTablesById(tableGroupRequest.getOrderTableIds());

        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        orderTables.group(savedTableGroup);

        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> persistOrderTables = tableService.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = persistOrderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidUnGroupException("주문 상태가 조리 또는 식사일 경우 해제할 수 없습니다.");
        }

        persistOrderTables.forEach(OrderTable::unGroup);
    }
}
