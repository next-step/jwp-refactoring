package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getIds());
        verifyAvailableTableGroupSize(tableGroupRequest.getOrderTables().size(), orderTables.size());
        TableGroup saveTableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        return TableGroupResponse.of(saveTableGroup);
    }

    private void verifyAvailableTableGroupSize(int requestSize, int dbSize) {
        if (requestSize != dbSize) {
            throw new IllegalArgumentException("요청한 단체지정의 주문테이블 수와 디비의 주문테이블 수가 불일치합니다.");
        }
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        final List<Long> orderTableIds = getOrderTableIds(tableGroup);
        verifyOrderStatus(orderTableIds);
        tableGroup.ungroup();
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체지정이 존재하지 않습니다."));
    }

    private void verifyOrderStatus(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문상태가 단체지정 할 수 없는 상태입니다.");
        }
    }

    private List<Long> getOrderTableIds(TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
                .map(orderTable -> orderTable.getId())
                .collect(Collectors.toList());
    }
}
