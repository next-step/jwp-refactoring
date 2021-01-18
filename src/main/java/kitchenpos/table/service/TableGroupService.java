package kitchenpos.table.service;

import kitchenpos.order.service.OrderStatusService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableService orderTableService;
    private final OrderStatusService orderStatusService;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableService orderTableService, OrderStatusService orderStatusService) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableService = orderTableService;
        this.orderStatusService = orderStatusService;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        checkGroupValidation(request);
        final TableGroup tableGroup = new TableGroup(findOrderTables(request));
        final TableGroup save = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(save);
    }

    private void checkGroupValidation(final TableGroupRequest request) {
        if (request.getOrderTables().isEmpty() || request.getOrderTables().size() < 2) {
            throw new IllegalArgumentException("단체 테이블을 지정할 수 없습니다.");
        }
    }

    private List<OrderTable> findOrderTables(final TableGroupRequest request) {
        return request.getOrderTables()
                .stream()
                .map(OrderTableIdRequest::getId)
                .map(orderTableService::findById)
                .collect(Collectors.toList());
    }


    public void ungroup(long id) {
        final List<OrderTable> orderTables = findGroupById(id).getOrderTables();
        orderTables.forEach(table -> {
            checkIsNotCompleteOrder(table);
            table.removeGroupTable();
        });
    }

    private void checkIsNotCompleteOrder(OrderTable tableId) {
        if (orderStatusService.isNotCompleteOrder(tableId)) {
            throw new IllegalArgumentException("주문이 완료되지 않아 그룹 해제가 불가능합니다.");
        }
    }

    @Transactional(readOnly = true)
    public TableGroup findGroupById(long id) {
        return tableGroupRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹을 찾을수 없습니다."));
    }
}
