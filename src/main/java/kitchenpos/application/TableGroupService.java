package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public TableGroupResponse create(final TableGroupRequest request) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.addOrderTables(getOrderTablesFromRequest(request));
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> getOrderTablesFromRequest(final TableGroupRequest request) {
        return request.getOrderTables()
                .stream()
                .map(id -> tableService.getById(id))
                .collect(Collectors.toList());
    }

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
