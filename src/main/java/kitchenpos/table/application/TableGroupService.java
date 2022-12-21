package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        List<OrderTable> orderTables = tableGroupRequest.getOrderTableIds().stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());

        TableGroup tableGroup = new TableGroup();
        tableGroup.addList(orderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup persistTableGroup = findTableGroupById(tableGroupId);
        final List<OrderTable> orderTables = persistTableGroup.getOrderTables();
        orderTables.stream()
                .forEach(orderTable -> {
                    Order order = findOrderByOrderTableId(orderTable.getId());
                    orderTable.unGroup(order);
                });
        }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
    }

    private Order findOrderByOrderTableId(Long orderTableId) {
        return orderRepository.findOrderByOrderTableId(orderTableId).orElseThrow(IllegalArgumentException::new);
    }
}
