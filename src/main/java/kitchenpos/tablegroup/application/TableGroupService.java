package kitchenpos.tablegroup.application;

import static kitchenpos.exception.KitchenposExceptionMessage.EXISTS_NOT_COMPLETION_ORDER;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER_TABLE;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_TABLE_GROUP;
import static kitchenpos.exception.KitchenposExceptionMessage.ORDER_TABLE_CONNOT_LOWER_THAN_MIN;

import kitchenpos.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {

    private static final int ORDER_TABLE_MIN_SIZE = 2;

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
        checkOrderTableOverMin(request.getOrderTables());
        final List<OrderTable> savedOrderTables = getOrderTables(request);
        return TableGroupResponse.of(tableGroupRepository.save(new TableGroup(savedOrderTables)));
    }

    private void checkOrderTableOverMin(List<OrderTableIdRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < ORDER_TABLE_MIN_SIZE) {
            throw new KitchenposException(ORDER_TABLE_CONNOT_LOWER_THAN_MIN);
        }
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
