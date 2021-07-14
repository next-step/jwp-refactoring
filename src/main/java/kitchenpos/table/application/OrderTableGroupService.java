package kitchenpos.table.application;

import kitchenpos.table.application.exception.NotExistOrderTableGroupException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableGroup;
import kitchenpos.table.domain.OrderTableGroupRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.presentation.dto.OrderTableGroupRequest;
import kitchenpos.table.presentation.dto.OrderTableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTableGroupService {
    private final OrderTableGroupRepository orderTableGroupRepository;
    private final OrderTableService orderTableService;

    public OrderTableGroupService(OrderTableGroupRepository orderTableGroupRepository, OrderTableService orderTableService) {
        this.orderTableGroupRepository = orderTableGroupRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderTableGroupResponse create(final OrderTableGroupRequest orderTableGroupRequest) {
        List<OrderTable> savedOrderTables = orderTableService.findByIdIn(orderTableGroupRequest.getOrderTableIds());
        OrderTables orderTables = OrderTables.create(savedOrderTables, orderTableGroupRequest.getTableCount());
        OrderTableGroup orderTableGroup = OrderTableGroup.createWithMapping(orderTables);
        return OrderTableGroupResponse.of(orderTableGroupRepository.save(orderTableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTableGroup orderTableGroup = orderTableGroupRepository.findById(tableGroupId)
                .orElseThrow(NotExistOrderTableGroupException::new);
        orderTableGroup.validateNotCompletionStatus();
        orderTableGroup.ungroup();
    }
}
