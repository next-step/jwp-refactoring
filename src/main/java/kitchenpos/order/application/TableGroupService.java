package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = findOrderTableAllBy(tableGroupRequest.getOrderTables());
        verifyAvailableTableGroupSize(tableGroupRequest.getOrderTables().size(), orderTables.size());
        TableGroup saveTableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        return TableGroupResponse.of(saveTableGroup);
    }

    private List<OrderTable> findOrderTableAllBy(List<OrderTableRequest> orderTableRequests) {
        List<Long> orderTableIds = findOrderTableIds(orderTableRequests);
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        return orderTables;
    }

    private List<Long> findOrderTableIds(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(orderTableRequest -> orderTableRequest.getId())
                .collect(Collectors.toList());
    }

    private void verifyAvailableTableGroupSize(int requestSize, int dbSize) {
        if (requestSize != dbSize) {
            throw new IllegalArgumentException("요청한 단체지정의 주문테이블 수와 디비의 주문테이블 수가 불일치합니다.");
        }
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        tableGroup.ungroup();
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체지정이 존재하지 않습니다."));
    }
}
