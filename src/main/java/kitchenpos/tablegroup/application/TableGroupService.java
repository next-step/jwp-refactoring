package kitchenpos.tablegroup.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderService orderService;
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderService orderService, TableService tableService, TableGroupRepository tableGroupRepository) {
        this.orderService = orderService;
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> orderTables = createOrderTables(request);
        TableGroup tableGroup = TableGroup.of(orderTables);
        final TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(saveTableGroup);
    }

    private List<OrderTable> createOrderTables(TableGroupRequest request) {
        return request.getOrderTableIds().stream()
                .map(id -> tableService.findById(id))
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단체입니다."));
        validUpGroup(tableGroup);
        tableGroup.changeUnGroup();
        tableGroupRepository.save(tableGroup);
    }

    private void validUpGroup(TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderService.existsNotCompletesByOrderTableIdIn(orderTableIds)) {
            throw new IllegalArgumentException("주문이 완료된 테이블만 그룹 해제가 가능합니다.");
        }
    }
}
