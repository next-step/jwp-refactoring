package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.dto.CreateTableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    public static final String COOKING_OR_MEAL_ORDER_TABLE_DEALLOCATE_ERROR_MESSAGE = "조리중, 식사중인 주문 테이블이 포함되어 있어 단체 지정을 해제 할 수 없습니다.";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupRequest createTableGroupRequest) {
        List<OrderTable> groupTargetOrderTables = findGroupingTargetOrderTables(createTableGroupRequest);
        TableGroup tableGroup = createTableGroupRequest.toTableGroup(groupTargetOrderTables);
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    public List<OrderTable> findGroupingTargetOrderTables(final CreateTableGroupRequest createTableGroupRequest) {
        return orderTableRepository.findAllByIdIn(createTableGroupRequest.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup persistGroup = findTableGroupById(tableGroupId);
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(persistGroup.getOrderTablesIds(), OrderStatus.canNotChangeOrderTableStatuses())) {
            throw new IllegalArgumentException(COOKING_OR_MEAL_ORDER_TABLE_DEALLOCATE_ERROR_MESSAGE);
        }
        persistGroup.deallocateOrderTable();
    }

    private TableGroup findTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException(COOKING_OR_MEAL_ORDER_TABLE_DEALLOCATE_ERROR_MESSAGE));
    }

    public TableGroupResponse findById(final Long tableGroupId) {
        return TableGroupResponse.from(findTableGroupById(tableGroupId));
    }
}
