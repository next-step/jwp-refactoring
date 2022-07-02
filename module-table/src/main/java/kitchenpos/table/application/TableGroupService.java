package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository,
                             OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest, LocalDateTime createdDate) {
        OrderTables tables = findOrderTables(tableGroupRequest);
        checkAlreadyTableGroup(tables);
        TableGroup tableGroup = new TableGroup(createdDate, tables);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroup(tableGroupId);
        OrderTables orderTables = tableGroup.getOrderTables();
        checkPossibleUngroup(orderTables);
        tableGroup.ungroupingTableGroup();
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 단체 지정이 등록되어있지 않습니다."));
    }

    private void checkPossibleUngroup(OrderTables orderTables) {
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            Orders savedOrders = new Orders(orderRepository.findAllByOrderTableId(orderTable.getId()));
            checkOrderStatus(savedOrders);
        }
    }

    private void checkAlreadyTableGroup(OrderTables orderTables) {
        List<Long> orderTableIds = orderTables.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (tableGroupRepository.existsByOrderTables(orderTableIds)) {
            throw new IllegalArgumentException("[ERROR] 이미 그룹이 지정 되어있습니다.");
        }
    }

    private OrderTables findOrderTables(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(tableGroupRequest.getRequestOrderTableIds());
        if (orderTables.size() != tableGroupRequest.getRequestOrderTableIds().size()) {
            throw new IllegalArgumentException("[ERROR] 등록 되어있지 않은 테이블이 있습니다.");
        }
        return new OrderTables(orderTables);
    }

    private void checkOrderStatus(Orders orders) {
        if (orders.containOrderStatus(OrderStatus.COOKING)) {
            throw new IllegalArgumentException("[ERROR] 조리 상태인 주문이 있어 단체 지정 해제 할 수 없습니다.");
        }

        if (orders.containOrderStatus(OrderStatus.MEAL)) {
            throw new IllegalArgumentException("[ERROR] 식사 상태인 주문이 있어 단체 지정 해제 할 수 없습니다.");
        }
    }

}
