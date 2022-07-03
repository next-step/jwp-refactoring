package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final OrderService orderService;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final TableService tableService,
                             final OrderService orderService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
        this.orderService = orderService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.addOrderTables(getOrderTablesFromRequest(request));
        return TableGroupResponse.of(tableGroup);
    }

    private List<OrderTable> getOrderTablesFromRequest(final TableGroupRequest request) {
        return request.getOrderTables()
                .stream()
                .map(id -> tableService.getById(id))
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long id) {
        final TableGroup savedTableGroup = tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("테이블 그룹을 찾을 수 없습니다. id: %d", id)));

        validateOrderTableToUngroup(savedTableGroup);

        savedTableGroup.removeRelationsToOrderTables();
        tableGroupRepository.save(savedTableGroup);

        tableGroupRepository.delete(savedTableGroup);
    }

    private void validateOrderTableToUngroup(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());
        if (orderService.existsNotCompletesByOrderTableIdIn(orderTableIds)) {
            throw new IllegalStateException("주문이 완료되지 않은 테이블이 있는 그룹은 해제할 수 없습니다.");
        }
    }
}
