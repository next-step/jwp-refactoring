package kitchenpos.table.application;

import kitchenpos.Exception.CannotCreateOrderTableException;
import kitchenpos.Exception.UnCompletedOrderStatusException;
import kitchenpos.Exception.NotFoundTableGroupException;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderService orderService;
    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderService orderService, final OrderTableService orderTableService,
                             final TableGroupRepository tableGroupRepository) {
        this.orderService = orderService;
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.toOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableService.findAllOrderTablesByIdIn(orderTableIds);

        TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);

        validateNotFoundOrderTables(savedOrderTables, orderTableIds);

        tableGroup.groupOrderTables(OrderTables.from(savedOrderTables));

        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NotFoundTableGroupException::new);
        validateUnCompletedOrderStatus(tableGroup);
        tableGroup.unGroupOrderTables();
    }

    private void validateUnCompletedOrderStatus(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables()
                .getValue()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderService.existsByOrderTableIdUnCompletedOrderStatus(orderTableIds)) {
            throw new UnCompletedOrderStatusException("단체 내 모든 테이블의 주문 상태가 주문 또는 식사 상태이면 단체 지정을 해제할 수 없습니다.");
        }
    }

    private void validateNotFoundOrderTables(List<OrderTable> orderTables, List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new CannotCreateOrderTableException("모든 테이블은 존재하는 테이블이어야 합니다.");
        }
    }
}
