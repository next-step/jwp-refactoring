package kitchenpos.tobe.tablegroup.application;

import kitchenpos.tobe.order.domain.OrderRepository;
import kitchenpos.tobe.order.domain.OrderStatus;
import kitchenpos.tobe.table.domain.OrderTable;
import kitchenpos.tobe.table.domain.OrderTableRepository;
import kitchenpos.tobe.table.domain.OrderTables;
import kitchenpos.tobe.tablegroup.domain.TableGroup;
import kitchenpos.tobe.tablegroup.domain.TableGroupRepository;
import kitchenpos.tobe.tablegroup.dto.TableGroupRequest;
import kitchenpos.tobe.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private static final String INVALID_CHANGE_ORDER_STATUS = "변경 할 수 없는 주문 상태입니다";
    private static final String TABLE_GROUP_NOT_EXIST = "테이블그룹이 존재하지 않습니다";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        TableGroup tableGroup = TableGroup.createWithIdValidation(orderTableIds, new OrderTables(orderTables));
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupNotFoundException(TABLE_GROUP_NOT_EXIST));
        checkOrderStatus(tableGroup.getOrderTableIds());
        tableGroup.ungroup();
    }

    private void checkOrderStatus(List<Long> orderTableId) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalStateException(INVALID_CHANGE_ORDER_STATUS);
        }
    }
}
