package kitchenpos.application;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.tableGroup.OrderTableInTableGroup;
import kitchenpos.domain.tableGroup.TableGroup;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.orderTable.exceptions.OrderTableEntityNotFoundException;
import kitchenpos.domain.tableGroup.TableGroupRepository;
import kitchenpos.domain.tableGroup.exceptions.InvalidTableGroupTryException;
import kitchenpos.ui.dto.tableGroup.OrderTableInTableGroupRequest;
import kitchenpos.ui.dto.tableGroup.TableGroupRequest;
import kitchenpos.ui.dto.tableGroup.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableInTableGroupRequest> orderTables = tableGroupRequest.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidTableGroupTryException("2개 미만의 주문 테이블로 단체 지정할 수 없다.");
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableInTableGroupRequest::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new OrderTableEntityNotFoundException("존재하지 않는 주문 테이블로 단체 지정할 수 없습니다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new InvalidTableGroupTryException("이미 단체 지정된 주문 테이블을 또 단체 지정할 수 없습니다.");
            }
            if (!savedOrderTable.isEmpty()) {
                throw new InvalidTableGroupTryException("비어있지 않은 주문 테이블로 단체 지정할 수 없습니다.");
            }
        }

        List<OrderTableInTableGroup> orderTablesInTableGroup = orderTables.stream()
                .map(it -> new OrderTableInTableGroup(it.getId()))
                .collect(Collectors.toList());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTablesInTableGroup);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        // TODO: OrderTable에 그룹 지어주는 동작 필요

        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableRepository.save(orderTable);
        }
    }
}
