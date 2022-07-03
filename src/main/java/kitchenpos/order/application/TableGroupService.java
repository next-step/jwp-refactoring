package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TableGroupService {
    private static final String ERROR_MESSAGE_INVALID_ORDER_STATUS = "주문 상태가 '조리' 또는 '식사'가 아니어야 합니다.";

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> orderTables = getOrderTablesByIds(request.getOrderTableIds());
        TableGroup tableGroup = new TableGroup(orderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.of(savedTableGroup);
    }

    private List<OrderTable> getOrderTablesByIds(List<Long> orderTableIds) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTableEqualsSize(savedOrderTables, orderTableIds);
        return savedOrderTables;
    }

    private void validateOrderTableEqualsSize(List<OrderTable> savedOrderTables, List<Long> orderTableIds) {
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getTableGroupById(tableGroupId);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        validateOrderTablesStatus(orderTables);
        tableGroup.unGroup();
    }

    private void validateOrderTablesStatus(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ORDER_STATUS);
        }
    }

    private TableGroup getTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NoSuchElementException::new);
    }
}
