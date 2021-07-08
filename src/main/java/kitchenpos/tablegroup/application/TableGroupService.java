package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<OrderTableIdRequest> orderTableIdRequests = generateOrderTableIdRequests(tableGroupRequest);

        List<Long> orderTableIds = orderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());

        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        OrderTables orderTables = new OrderTables(savedOrderTables);
        orderTables.validateOrderTableNotEmpty();

        TableGroup savedTableGroup = generateTableGroup(savedOrderTables);
        savedTableGroup.setOrderTables(orderTables);

        return TableGroupResponse.of(savedTableGroup);
    }

    private TableGroup generateTableGroup(List<OrderTable> savedOrderTables) {
        TableGroup tableGroup = new TableGroup(savedOrderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.changeEmpty(false);
        }
        return savedTableGroup;
    }

    private List<OrderTableIdRequest> generateOrderTableIdRequests(TableGroupRequest tableGroupRequest) {
        List<OrderTableIdRequest> orderTableIdRequests = tableGroupRequest.getOrderTables();

        if (CollectionUtils.isEmpty(orderTableIdRequests) || orderTableIdRequests.size() < 2) {
            throw new IllegalArgumentException();
        }
        return orderTableIdRequests;
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTableList = orderTableRepository.findAllByTableGroupId(tableGroupId);

        OrderTables orderTables = new OrderTables(orderTableList);
        List<Long> orderTableIds = orderTables.orderTableIds();

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTableList) {
            orderTable.setTableGroup(null);
        }
    }
}
