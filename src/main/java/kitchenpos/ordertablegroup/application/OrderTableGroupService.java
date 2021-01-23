package kitchenpos.ordertablegroup.application;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import kitchenpos.ordertablegroup.domain.OrderTableGroupRepository;
import kitchenpos.ordertablegroup.dto.OrderTableGroupRequest;
import kitchenpos.ordertablegroup.dto.OrderTableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTableGroupService {

    private final OrderTableService orderTableService;
    private final OrderTableGroupRepository orderTableGroupRepository;

    public OrderTableGroupService(OrderTableService orderTableService, OrderTableGroupRepository orderTableGroupRepository) {
        this.orderTableService = orderTableService;
        this.orderTableGroupRepository = orderTableGroupRepository;
    }

    @Transactional
    public OrderTableGroupResponse create(final OrderTableGroupRequest request) {
        List<OrderTable> savedOrderTables = orderTableService.findAllByIdIn(request.getOrderTableIds());
        OrderTables orderTables = new OrderTables(savedOrderTables, request.getOrderTableIds().size());

        OrderTableGroup orderTableGroup = orderTableGroupRepository.save(new OrderTableGroup(orderTables));
        return OrderTableGroupResponse.of(orderTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTableGroup orderTableGroup = findById(tableGroupId);
        orderTableGroup.unGroup();
    }

    private OrderTableGroup findById(Long tableGroupId) {
        return orderTableGroupRepository.findById(tableGroupId).orElseThrow(IllegalAccessError::new);
    }
}
