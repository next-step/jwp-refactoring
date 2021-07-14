package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.ordering.domain.OrderRepository;
import kitchenpos.ordering.domain.OrderStatus;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        checkOrderTablesAreAllSaved(savedOrderTables, tableGroupRequest);

        TableGroup tableGroup = TableGroup.of(savedOrderTables);
        tableGroup.grouping();

        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup persistTableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        checkOrderStatusInOrderTablesIfAllComplete(persistTableGroup.getOrderTableIds());

        persistTableGroup.ungrouping();
    }

    private void checkOrderTablesAreAllSaved(final List<OrderTable> orderTables, final TableGroupRequest tableGroupRequest) {
        if (orderTables.size()
            != tableGroupRequest.getOrderTableIds().size()) {
            throw new IllegalArgumentException("등록되지 않은 주문테이블이 있으면 그룹을 만들 수 없습니다.");
        }
    }

    private void checkOrderStatusInOrderTablesIfAllComplete(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문테이블들의 주문들 중 아직 완료되지 않은 주문이 있습니다.");
        }
    }

}
