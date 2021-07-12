package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;

@Transactional
@Service
public class TableGroupService {
    private static final int MINIMUM_TABLE_SIZE = 2;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        validateRequest(tableGroupRequest);
        final List<Long> ids = tableGroupRequest.ids();
        final OrderTables orderTables = new OrderTables(findOrderTables(ids));
        validateOrderTableSize(ids, orderTables);
        final TableGroup saved = saveTableGroup(orderTables);

        return TableGroupResponse.of(saved);
    }

    private void validateRequest(final TableGroupRequest tableGroupRequest) {
        if (tableGroupRequest.isEmptyOrderTables() || tableGroupRequest.orderTablesSize() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException(MINIMUM_TABLE_SIZE + "개 이상 테이블이 있어야 그룹을 만들 수 있습니다.");
        }
    }

    private List<OrderTable> findOrderTables(final List<Long> ids) {
        return orderTableRepository.findAllByIdIn(ids);
    }

    private void validateOrderTableSize(final List<Long> ids, final OrderTables orderTables) {
        if (ids.size() != orderTables.size()) {
            throw new IllegalArgumentException("등록되지 않은 테이블이 있습니다.");
        }
    }

    private TableGroup saveTableGroup(final OrderTables orderTables) {
        return tableGroupRepository.save(new TableGroup(orderTables));
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroup(tableGroupId);
        final OrderTables orderTables = tableGroup.getOrderTables();
        final List<Order> orderList = findAllOrder(orderTables.ids());
        orderTables.ungroup(new Orders(orderList));
    }

    private TableGroup findTableGroup(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private List<Order> findAllOrder(final List<Long> orderTableIds) {
        return orderRepository.findAllByOrderTable_IdIn(orderTableIds);
    }
}
