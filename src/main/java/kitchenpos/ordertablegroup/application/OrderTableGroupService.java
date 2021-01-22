package kitchenpos.ordertablegroup.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import kitchenpos.ordertablegroup.domain.OrderTableGroupRepository;
import kitchenpos.ordertablegroup.dto.OrderTableGroupRequest;
import kitchenpos.ordertablegroup.dto.OrderTableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderTableGroupService {

    private final OrderDao orderDao;
    private final OrderTableService orderTableService;
    private final OrderTableGroupRepository orderTableGroupRepository;

    public OrderTableGroupService(OrderDao orderDao, OrderTableService orderTableService, OrderTableGroupRepository orderTableGroupRepository) {
        this.orderDao = orderDao;
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

        final List<Long> orderTableIds = orderTableGroup.getOrderTables().getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("`조리중`과 `식사중`에는 변경할 수 없다.");
        }

        orderTableGroup.unGroup();
    }

    private OrderTableGroup findById(Long tableGroupId) {
        return orderTableGroupRepository.findById(tableGroupId).orElseThrow(IllegalAccessError::new);
    }
}
