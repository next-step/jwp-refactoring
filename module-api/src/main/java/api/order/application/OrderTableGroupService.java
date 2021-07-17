package api.order.application;

import api.order.application.exception.BadSizeOrderTableException;
import api.order.application.exception.NotExistOrderTableException;
import api.order.application.exception.NotExistOrderTableGroupException;
import api.order.dto.OrderTableGroupRequest;
import api.order.dto.OrderTableGroupResponse;
import domain.order.OrderTable;
import domain.order.OrderTableGroup;
import domain.order.OrderTableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTableGroupService {
    private static final int GROUP_TABLE_MIN_VALUE = 2;

    private final OrderTableGroupRepository orderTableGroupRepository;
    private final OrderTableService orderTableService;

    public OrderTableGroupService(OrderTableGroupRepository orderTableGroupRepository, OrderTableService orderTableService) {
        this.orderTableGroupRepository = orderTableGroupRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderTableGroupResponse create(final OrderTableGroupRequest orderTableGroupRequest) {
        List<Long> orderTableIds = getOrderTableIds(orderTableGroupRequest);
        List<OrderTable> savedOrderTables = getOrderTables(orderTableIds);
        OrderTableGroup savedOrderTableGroup = orderTableGroupRepository.save(OrderTableGroup.of(savedOrderTables));
        savedOrderTableGroup.grouped();
        return OrderTableGroupResponse.of(savedOrderTableGroup);
    }

    private List<OrderTable> getOrderTables(List<Long> orderTableIds) {
        List<OrderTable> savedOrderTables = orderTableService.findByIdIn(orderTableIds);
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new NotExistOrderTableException("요청한 테이블 아이디 중 잘못된 아이디가 있습니다.");
        }
        return savedOrderTables;
    }

    private List<Long> getOrderTableIds(OrderTableGroupRequest orderTableGroupRequest) {
        List<Long> orderTableIds = orderTableGroupRequest.getOrderTableIds();
        if (orderTableIds.isEmpty() || orderTableIds.size() < GROUP_TABLE_MIN_VALUE) {
            throw new BadSizeOrderTableException(GROUP_TABLE_MIN_VALUE);
        }
        return orderTableIds;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTableGroup orderTableGroup = orderTableGroupRepository.findById(tableGroupId)
                .orElseThrow(NotExistOrderTableGroupException::new);
        orderTableGroup.ungrouped();
    }
}
