package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.IllegalOrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.NoSuchTableGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public static final String ERROR_ORDER_TABLE_NOT_EXISTS = "존재하지 않는 주문테이블이 있습니다.";
    public static final String ERROR_ORDER_INVALID_STATUS = "주문의 상태는 %s일 수 없습니다.";

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
        validateOrderStatusToUngroup(tableGroup);
        tableGroup.ungroup();
    }

    private void validateOrderStatusToUngroup(TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if(orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalOrderException(
                    String.format(ERROR_ORDER_INVALID_STATUS, OrderStatus.COOKING + " " + OrderStatus.MEAL)
            );
        }
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
