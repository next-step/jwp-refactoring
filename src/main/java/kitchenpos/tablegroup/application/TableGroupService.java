package kitchenpos.tablegroup.application;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.exception.NoSuchTableGroupException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public static final String ERROR_ORDER_TABLE_NOT_EXISTS = "존재하지 않는 주문테이블이 있습니다.";

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = retrieveOrderTables(tableGroupRequest);
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of(LocalDateTime.now(), orderTables));

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        List<Order> orders = findOrdersInTableGroup(tableGroup);
        tableGroup.ungroup(orders);
    }

    private List<Order> findOrdersInTableGroup(TableGroup tableGroup) {
        List<OrderTable> orderTables = tableGroup.getOrderTables();
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return orderRepository.findAllByOrderTableIdIn(orderTableIds);
    }

    private List<OrderTable> retrieveOrderTables(TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTablesAllExist(orderTableIds, savedOrderTables);

        return savedOrderTables;
    }

    private void validateOrderTablesAllExist(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_NOT_EXISTS);
        }
    }

    public TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NoSuchTableGroupException(tableGroupId));
    }
}
