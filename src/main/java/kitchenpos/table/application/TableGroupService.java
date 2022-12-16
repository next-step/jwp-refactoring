package kitchenpos.table.application;

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

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("저장된 테이블 리스트와 일치하지 않습니다.");
        }

        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(orderTables));

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        validateToUngroup(tableGroup);
        tableGroup.ungroup();
    }

    private void validateToUngroup(TableGroup tableGroup) {
        List<OrderTable> orderTables = tableGroup.getOrderTables();

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.notCompletion())) {
            throw new IllegalArgumentException("주문 상태가 조리 또는 식사 중인 경우 단체 지정 등록을 해제할 수 없습니다.");
        }
    }
}
