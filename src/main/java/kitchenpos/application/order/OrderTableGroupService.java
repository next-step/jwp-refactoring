package kitchenpos.application.order;

import kitchenpos.domain.order.*;
import kitchenpos.ui.dto.order.OrderTableGroupRequest;
import kitchenpos.ui.dto.order.OrderTableGroupResponse;
import kitchenpos.ui.dto.order.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderTableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableGroupRepository orderTableGroupRepository;

    public OrderTableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, OrderTableGroupRepository orderTableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderTableGroupRepository = orderTableGroupRepository;
    }

    @Transactional
    public OrderTableGroupResponse create(final OrderTableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();
        if (CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableRequests.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }

        OrderTableGroup orderTableGroup = OrderTableGroup.of(LocalDateTime.now(), savedOrderTables);
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setEmpty(false);
        }

        return OrderTableGroupResponse.of(orderTableGroupRepository.save(orderTableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
        }
    }
}
