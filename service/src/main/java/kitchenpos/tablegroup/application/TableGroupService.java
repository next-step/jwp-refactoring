package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository,
            OrderRepository orderRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        request.validateRequestedSizeOfOrderTables();
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(request.getOrderTableIds());
        request.validateSavedSizeOfOrderTables(savedOrderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));
        savedOrderTables.forEach(ot -> ot.occupy(savedTableGroup));
        return TableGroupResponse.from(savedTableGroup, savedOrderTables);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);
        if (isNotPaymentFinished(tableGroup)) {
            throw new IllegalArgumentException("테이블 그룹 중에 아직 결제가 끝나지 않은 주문이 있습니다.");
        }
        clearTables(tableGroup);
    }

    public TableGroup findById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TableGroup id:" + id + "가 존재하지 않습니다."));
    }

    private boolean isNotPaymentFinished(TableGroup tableGroup) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        return orderRepository.findByOrderTableIn(orderTables)
                .stream()
                .anyMatch(Order::isNotCompleted);
    }

    private void clearTables(TableGroup tableGroup) {
        orderTableRepository.findAllByTableGroup(tableGroup)
                .forEach(OrderTable::releaseInGroup);
    }
}
