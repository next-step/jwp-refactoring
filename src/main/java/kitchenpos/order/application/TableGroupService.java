package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.dao.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;

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
    public TableGroupResponse create(final TableGroupRequest request) {
        TableGroup tableGroup = request.toTableGroup();
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체지정은 최소 두 테이블 이상만 가능합니다");
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("등록된 주문 테이블만 단체지정 할 수 있습니다");
        }
        
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException("지정할 주문 테이블은 단체지정이 되어 있지 않아야 합니다");
            }
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroup(savedTableGroup);
            savedOrderTable.changeEmpty(false);
            orderTableRepository.save(savedOrderTable);
        }
        savedTableGroup.addOrderTables(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중, 식사중인 주문 테이블은 단체지정을 해제할 수 업습니다");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(null);
            orderTableRepository.save(orderTable);
        }
    }
}
