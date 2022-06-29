package kitchenpos.application;

import static kitchenpos.Exception.CannotUngroupException.CANNOT_UNGROUP_EXCEPTION;

import kitchenpos.Exception.NotFoundTableGroupException;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
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
            throw CANNOT_UNGROUP_EXCEPTION;
        }
    }

    private void validateNotFoundOrderTables(List<OrderTable> orderTables, List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("모든 테이블은 존재하는 테이블이어야 합니다.");
        }
    }
}
