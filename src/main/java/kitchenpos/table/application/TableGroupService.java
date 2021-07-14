package kitchenpos.table.application;

import static kitchenpos.exception.KitchenposExceptionMessage.EXISTS_NOT_COMPLETION_ORDER;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER_TABLE;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_TABLE_GROUP;

import kitchenpos.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = getOrderTables(request);
        return TableGroupResponse.of(tableGroupRepository.save(new TableGroup(savedOrderTables)));
    }

    private List<OrderTable> getOrderTables(TableGroupRequest request) {
        return request.getOrderTables()
                      .stream()
                      .map(this::findOrderTableById)
                      .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(OrderTableIdRequest orderTableIdRequest) {
        return orderTableRepository.findById(orderTableIdRequest.getId())
                                   .orElseThrow(() -> new KitchenposException(NOT_FOUND_ORDER_TABLE));
    }

    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = findTableGroupById(tableGroupId).getOrderTables();
        checkNotCompletionOrders(orderTables.getOrderTableIds());
        orderTables.ungroup();
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                                   .orElseThrow(() -> new KitchenposException(NOT_FOUND_TABLE_GROUP));
    }

    private void checkNotCompletionOrders(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                                                                   OrderStatus.excludeCompletionList())) {
            throw new KitchenposException(EXISTS_NOT_COMPLETION_ORDER);
        }
    }
}
