package kitchenpos.tableGroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        TableGroup tableGroup = tableGroupRequest.toTableGroup();
        OrderTables orderTables = tableGroup.getOrderTables();

        orderTables.checkOrderTables();
        orderTables.checkTableStatus();

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable savedOrderTable : orderTables.getOrderTables()) {
            savedOrderTable.initialTableGroup(savedTableGroup);
            orderTableRepository.save(savedOrderTable);
        }

        return TableGroupResponse.of(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        OrderTables orderTables = tableGroup.getOrderTables();

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTables.getOrderTablesIds(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 완료되지 않았습니다.");
        }

        for (final OrderTable orderTable : orderTables.getOrderTables()) {
            orderTable.unGroupingTable();
            orderTableRepository.save(orderTable);
        }
    }
}
