package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.CreateTableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TableGroupService {
    public static final String ORDER_STATUS_EXCEPTION_MESSAGE = "주문상태가 완료일 경우에만 해제가능합니다.";
//    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(/*final OrderRepository orderRepository, */final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
//        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupRequest request) {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(findOrderTables(request.getOrderTableIds())));
        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(EntityNotFoundException::new);
//        validateOrderStatus(tableGroup);
        tableGroup.upGroup();
    }

    private OrderTables findOrderTables(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        validateOrderTables(orderTableIds, savedOrderTables);
        return new OrderTables(savedOrderTables);
    }

    private void validateOrderTables(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }
//
//    private void validateOrderStatus(TableGroup tableGroup) {
//        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
//                tableGroup.getOrderTableIds(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
//            throw new IllegalArgumentException(ORDER_STATUS_EXCEPTION_MESSAGE);
//        }
//    }
}
