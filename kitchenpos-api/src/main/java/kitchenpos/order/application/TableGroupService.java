package kitchenpos.order.application;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.dao.TableGroupRepository;
import kitchenpos.order.domain.TableGroup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
        final OrderRepository orderRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.updateOrderTables(findOrderTables(tableGroupRequest.orderTableIds()));
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 단체 입니다."));

        checkOrderStatus(tableGroup.getOrderTables());
        tableGroup.unGroup();
        tableGroupRepository.save(tableGroup);
    }

    private OrderTables findOrderTables(List<Long> orderTableIds) {
        final OrderTables savedOrderTables = new OrderTables(
            orderTableRepository.findAllByIdIn(orderTableIds)
        );

        if (!savedOrderTables.sameSizeWith(orderTableIds.size())) {
            throw new IllegalArgumentException("등록되지 않은 테이블이 포함되어 있습니다.");
        }
        return savedOrderTables;
    }

    private void checkOrderStatus (List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
            orderTables, OrderStatus.UNCHANGEABLE_STATUS)) {
            throw new IllegalArgumentException("주문 상태가 조리중이거나 식사중인 테이블의 단체 지정은 해지할 수 없습니다.");
        }
    }
}
