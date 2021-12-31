package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NotCreateTableGroupException;
import kitchenpos.table.exception.NotCreatedOrderTablesException;
import kitchenpos.table.exception.NotValidOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
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
        validateRequest(tableGroupRequest);

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        isNotCreatedOrderTables(tableGroupRequest, savedOrderTables);

        TableGroup tableGroup = TableGroup.create(savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroupId);
        validateOrder(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
        }
    }

    private void validateOrder(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new NotValidOrderException();
        }
    }

    private void validateRequest(TableGroupRequest tableGroupRequest) {
        final List<Long> orderTables = tableGroupRequest.getOrderTableIds();
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new NotCreateTableGroupException("주문 테이블 개수가 부족합니다.");
        }

    }

    private void isNotCreatedOrderTables(TableGroupRequest tableGroupRequest, List<OrderTable> savedOrderTables) {
        List<Long> requestIds = tableGroupRequest.getOrderTableIds();
        if (requestIds.size() != savedOrderTables.size()) {
            throw new NotCreatedOrderTablesException();
        }
    }
}
