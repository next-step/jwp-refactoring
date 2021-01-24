package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
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

    public TableGroupResponse create(final TableGroupRequest tableGroup) {
        final List<OrderTable> savedOrderTables = findOrderTables(tableGroup);
        TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.add(savedOrderTables);
        return TableGroupResponse.of(tableGroupRepository.save(savedTableGroup));
    }

    private List<OrderTable> findOrderTables(TableGroupRequest tableGroup) {
        final List<OrderTableRequest> orderTables = tableGroup.getOrderTables();

        final List<Long> orderTableIds = findOrderTableIds(tableGroup);
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("조회되지 않는 주문 테이블이 있습니다.");
        }

        return savedOrderTables;
    }

    private List<Long> findOrderTableIds(TableGroupRequest tableGroup) {
        final List<OrderTableRequest> orderTables = tableGroup.getOrderTables();
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateOrderStatusCookingOrMeal(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.changeTableGroupId(null);
        }
    }

    private void validateOrderStatusCookingOrMeal(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("요리중이거나 식사중인 테이블은 변경 불가합니다.");
        }
    }
}
