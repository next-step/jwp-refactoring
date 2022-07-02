package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
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
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(tableGroup.getOrderTableIds());
        tableGroup.changeOrderTables(savedOrderTables);
        tableGroup.create(LocalDateTime.now());

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정을 찾을 수 없습니다."));
        validateUngroup(tableGroup.getOrderTableIds());

        tableGroup.ungroupOrderTables();
    }

    private void validateUngroup(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, OrderStatus.findNotCompletionStatus())) {
            throw new IllegalArgumentException("조리 또는 식사 중인 테이블은 단체 지정을 해제할 수 없습니다.");
        }
    }
}
