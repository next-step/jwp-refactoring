package kitchenpos.table.application;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private static final int MINIMUM_GROUP_SIZE = 2;

    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(TableGroupRepository tableGroupRepository,OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        final TableGroup persistTableGroup = tableGroupRepository.save(TableGroup.create());
        final List<OrderTable> orderTables = findOrderTableByIds(orderTableIds);

        validateGrouping(orderTables);
        persistTableGroup.group(orderTables);

        return TableGroupResponse.of(tableGroupRepository.save(persistTableGroup));
    }

    private void validateGrouping(List<OrderTable> orderTables) {
        checkNumberOfOrderTable(orderTables);
        checkOrderTableGroupIsEmpty(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup persistTableGroup = findTableGroupById(tableGroupId);
        final List<OrderTable> orderTables = findOrderTableByTableGroupId(tableGroupId);

        validateUngrouping(orderTables);
        persistTableGroup.ungroup(orderTables);

        tableGroupRepository.delete(persistTableGroup);
    }

    private void validateUngrouping(List<OrderTable> orderTables) {
        checkGroupOrderIsNotComplete(orderTables);
    }

    private void checkNumberOfOrderTable(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_GROUP_SIZE) {
            throw new BadRequestException("주문 테이블 " + MINIMUM_GROUP_SIZE + "개 이상 그룹화할 수 있습니다.");
        }
    }

    private void checkOrderTableGroupIsEmpty(List<OrderTable> orderTables) {
        boolean isGroupEmpty = orderTables.stream()
                .allMatch(orderTable -> Objects.isNull(orderTable.getTableGroupId()));

        if (!isGroupEmpty) {
            throw new BadRequestException("이미 그룹이 존재하는 주문 테이블입니다.");
        }
    }

    private void checkGroupOrderIsNotComplete(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        orderTableIds.forEach(this::checkOrderIsNotComplete);
    }

    private void checkOrderIsNotComplete(Long orderTableId) {
        List<Order> orders = orderRepository.findOrderByOrderTableId(orderTableId);
        boolean isComplete = orders.stream()
                .allMatch(Order::isComplete);

        if (!isComplete) {
            throw new BadRequestException("완료되지 않은 주문이 존재하여 테이블 그룹을 해제할 수 없습니다.");
        }
    }

    private List<OrderTable> findOrderTableByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findOrderTableByTableGroupId(tableGroupId);
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundException("해당 테이블 그룹을 찾을 수 없습니다."));
    }

    private List<OrderTable> findOrderTableByIds(List<Long> orderTableIds) {
        return orderTableRepository.findAllById(orderTableIds);
    }
}
