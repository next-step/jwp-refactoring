package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequests) {
        final List<Long> orderTableIds = tableGroupRequests.getOrderTableIds();

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        checkAllTableSaved(orderTableIds, savedOrderTables);

        TableGroup tableGroup = new TableGroup();
        tableGroup.groupTables(savedOrderTables);

        TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(persistTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);

        checkOrderStatusUngroupPossible(tableGroup);

        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }

    @Transactional(readOnly = true)
    public TableGroup findTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("단체를 찾을 수 없습니다."));
    }

    private void checkOrderStatusUngroupPossible(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTableIds();

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리나 식사중인 테이블은 단체를 해제할 수 없습니다.");
        }
    }

    private void checkAllTableSaved(final List<Long> orderTableIds,
        final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("테이블을 찾을 수 없습니다.");
        }
    }
}
