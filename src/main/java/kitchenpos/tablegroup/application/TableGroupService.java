package kitchenpos.tablegroup.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
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
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderService orderService, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        validSizeCheck(request.getOrderTableIds());
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(request.getOrderTableIds());
        validOrderTableSizeIsEqual(orderTables, request.getOrderTableIds());

        TableGroup tableGroup = TableGroup.of(orderTables);
        final TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(saveTableGroup);
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
        final List<Long> orderTableIds = orderTableRepository.findAllByTableGroupId(tableGroup.getId()).stream()
                .map(OrderTable::getTableGroupId)
                .collect(Collectors.toList());

        if (orderService.existsNotCompletesByOrderTableIdIn(orderTableIds)) {
            throw new IllegalStateException("주문이 완료된 테이블만 그룹 해제가 가능합니다.");
        }
    }

    private void validSizeCheck(List<Long> orderTableIds) {
        validOrderTableEmpty(orderTableIds);
        validOrderTableMinSize(orderTableIds);
    }

    private void validOrderTableEmpty(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds)) {
            throw new IllegalArgumentException("주문 테이블이 입력되지 않았습니다.");
        }
    }

    private void validOrderTableMinSize(List<Long> orderTableIds) {
        if (orderTableIds.size() < 2) {
            throw new IllegalArgumentException("총 주문 테이블수는 2보다 작을 수 없습니다.");
        }
    }

    private void validOrderTableSizeIsEqual(List<OrderTable> orderTables, List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("등록되지 않은 주문테이블이 있습니다.");
        }
    }
}
